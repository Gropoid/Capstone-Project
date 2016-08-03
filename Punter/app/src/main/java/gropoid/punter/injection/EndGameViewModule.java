package gropoid.punter.injection;

import android.support.annotation.NonNull;

import gropoid.punter.interactor.EndGameInteractor;
import gropoid.punter.interactor.impl.EndGameInteractorImpl;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.presenter.EndGamePresenter;
import gropoid.punter.presenter.impl.EndGamePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class EndGameViewModule {
    @Provides
    public EndGameInteractor provideInteractor() {
        return new EndGameInteractorImpl();
    }

    @Provides
    public PresenterFactory<EndGamePresenter> providePresenterFactory(@NonNull final EndGameInteractor interactor) {
        return new PresenterFactory<EndGamePresenter>() {
            @NonNull
            @Override
            public EndGamePresenter create() {
                return new EndGamePresenterImpl(interactor);
            }
        };
    }
}
