package gropoid.punter.injection;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import dagger.Module;
import dagger.Provides;
import gropoid.punter.data.PunterState;
import gropoid.punter.domain.PlayGamesHelperImpl;
import gropoid.punter.view.PlayGamesHelper;

@Module
public class PlayGamesApiModule {
    @Provides
    GoogleApiClient provideGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    @Provides
    PlayGamesHelper providePlayGamesHelper(GoogleApiClient googleApiClient, PunterState punterState) {
        return new PlayGamesHelperImpl(googleApiClient, punterState);
    }
}
