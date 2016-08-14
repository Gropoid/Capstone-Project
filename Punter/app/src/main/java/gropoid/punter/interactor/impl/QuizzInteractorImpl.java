package gropoid.punter.interactor.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.List;

import javax.inject.Inject;

import gropoid.punter.data.PunterState;
import gropoid.punter.domain.GameManager;
import gropoid.punter.domain.Question;
import gropoid.punter.domain.QuestionManager;
import gropoid.punter.interactor.QuizzInteractor;
import gropoid.punter.presenter.impl.LoadingCallback;
import gropoid.punter.retrofit.GameFetchIntentService;
import timber.log.Timber;

public final class QuizzInteractorImpl implements QuizzInteractor {
    public static final int QUESTIONS_COUNT = 10;
    @Inject
    Context context;

    @Inject
    QuestionManager questionManager;

    @Inject
    GameManager gameManager;

    @Inject
    PunterState punterState;

    List<Question> quizz;

    int currentQuestion = -1;
    private int score = 0;
    private GameDbStateReceiver gameDbStateReceiver;


    @Inject
    public QuizzInteractorImpl(Context context, QuestionManager questionManager, GameManager gameManager, PunterState punterState) {
        this.context = context;
        this.questionManager = questionManager;
        this.gameManager = gameManager;
        this.punterState = punterState;
    }


    @Override
    public Question getCurrentQuestion() {
        if (currentQuestion == -1 || quizz == null || quizz.size() == 0) {
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

    @Override
    public void prepareQuizz(LoadingCallback callback) {
        Timber.v("Preparing  quizz");
        quizz = questionManager.getQuestions(QUESTIONS_COUNT);
        if (quizz != null && quizz.size() == QUESTIONS_COUNT) {
            if (currentQuestion == -1) {
                currentQuestion = 0;
                callback.onLoadingProgress(100);
            } else {
                // we are resuming an ongoing quizz, do nothing
                callback.onLoadingProgress(100);
            }
        } else {
            if (!gameManager.isGameDbStarved()) {
                questionManager.generateQuestions(QuestionManager.DEFAULT_QUESTION_POOL_SIZE);
                prepareQuizz(callback);
            } else {
                gameDbStateReceiver = new GameDbStateReceiver(callback);
                gameDbStateReceiver.register();
                GameFetchIntentService.startFetchGames(context);
            }
        }
    }

    @Override
    public void unregisterReceiver() {
        if (gameDbStateReceiver != null) {
            try {
                context.unregisterReceiver(gameDbStateReceiver);
            } catch (IllegalArgumentException e) {
                Timber.i("Receiver was already unregistered");
            } finally {
                gameDbStateReceiver = null;
            }
        }
    }

    private class GameDbStateReceiver extends BroadcastReceiver {

        LoadingCallback callback;

        public GameDbStateReceiver(LoadingCallback callback) {
            this.callback = callback;
        }

        public void register() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GameFetchIntentService.FETCHING_PROGRESS);
            filter.addAction(GameFetchIntentService.FETCHING_FAILURE);
            context.registerReceiver(this, filter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case GameFetchIntentService.FETCHING_PROGRESS:
                    int progress = intent.getIntExtra(GameFetchIntentService.PROGRESS_VALUE, 0);
                    if (progress >= 100) {
                        prepareQuizz(callback);
                        context.unregisterReceiver(this);
                    } else {
                        callback.onLoadingProgress(progress);
                    }
                    break;
                case GameFetchIntentService.FETCHING_FAILURE:
                    callback.onLoadingFailure();
                    context.unregisterReceiver(this);
                    break;
                default:
            }
        }
    }
}