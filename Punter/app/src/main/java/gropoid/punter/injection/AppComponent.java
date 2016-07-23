package gropoid.punter.injection;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import gropoid.punter.Punter;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Context getAppContext();

    Punter getApp();
}