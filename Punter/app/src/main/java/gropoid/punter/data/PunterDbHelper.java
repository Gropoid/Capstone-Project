package gropoid.punter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import gropoid.punter.data.PunterContract.GameEntry;
import gropoid.punter.data.PunterContract.GamePlatformEntry;
import gropoid.punter.data.PunterContract.PlatformEntry;
import gropoid.punter.data.PunterContract.QuestionEntry;


public class PunterDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "punter.db";

    public PunterDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GAME_TABLE = createGameTable();
        final String SQL_CREATE_PLATFORM_TABLE = createPlatformTable();
        final String SQL_CREATE_GAMEPLATFORM_TABLE = createGamePlatformTable();
        final String SQL_CREATE_QUESTION_TABLE = createQuestionTable();

        db.execSQL(SQL_CREATE_GAME_TABLE);
        db.execSQL(SQL_CREATE_PLATFORM_TABLE);
        db.execSQL(SQL_CREATE_GAMEPLATFORM_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PlatformEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GamePlatformEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionEntry.TABLE_NAME);
        onCreate(db);
    }

    private String createGameTable() {
        return "CREATE TABLE " + GameEntry.TABLE_NAME + " (" +
                GameEntry.COLUMN_GIANT_BOMB_ID + " INTEGER PRIMARY KEY," +
                GameEntry.COLUMN_DECK + " TEXT," +
                GameEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                GameEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                GameEntry.COLUMN_ORIGINAL_RELEASE_DATE + " INTEGER, " +
                GameEntry.COLUMN_API_DETAIL_URL + " TEXT, " +
                GameEntry.COLUMN_USES + " INTEGER" +
                ");";
    }

    private String createPlatformTable() {
        return "CREATE TABLE " + PlatformEntry.TABLE_NAME + " (" +
                PlatformEntry.COLUMN_GIANT_BOMB_ID + " INTEGER PRIMARY KEY, " +
                PlatformEntry.COLUMN_NAME + " TEXT, " +
                PlatformEntry.COLUMN_ABBREVIATION + " TEXT" +
                ");";
    }

    private String createGamePlatformTable() {
        return "CREATE TABLE " + GamePlatformEntry.TABLE_NAME + " (" +
                GamePlatformEntry._ID + " INTEGER PRIMARY KEY, " +
                GamePlatformEntry.COLUMN_GAME + " INTEGER REFERENCES " + GameEntry.TABLE_NAME + ", "+
                GamePlatformEntry.COLUMN_PLATFORM + " INTEGER REFERENCES " + PlatformEntry.TABLE_NAME +
                ")";
    }

    private String createQuestionTable() {
        return "CREATE TABLE " + QuestionEntry.TABLE_NAME + " (" +
                QuestionEntry._ID + " INTEGER PRIMARY KEY, " +
                QuestionEntry.COLUMN_ANSWER1 + " INTEGER REFERENCES " + GameEntry.TABLE_NAME + ", "+
                QuestionEntry.COLUMN_ANSWER2 + " INTEGER REFERENCES " + GameEntry.TABLE_NAME + ", "+
                QuestionEntry.COLUMN_ANSWER3 + " INTEGER REFERENCES " + GameEntry.TABLE_NAME + ", "+
                QuestionEntry.COLUMN_ANSWER4 + " INTEGER REFERENCES " + GameEntry.TABLE_NAME + ", "+
                QuestionEntry.COLUMN_CORRECT_ANSWER + " INTEGER " +
                ")";
    }
}
