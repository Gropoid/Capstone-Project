package gropoid.punter.view;

import android.support.annotation.UiThread;

@UiThread
public interface EndGameView {

    void displayScore(int gameScore);
}