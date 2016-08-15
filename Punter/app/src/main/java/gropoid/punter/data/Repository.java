package gropoid.punter.data;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import gropoid.punter.data.PunterContract.LocalHighScoreEntry;
import gropoid.punter.domain.Game;
import gropoid.punter.domain.Platform;
import gropoid.punter.domain.Question;
import hugo.weaving.DebugLog;
import timber.log.Timber;

import static gropoid.punter.data.PunterContract.GameEntry;
import static gropoid.punter.data.PunterContract.GamePlatformEntry;
import static gropoid.punter.data.PunterContract.PlatformEntry;
import static gropoid.punter.data.PunterContract.QuestionEntry;

public class Repository {

    private static final String LOCAL_HIGH_SCORE_ID = "0";
    @Inject
    ContentResolver contentResolver;

    @Inject
    public Repository(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public int save(Game game) {
        if (game == null) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameEntry.COLUMN_DECK, game.getDeck());
        contentValues.put(GameEntry.COLUMN_API_DETAIL_URL, game.getApiDetailUrl());
        contentValues.put(GameEntry.COLUMN_GIANT_BOMB_ID, game.getId());
        contentValues.put(GameEntry.COLUMN_IMAGE, game.getImageFile());
        contentValues.put(GameEntry.COLUMN_NAME, game.getName());
        contentValues.put(GameEntry.COLUMN_ACTUAL_USES, game.getActualUses());
        contentValues.put(GameEntry.COLUMN_PLANNED_USES, game.getPlannedUses());

        if (game.getOriginalReleaseDate() != null)
            contentValues.put(GameEntry.COLUMN_ORIGINAL_RELEASE_DATE, game.getOriginalReleaseDate().getTime());
        int update_count = contentResolver.update(
                GameEntry.CONTENT_URI,
                contentValues,
                GameEntry.COLUMN_GIANT_BOMB_ID + " = ?",
                new String[]{String.valueOf(game.getId())}
        );
        if (update_count == 0) {
            contentResolver.insert(
                    GameEntry.CONTENT_URI,
                    contentValues);
        }
        for (Platform platform : game.getPlatforms()) {
            save(platform);
            saveJunction(game.getId(), platform.getId());
        }

        return update_count;
    }

    @NonNull
    private Game getGameFromCursor(Cursor c) {
        Game game = new Game();
        game.setId(c.getLong(c.getColumnIndexOrThrow(GameEntry.COLUMN_GIANT_BOMB_ID)));
        game.setName(c.getString(c.getColumnIndexOrThrow(GameEntry.COLUMN_NAME)));
        game.setImageFile(c.getString(c.getColumnIndexOrThrow(GameEntry.COLUMN_IMAGE)));
        game.setOriginalReleaseDate(new Date(c.getLong(c.getColumnIndexOrThrow(GameEntry.COLUMN_ORIGINAL_RELEASE_DATE))));
        game.setDeck(c.getString(c.getColumnIndexOrThrow(GameEntry.COLUMN_DECK)));
        game.setApiDetailUrl(c.getString(c.getColumnIndexOrThrow(GameEntry.COLUMN_API_DETAIL_URL)));
        game.setActualUses(c.getInt(c.getColumnIndexOrThrow(GameEntry.COLUMN_ACTUAL_USES)));
        game.setPlannedUses(c.getInt(c.getColumnIndexOrThrow(GameEntry.COLUMN_PLANNED_USES)));
        game.setPlatforms(findPlatformsForGameId(game.getId()));
        Timber.d("got game from database (%s)", game.getName());
        return game;
    }

    @DebugLog
    private List<Platform> findPlatformsForGameId(long gameId) {
        List<Platform> platforms = new ArrayList<>();
        for (long platformId : findPlatformIdsForGameId(gameId)) {
            Cursor c = contentResolver.query(
                    PlatformEntry.CONTENT_URI,
                    null,
                    PlatformEntry.COLUMN_GIANT_BOMB_ID + " = ?",
                    new String[]{String.valueOf(platformId)},
                    null
            );
            if (c != null && c.moveToFirst()) {
                do {
                    platforms.add(getPlatformFromCursor(c));
                } while (c.moveToNext());
                c.close();
            }
        }
        return platforms;
    }

    @NonNull
    private Platform getPlatformFromCursor(Cursor c) {
        Platform platform = new Platform();
        platform.setId(c.getLong(c.getColumnIndexOrThrow(PlatformEntry.COLUMN_GIANT_BOMB_ID)));
        platform.setName(c.getString(c.getColumnIndexOrThrow(PlatformEntry.COLUMN_NAME)));
        platform.setAbbreviation(c.getString(c.getColumnIndexOrThrow(PlatformEntry.COLUMN_ABBREVIATION)));
        Timber.d("got platform from database (%s)", platform.getName());
        return platform;
    }

    public int save(Platform platform) {
        if (platform == null)
            return 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlatformEntry.COLUMN_GIANT_BOMB_ID, platform.getId());
        contentValues.put(PlatformEntry.COLUMN_NAME, platform.getName());
        contentValues.put(PlatformEntry.COLUMN_ABBREVIATION, platform.getAbbreviation());

