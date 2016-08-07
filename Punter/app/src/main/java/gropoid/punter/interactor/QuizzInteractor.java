package gropoid.punter.interactor;

import gropoid.punter.domain.Question;

public interface QuizzInteractor extends BaseInteractor {

    Question getCurrentQuestion();

    int getCurrentScore();

    boolean submitAnswer(int answer);

    boolean nextQuestion();
}