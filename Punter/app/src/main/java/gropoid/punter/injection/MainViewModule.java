package gropoid.punter.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import gropoid.punter.data.PunterState;
import gropoid.punter.interactor.MainInteractor;
import gropoid.punter.interactor.impl.MainInteractorImpl;
import gropoid.punter.presenter.MainPresenter;
import gropoid.punter.presenter.impl.MainPresenterImpl;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.view.PlayGamesHelper;

@Module
public final class MainViewModule {
    @Provides
    public MainInteractor provideInteractor(Context context,  PunterState punterState, PlayGamesHelper playGamesHelper) {
        return new MainInteractorImpl(context, punterState, playGamesHelper);
    }

    @Provides
    public PresenterFactory<MainPresenter> providePresenterFactory(@NonNull final MainInteractor interactor) {
        return new PresenterFactory<MainPresenter>() {
            @NonNull
            @Override
            public MainPresenter create() {
                return new MainPresenterImpl(interactor);
            }
        };
    }

}
