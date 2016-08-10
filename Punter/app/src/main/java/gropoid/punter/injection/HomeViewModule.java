package gropoid.punter.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import gropoid.punter.domain.GameManager;
import gropoid.punter.interactor.HomeInteractor;
import gropoid.punter.interactor.impl.HomeInteractorImpl;
import gropoid.punter.presenter.HomePresenter;
import gropoid.punter.presenter.impl.HomePresenterImpl;
import gropoid.punter.presenter.loader.PresenterFactory;

@Module
public final class HomeViewModule {
    @Provides
    public HomeInteractor provideInteractor(Context context, GameManager gameManager) {
        return new HomeInteractorImpl(context, gameManager);
    }

    @Provides
    public PresenterFactory<HomePresenter> providePresenterFactory(@NonNull final HomeInteractor interactor) {
        return new PresenterFactory<HomePresenter>() {
            @NonNull
            @Override
            public HomePresenter create() {
                return new HomePresenterImpl(interactor);
            }
        };
    }
}
