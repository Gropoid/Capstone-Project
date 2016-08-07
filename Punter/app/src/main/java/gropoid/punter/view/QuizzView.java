package gropoid.punter.view;

import android.support.annotation.UiThread;

import gropoid.punter.domain.Question;

@UiThread
public interface QuizzView {

    void showQuestion(Question question);

    void showResult(boolean b);

    void showEndGame();
}