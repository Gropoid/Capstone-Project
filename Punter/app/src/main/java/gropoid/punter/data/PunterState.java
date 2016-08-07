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
        return sharedPreferences.getInt(CURRENT_STATE, QUIZZ);
    }
}
