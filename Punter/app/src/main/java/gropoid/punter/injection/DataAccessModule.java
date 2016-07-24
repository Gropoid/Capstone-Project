package gropoid.punter.injection;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import gropoid.punter.data.PunterDbHelper;
import gropoid.punter.domain.GameManager;

@Module
public class DataAccessModule {
    @Provides
    PunterDbHelper providePunterDbHelper(Context context) {
        return new PunterDbHelper(context);
    }

    @Provides
    GameManager provideGameManager(Context context) {
        return new GameManager(context);
    }
}
