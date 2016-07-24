package gropoid.punter.injection;

import android.support.annotation.NonNull;

import gropoid.punter.interactor.DebugInteractor;
import gropoid.punter.interactor.impl.DebugInteractorImpl;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.presenter.DebugPresenter;
import gropoid.punter.presenter.impl.DebugPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class DebugViewModule {
    @Provides
    public DebugInteractor provideInteractor() {
        return new DebugInteractorImpl();
    }

    @Provides
    public PresenterFactory<DebugPresenter> providePresenterFactory(@NonNull final DebugInteractor interactor) {
        return new PresenterFactory<DebugPresenter>() {
            @NonNull
            @Override
            public DebugPresenter create() {
                return new DebugPresenterImpl(interactor);
            }
        };
    }
}
