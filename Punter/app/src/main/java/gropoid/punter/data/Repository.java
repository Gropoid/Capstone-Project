package gropoid.punter.data;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

import javax.inject.Inject;

import gropoid.punter.domain.Game;
import gropoid.punter.domain.Platform;
import gropoid.punter.domain.Question;

import static gropoid.punter.data.PunterContract.GameEntry;
import static gropoid.punter.data.PunterContract.GamePlatformEntry;
import static gropoid.punter.data.PunterContract.PlatformEntry;

public class Repository {

    @Inject
    ContentResolver contentResolver;

    @Inject
    public Repository(ContentResolver contentResolver) {
    }

    public int save(Game game) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameEntry.COLUMN_DECK, game.getDeck());
        contentValues.put(GameEntry.COLUMN_API_DETAIL_URL, game.getApiDetailUrl());
        contentValues.put(GameEntry.COLUMN_GIANT_BOMB_ID, game.getId());
        contentValues.put(GameEntry.COLUMN_IMAGE, game.getImageFile());
        contentValues.put(GameEntry.COLUMN_NAME, game.getName());
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
            int platform_updated_count = save(platform);
            saveJunction(game.getId(), platform.getId());
        }

        return update_count;
    }

    private Game findGameById(long giantBombApiId) {
        String giantBombApiArg = String.valueOf(giantBombApiId);
        Cursor c = contentResolver.query(GameEntry.CONTENT_URI,
                null,
                GameEntry.COLUMN_GIANT_BOMB_ID + "=?",
                new String[]{giantBombApiArg},
                null);
        if (c != null && c.moveToFirst()) {
            Game game = new Game();
            game.setId(c.getLong(c.getColumnIndexOrThrow(GameEntry.COLUMN_GIANT_BOMB_ID)));
            game.setImageFile(c.getString(c.getColumnIndexOrThrow(GameEntry.COLUMN_IMAGE)));
            game.setOriginalReleaseDate(new Date(c.getLong(c.getColumnIndexOrThrow(GameEntry.COLUMN_ORIGINAL_RELEASE_DATE))));
            game.setDeck(c.getString(c.getColumnIndexOrThrow(GameEntry.COLUMN_DECK)));
            game.setApiDetailUrl(c.getString(c.getColumnIndexOrThrow(GameEntry.COLUMN_API_DETAIL_URL)));
            c.close();
            return game;
        }
        return null;
    }

    public int save(Platform platform) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlatformEntry.COLUMN_GIANT_BOMB_ID, platform.getId());
        contentValues.put(PlatformEntry.COLUMN_NAME, platform.getName());
        contentValues.put(PlatformEntry.COLUMN_ABBREVIATION, platform.getAbbreviation());

        int update_count = contentResolver.update(
                PlatformEntry.CONTENT_URI,
                contentValues,
                PlatformEntry.COLUMN_GIANT_BOMB_ID + " + ?",
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
}
