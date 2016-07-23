package gropoid.punter.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import gropoid.punter.Punter;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
    @NonNull
    private final Punter mApp;

    public AppModule(@NonNull Punter app) {
        mApp = app;
    }

    @Provides
    public Context provideAppContext() {
        return mApp;
    }

    @Provides
    public Punter provideApp() {
        return mApp;
    }
}