        int update_count = contentResolver.update(
                PlatformEntry.CONTENT_URI,
                contentValues,
                PlatformEntry.COLUMN_GIANT_BOMB_ID + " = ?",
                new String[]{String.valueOf(platform.getId())}
        );

        if (update_count == 0) {
            contentResolver.insert(
                    PlatformEntry.CONTENT_URI,
                    contentValues
            );
        }
        return update_count;
    }

    private int saveJunction(long gameId, long platformId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GamePlatformEntry.COLUMN_GAME, gameId);
        contentValues.put(GamePlatformEntry.COLUMN_PLATFORM, platformId);
        Cursor c = contentResolver.query(
                GamePlatformEntry.CONTENT_URI,
                null,
                GamePlatformEntry.COLUMN_GAME + " = ? AND " + GamePlatformEntry.COLUMN_PLATFORM + " = ?",
                new String[]{String.valueOf(gameId), String.valueOf(platformId)},
                null);
        if (c != null && c.moveToFirst()) {
            // junction already exists
            c.close();
            return 0;
        } else {
            contentResolver.insert(
                    GamePlatformEntry.CONTENT_URI,
                    contentValues
            );
            if (c != null) {
                c.close();
            }
            return 1;
        }
    }

    public void save(Question question) {
        if (question == null)
            return;
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionEntry.COLUMN_TYPE, question.getType());
        contentValues.put(QuestionEntry.COLUMN_ANSWER1, question.getGames()[0].getId());
        contentValues.put(QuestionEntry.COLUMN_ANSWER2, question.getGames()[1].getId());
        contentValues.put(QuestionEntry.COLUMN_ANSWER3, question.getGames()[2].getId());
        contentValues.put(QuestionEntry.COLUMN_ANSWER4, question.getGames()[3].getId());
        contentValues.put(QuestionEntry.COLUMN_CORRECT_ANSWER, question.getCorrectAnswer().getId());
        contentValues.put(QuestionEntry.COLUMN_CRITERION, question.getCorrectAnswerCriterion());
        contentValues.put(QuestionEntry.COLUMN_WORDING, question.getWording());


        contentResolver.insert(
                QuestionEntry.CONTENT_URI,
                contentValues);
    }

    public List<Game> findAllGames() {
        List<Game> games = new ArrayList<>();
        Cursor c = contentResolver.query(
                GameEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (c != null && c.moveToFirst()) {
            do {
                games.add(getGameFromCursor(c));
            } while (c.moveToNext());
            c.close();
        }
        return games;
    }

    public List<Platform> findAllPlatforms() {
        List<Platform> platforms = new ArrayList<>();
        Cursor c = contentResolver.query(
                PlatformEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (c != null && c.moveToFirst()) {
            do {
                platforms.add(getPlatformFromCursor(c));
            } while (c.moveToNext());
            c.close();
        }
        return platforms;
    }

    public List<Long> findPlatformIdsForGameId(long gameId) {
        List<Long> platformIds = new ArrayList<>();
        Cursor c = contentResolver.query(
                GamePlatformEntry.CONTENT_URI,
                new String[]{GamePlatformEntry.COLUMN_PLATFORM},
                GamePlatformEntry.COLUMN_GAME + " = ? ",
                new String[]{String.valueOf(gameId)},
                null
        );
        if (c != null && c.moveToFirst()) {
            do {
                platformIds.add(c.getLong(c.getColumnIndex(GamePlatformEntry.COLUMN_PLATFORM)));
            } while (c.moveToNext());
            c.close();
        }
        if (platformIds.size() == 0) {
            Timber.e("Found zero platforms for game %s", gameId);
        }
        return platformIds;
    }

    @DebugLog
    public int getQuestionsCount() {
        Cursor c = contentResolver.query(QuestionEntry.CONTENT_URI, new String[]{"count(*)"}, null, null, null);
        if (c == null) {
            return 0;
        }
        if (c.getCount() == 0) {
            c.close();
            return 0;
        } else {
            c.moveToFirst();
            int result = c.getInt(0);
            c.close();
            return result;
        }
    }


    public List<Question> findQuestions(int count) {
        List<Question> questions = new ArrayList<>(count);
        Cursor c = contentResolver.query(
                QuestionEntry.CONTENT_URI,
                null,
                null,
                null,
                String.format("_ID ASC LIMIT %s", count)
        );
        if (c != null && c.moveToFirst()) {
            do {
                Question question = getQuestionFromCursor(c);
                questions.add(question);
            } while (c.moveToNext());
            c.close();
        }
        return questions;
    }

    @NonNull
    private Question getQuestionFromCursor(Cursor c) {
        Question question = new Question();
        question.setId(c.getLong(c.getColumnIndex(QuestionEntry._ID)));
        question.setCorrectAnswer(findGameById(c.getLong(c.getColumnIndex(QuestionEntry.COLUMN_CORRECT_ANSWER))));
        Game[] games = new Game[4];
        games[0] = findGameById(c.getLong(c.getColumnIndex(QuestionEntry.COLUMN_ANSWER1)));
        games[1] = findGameById(c.getLong(c.getColumnIndex(QuestionEntry.COLUMN_ANSWER2)));
        games[2] = findGameById(c.getLong(c.getColumnIndex(QuestionEntry.COLUMN_ANSWER3)));
        games[3] = findGameById(c.getLong(c.getColumnIndex(QuestionEntry.COLUMN_ANSWER4)));
        question.setGames(games);
        question.setType(c.getInt(c.getColumnIndex(QuestionEntry.COLUMN_TYPE)));
        question.setCorrectAnswerCriterion(c.getLong(c.getColumnIndex(QuestionEntry.COLUMN_CRITERION)));
        question.setWording(c.getString(c.getColumnIndex(QuestionEntry.COLUMN_WORDING)));
        return question;
    }

    public Game findGameById(long id) {
        Game game = null;
        Cursor c = contentResolver.query(
                GameEntry.CONTENT_URI,
                null,
                GameEntry.COLUMN_GIANT_BOMB_ID + " = ?",
                new String[]{String.valueOf(id)},
                null
        );
        if (c != null && c.moveToFirst()) {
            game = getGameFromCursor(c);
            c.close();
        }
        return game;
    }

    public int findGamePendingUsesByGameId(long id) {
        int gamePendingUses = -1;
        Cursor c = contentResolver.query(
                GameEntry.CONTENT_URI,
                new String[]{GameEntry.COLUMN_PLANNED_USES},
                GameEntry.COLUMN_GIANT_BOMB_ID + " = ?",
                new String[]{String.valueOf(id)},
                null
        );
        if (c != null && c.moveToFirst()) {
            gamePendingUses = c.getInt(c.getColumnIndex(GameEntry.COLUMN_PLANNED_USES));
            c.close();
        }
        return gamePendingUses;
    }

    public int updateGameUsesById(long id, int uses) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameEntry.COLUMN_ACTUAL_USES, uses);
        return contentResolver.update(GameEntry.CONTENT_URI,
                contentValues,
                GameEntry.COLUMN_GIANT_BOMB_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public int getGamesCount() {
        Cursor c = contentResolver.query(
                GameEntry.CONTENT_URI,
                new String[]{"count(*)"},
                null,
                null,
                null);
        if (c == null) {
            return 0;
        }
        if (c.getCount() == 0) {
            c.close();
            return 0;
        } else {
            c.moveToFirst();
            int result = c.getInt(0);
            c.close();
            return result;
        }
    }

    public void delete(Game game) {
        if (contentResolver.delete(
                GameEntry.CONTENT_URI,
                GameEntry.COLUMN_GIANT_BOMB_ID + " = ?",
                new String[]{String.valueOf(game.getId())}
        ) != 1) {
            Timber.w("Trying to delete nonexistant game (%s)", game.getName());
        } else {
            int rows = contentResolver.delete(
                    GamePlatformEntry.CONTENT_URI,
                    GamePlatformEntry.COLUMN_GAME + " = ?",
                    new String[]{String.valueOf(game.getId())}
            );
            Timber.v("deleted %s rows out of %s", rows, game.getPlatforms().size());
        }
    }

    public void delete(Question question) {
        if (contentResolver.delete(
                QuestionEntry.CONTENT_URI,
                QuestionEntry._ID + " = ?",
                new String[]{String.valueOf(question.getId())}
        ) != 1) {
            Timber.w("Trying to delete nonexistant question (%s)", question.getId());
        }
    }

    public int findLocalHighScore() {
        int localHighScore = 0;
        Cursor c = contentResolver.query(
                LocalHighScoreEntry.CONTENT_URI,
                new String[]{LocalHighScoreEntry.COLUMN_HIGH_SCORE},
                LocalHighScoreEntry.COLUMN_ID + " = ?",
                new String[]{LOCAL_HIGH_SCORE_ID},
                null
        );
        if (c != null && c.moveToFirst()) {
            localHighScore = c.getInt(c.getColumnIndex(LocalHighScoreEntry.COLUMN_HIGH_SCORE));
            c.close();
        }
        return localHighScore;
    }

    public void saveLocalHighScore(int localHighScore) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocalHighScoreEntry.COLUMN_ID, LOCAL_HIGH_SCORE_ID);
        contentValues.put(LocalHighScoreEntry.COLUMN_HIGH_SCORE, localHighScore);
        int update_count = contentResolver.update(
                LocalHighScoreEntry.CONTENT_URI,
                contentValues,
                LocalHighScoreEntry.COLUMN_ID + " = ?",
                new String[]{LOCAL_HIGH_SCORE_ID}
        );
        if (update_count == 0) {
            contentResolver.insert(
                    LocalHighScoreEntry.CONTENT_URI,
                    contentValues);
        }
    }
}
