package gropoid.punter.interactor.impl;

import java.util.List;

import javax.inject.Inject;

import gropoid.punter.data.PunterState;
import gropoid.punter.domain.Question;
import gropoid.punter.domain.QuestionManager;
import gropoid.punter.interactor.QuizzInteractor;

public final class QuizzInteractorImpl implements QuizzInteractor {
    public static final int QUESTIONS_COUNT = 10;
    @Inject
    QuestionManager questionManager;

    @Inject
    PunterState punterState;

    List<Question> quizz;

    int currentQuestion = -1;
    private int score = 0;

    @Inject
    public QuizzInteractorImpl(QuestionManager questionManager, PunterState punterState) {
        this.questionManager = questionManager;
        this.punterState = punterState;
    }

    @Override
    public Question getCurrentQuestion() {
        if (currentQuestion == -1) {
            quizz = questionManager.getQuestions(QUESTIONS_COUNT);
            currentQuestion = 0;
        }
        if (quizz == null || quizz.size() == 0) {
            return null;
        } else {
            return quizz.get(currentQuestion);
        }
    }

    @Override
    public int getCurrentScore() {
        return score;
    }

    @Override
    public boolean submitAnswer(int answer) {
        Question question = quizz.get(currentQuestion);
        boolean answerIsCorrect = question.getCorrectAnswer().getId() == question.getGames()[answer].getId();
        if (answerIsCorrect) {
            score += 10;
        }
        return answerIsCorrect;
    }

    @Override
    public boolean nextQuestion() {
        questionManager.expireQuestion(quizz.get(currentQuestion));
        currentQuestion++;
        return currentQuestion < QUESTIONS_COUNT;
    }

    @Override
    public void finishQuizz() {
        punterState.setScore(score);
    }
}