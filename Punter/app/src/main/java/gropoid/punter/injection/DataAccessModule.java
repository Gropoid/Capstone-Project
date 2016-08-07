package gropoid.punter.injection;

import android.content.ContentResolver;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import gropoid.punter.data.PunterDbHelper;
import gropoid.punter.data.PunterState;

@Module
public class DataAccessModule {

    Context context;
    public DataAccessModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext() {
        return context;
    }

    @Provides
    PunterDbHelper providePunterDbHelper(Context context) {
        return new PunterDbHelper(context);
    }

    @Provides
    ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Provides
    PunterState providePunterState(Context context) {
        return new PunterState(context);
    }
}
