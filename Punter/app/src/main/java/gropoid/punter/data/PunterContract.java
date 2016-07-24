package gropoid.punter.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and columns for the Punter database
 */
public class PunterContract {

    public static final String CONTENT_AUTHORITY = "gropoid.punter.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_GAME = "game";
    public static final String PATH_PLATFORM = "platform";
    public static final String PATH_GAME_PLATFORM = "gameplatform";
    public static final String PATH_QUESTION = "question";

    /* Table contents of the game table */
    public static final class GameEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAME).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME;

        public static final String TABLE_NAME = "game";

        public static final String COLUMN_GIANT_BOMB_ID = "giant_bomb_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_DECK = "deck";
        public static final String COLUMN_ORIGINAL_RELEASE_DATE = "original_release_date";
    }

    public static final class PlatformEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLATFORM).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLATFORM;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLATFORM;

        public static final String TABLE_NAME = "platform";

        public static final String COLUMN_GIANT_BOMB_ID = "giant_bomb_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ABBREVIATION = "abbreviation";
    }

    public static final class GamePlatformEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAME_PLATFORM).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME_PLATFORM;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME_PLATFORM;

        public static final String TABLE_NAME = "gameplatform";

        public static final String COLUMN_GAME = "game";
        public static final String COLUMN_PLATFORM = "platform";
    }

    public static final class QuestionEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUESTION).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUESTION;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUESTION;

        public static final String TABLE_NAME = "question";

        public static final String COLUMN_TYPE = "type";

        public static final String COLUMN_ANSWER1 = "answer1";
        public static final String COLUMN_ANSWER2 = "answer2";
        public static final String COLUMN_ANSWER3 = "answer3";
        public static final String COLUMN_ANSWER4 = "answer4";

        public static final String COLUMN_CORRECT_ANSWER = "correct_answer";

    }

}
