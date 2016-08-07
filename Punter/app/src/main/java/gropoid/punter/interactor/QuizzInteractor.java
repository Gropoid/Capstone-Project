package gropoid.punter.interactor;

import gropoid.punter.domain.Question;

public interface QuizzInteractor extends BaseInteractor {

    Question getCurrentQuestion();
}