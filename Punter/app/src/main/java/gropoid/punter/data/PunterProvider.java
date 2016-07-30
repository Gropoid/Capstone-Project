package gropoid.punter.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import gropoid.punter.injection.DaggerBackgroundComponent;
import gropoid.punter.injection.DataAccessModule;
import gropoid.punter.injection.NetworkModule;
import timber.log.Timber;

import static gropoid.punter.data.PunterContract.*;


public class PunterProvider extends ContentProvider {

    @Inject
    PunterDbHelper punterDbHelper;

    static final int GAMES = 100;

    static final int PLATFORMS = 200;

    static final int QUESTIONS = 300;

    static final int GAME_PLATFORMS = 400;

    public static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, PATH_GAME, GAMES);
        matcher.addURI(authority, PATH_PLATFORM, PLATFORMS);
        matcher.addURI(authority, PATH_QUESTION, QUESTIONS);
        matcher.addURI(authority, PATH_GAME_PLATFORM, GAME_PLATFORMS);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        DaggerBackgroundComponent.builder()
                .dataAccessModule(new DataAccessModule(getContext()))
                .networkModule(new NetworkModule())
                .build()
                .inject(this);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case GAMES:
                retCursor = punterDbHelper.getReadableDatabase().query(
                        GameEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PLATFORMS:
                retCursor = punterDbHelper.getReadableDatabase().query(
                        PlatformEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case QUESTIONS:
                retCursor = punterDbHelper.getReadableDatabase().query(
                        QuestionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case GAME_PLATFORMS:
                retCursor = punterDbHelper.getReadableDatabase().query(
                        GamePlatformEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs, null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case GAMES:
                return GameEntry.CONTENT_TYPE;
            case PLATFORMS:
                return PlatformEntry.CONTENT_TYPE;
            case QUESTIONS:
                return QuestionEntry.CONTENT_TYPE;
            case GAME_PLATFORMS:
                return GamePlatformEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Timber.v("insert (uri = %s, values = %s )", uri, values.toString());
        final SQLiteDatabase db = punterDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case GAMES:
                db.insertOrThrow(GameEntry.TABLE_NAME, null, values);
                //noinspection ConstantConditions
                getContext().getContentResolver().notifyChange(uri, null);
                return GameEntry.CONTENT_URI;
            case GAME_PLATFORMS:
                db.insertOrThrow(GamePlatformEntry.TABLE_NAME, null, values);
                //noinspection ConstantConditions
                getContext().getContentResolver().notifyChange(uri, null);
                return GamePlatformEntry.CONTENT_URI;
            case PLATFORMS:
                db.insertOrThrow(PlatformEntry.TABLE_NAME, null, values);
                //noinspection ConstantConditions
                getContext().getContentResolver().notifyChange(uri, null);
                return PlatformEntry.CONTENT_URI;
            case QUESTIONS:
                db.insertOrThrow(QuestionEntry.TABLE_NAME, null, values);
                //noinspection ConstantConditions
                getContext().getContentResolver().notifyChange(uri, null);
                return QuestionEntry.CONTENT_URI;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Timber.v("delete (uri = %s, selection = %s )", uri, selection);
        final SQLiteDatabase db = punterDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) {
            selection = "1";
        }
        switch (match) {
            case GAMES:
                rowsDeleted = db.delete(GameEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case PLATFORMS:
                rowsDeleted = db.delete(PlatformEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case GAME_PLATFORMS:
                rowsDeleted = db.delete(PlatformEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case QUESTIONS:
                rowsDeleted = db.delete(QuestionEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Timber.v("update (uri = %s, values = %s )", uri, values.toString());
        final SQLiteDatabase db = punterDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;
        if (null == selection) {
            selection = "1";
        }
        switch (match) {
            case GAMES:
                rowsUpdated = db.update(GameEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case PLATFORMS:
                rowsUpdated = db.update(PlatformEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case GAME_PLATFORMS:
                rowsUpdated = db.update(PlatformEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case QUESTIONS:
                rowsUpdated = db.update(QuestionEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
