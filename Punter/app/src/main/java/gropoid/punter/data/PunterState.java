package gropoid.punter.data;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

public class PunterState {
    public static final String PUNTER_STATE = "PunterState";

    public static final int HOME = 0;
    public static final int QUIZZ = 1;
    public static final int END_GAME = 2;
    public static final String CURRENT_STATE = "CurrentState";
    private static final String CURRENT_API_GAME_PAGE = "CurrentApiGamePage";
    private static final String SCORE = "Score";
    private static final String HIGH_SCORE_BUFFER = "HighScoreBuffer";

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    public PunterState(Context context) {
        sharedPreferences = context.getSharedPreferences(PUNTER_STATE, Context.MODE_PRIVATE);
    }

    public void setStateHome() {
        sharedPreferences.edit().putInt(CURRENT_STATE, HOME).apply();
    }

    public void setStateQuizz() {
        sharedPreferences.edit().putInt(CURRENT_STATE, QUIZZ).apply();
    }

    public void setStateEndGame() {
        sharedPreferences.edit().putInt(CURRENT_STATE, END_GAME).apply();
    }

    public int getCurrentState() {
        return sharedPreferences.getInt(CURRENT_STATE, HOME);
    }

    public int getCurrentApiGameOffset() {
        return sharedPreferences.getInt(CURRENT_API_GAME_PAGE, 0);
    }

    public void setCurrentApiGameOffset(int offset) {
        sharedPreferences.edit().putInt(CURRENT_API_GAME_PAGE, offset).apply();
    }

    public int getScore() {
        return sharedPreferences.getInt(SCORE, 0);
    }

    public void setScore(int score) {
        sharedPreferences.edit().putInt(SCORE, score).apply();
    }

    public void setHighScoreLocalBuffer(int score) {
        sharedPreferences.edit().putInt(HIGH_SCORE_BUFFER, score).apply();
    }

    public int getHighScoreLocalBuffer() {
        return sharedPreferences.getInt(HIGH_SCORE_BUFFER, -1);
    }
}
