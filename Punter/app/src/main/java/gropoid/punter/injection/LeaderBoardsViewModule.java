package gropoid.punter.injection;

import android.support.annotation.NonNull;

import gropoid.punter.interactor.LeaderBoardsInteractor;
import gropoid.punter.interactor.impl.LeaderBoardsInteractorImpl;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.presenter.LeaderBoardsPresenter;
import gropoid.punter.presenter.impl.LeaderBoardsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class LeaderBoardsViewModule {
    @Provides
    public LeaderBoardsInteractor provideInteractor() {
        return new LeaderBoardsInteractorImpl();
    }

    @Provides
    public PresenterFactory<LeaderBoardsPresenter> providePresenterFactory(@NonNull final LeaderBoardsInteractor interactor) {
        return new PresenterFactory<LeaderBoardsPresenter>() {
            @NonNull
            @Override
            public LeaderBoardsPresenter create() {
                return new LeaderBoardsPresenterImpl(interactor);
            }
        };
    }
}
