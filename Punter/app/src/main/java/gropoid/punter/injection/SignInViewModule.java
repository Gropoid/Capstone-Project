package gropoid.punter.injection;

import android.support.annotation.NonNull;

import gropoid.punter.interactor.SignInInteractor;
import gropoid.punter.interactor.impl.SignInInteractorImpl;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.presenter.SignInPresenter;
import gropoid.punter.presenter.impl.SignInPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class SignInViewModule {
    @Provides
    public SignInInteractor provideInteractor() {
        return new SignInInteractorImpl();
    }

    @Provides
    public PresenterFactory<SignInPresenter> providePresenterFactory(@NonNull final SignInInteractor interactor) {
        return new PresenterFactory<SignInPresenter>() {
            @NonNull
            @Override
            public SignInPresenter create() {
                return new SignInPresenterImpl(interactor);
            }
        };
    }
}
