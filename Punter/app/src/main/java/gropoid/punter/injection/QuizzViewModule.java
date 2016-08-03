package gropoid.punter.injection;

import android.support.annotation.NonNull;

import gropoid.punter.interactor.QuizzInteractor;
import gropoid.punter.interactor.impl.QuizzInteractorImpl;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.presenter.QuizzPresenter;
import gropoid.punter.presenter.impl.QuizzPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class QuizzViewModule {
    @Provides
    public QuizzInteractor provideInteractor() {
        return new QuizzInteractorImpl();
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
}
