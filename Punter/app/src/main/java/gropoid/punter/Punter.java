package gropoid.punter;

import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;

import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.AppModule;
import gropoid.punter.injection.DaggerAppComponent;
import timber.log.Timber;

public final class Punter extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.v("Timber is initialized");
            Stetho.initializeWithDefaults(this);
        }

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @NonNull
    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}