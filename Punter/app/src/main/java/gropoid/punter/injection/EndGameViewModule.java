package gropoid.punter.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import gropoid.punter.data.PunterState;
import gropoid.punter.domain.LocalHighScoreManager;
import gropoid.punter.interactor.EndGameInteractor;
import gropoid.punter.interactor.impl.EndGameInteractorImpl;
import gropoid.punter.presenter.EndGamePresenter;
import gropoid.punter.presenter.impl.EndGamePresenterImpl;
import gropoid.punter.presenter.loader.PresenterFactory;

@Module
public final class EndGameViewModule {
    @Provides
    public EndGameInteractor provideInteractor(Context context, PunterState punterState, LocalHighScoreManager localHighScoreManager) {
        return new EndGameInteractorImpl(context, punterState, localHighScoreManager);
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
