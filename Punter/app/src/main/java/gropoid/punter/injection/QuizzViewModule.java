package gropoid.punter.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import gropoid.punter.data.PunterState;
import gropoid.punter.data.Repository;
import gropoid.punter.domain.GameManager;
import gropoid.punter.domain.QuestionManager;
import gropoid.punter.interactor.QuizzInteractor;
import gropoid.punter.interactor.impl.QuizzInteractorImpl;
import gropoid.punter.presenter.QuizzPresenter;
import gropoid.punter.presenter.impl.QuizzPresenterImpl;
import gropoid.punter.presenter.loader.PresenterFactory;

@Module
public final class QuizzViewModule {
    @Provides
    public QuizzInteractor provideInteractor(Context context, QuestionManager questionManager, GameManager gameManager, PunterState punterState) {
        return new QuizzInteractorImpl(context, questionManager, gameManager, punterState);
    }

    @Provides
    public PresenterFactory<QuizzPresenter> providePresenterFactory(@NonNull final QuizzInteractor interactor) {
        return new PresenterFactory<QuizzPresenter>() {
            @NonNull
            @Override
            public QuizzPresenter create() {
                return new QuizzPresenterImpl(interactor);
            }
        };
    }

    @Provides
    public QuestionManager provideQuestionManager(GameManager gameManager, Repository repository) {
        return new QuestionManager(gameManager, repository);
    }
}
