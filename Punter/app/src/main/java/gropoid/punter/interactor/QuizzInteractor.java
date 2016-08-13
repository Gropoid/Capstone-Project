package gropoid.punter.interactor;

import gropoid.punter.domain.Question;
import gropoid.punter.presenter.impl.LoadingCallback;

public interface QuizzInteractor extends BaseInteractor {

    Question getCurrentQuestion();

    int getCurrentScore();

    boolean submitAnswer(int answer);

    boolean nextQuestion();

    void finishQuizz();

    void prepareQuizz(LoadingCallback callback);

    void unregisterReceiver();
}