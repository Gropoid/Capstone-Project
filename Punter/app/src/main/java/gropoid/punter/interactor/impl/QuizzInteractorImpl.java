package gropoid.punter.interactor.impl;

import java.util.List;

import javax.inject.Inject;

import gropoid.punter.domain.Question;
import gropoid.punter.domain.QuestionManager;
import gropoid.punter.interactor.QuizzInteractor;

public final class QuizzInteractorImpl implements QuizzInteractor {
    @Inject
    QuestionManager questionManager;

    List<Question> quizz;

    int currentQuestion = -1;

    @Inject
    public QuizzInteractorImpl(QuestionManager questionManager) {
        this.questionManager = questionManager;
    }

    @Override
    public Question getCurrentQuestion() {
        if (currentQuestion == -1) {
            quizz = questionManager.getQuestions(10);
            currentQuestion = 0;
        }
        return quizz.get(currentQuestion);
    }
}