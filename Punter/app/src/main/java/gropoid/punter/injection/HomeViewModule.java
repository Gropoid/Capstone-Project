package gropoid.punter.injection;

import android.support.annotation.NonNull;

import gropoid.punter.interactor.HomeInteractor;
import gropoid.punter.interactor.impl.HomeInteractorImpl;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.presenter.HomePresenter;
import gropoid.punter.presenter.impl.HomePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class HomeViewModule {
    @Provides
    public HomeInteractor provideInteractor() {
        return new HomeInteractorImpl();
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
