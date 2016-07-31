package gropoid.punter.data;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import gropoid.punter.domain.Game;
import gropoid.punter.domain.Platform;
import gropoid.punter.domain.Question;
import timber.log.Timber;

import static gropoid.punter.data.PunterContract.GameEntry;
import static gropoid.punter.data.PunterContract.GamePlatformEntry;
import static gropoid.punter.data.PunterContract.PlatformEntry;

public class Repository {

    @Inject
    ContentResolver contentResolver;

    @Inject
    public Repository(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public int save(Game game) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameEntry.COLUMN_DECK, game.getDeck());
        contentValues.put(GameEntry.COLUMN_API_DETAIL_URL, game.getApiDetailUrl());
        contentValues.put(GameEntry.COLUMN_GIANT_BOMB_ID, game.getId());
        contentValues.put(GameEntry.COLUMN_IMAGE, game.getImageFile());
        contentValues.put(GameEntry.COLUMN_NAME, game.getName());
        contentValues.put(GameEntry.COLUMN_USES, game.getUses());

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
        game.setUses(c.getInt(c.getColumnIndexOrThrow(GameEntry.COLUMN_USES)));
        Timber.d("got game from database (%s)", game.getName());
        return game;
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
            return 1;
        }
    }

    public Uri save(Question question) {
        return null;
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
        return platformIds;
    }
}
