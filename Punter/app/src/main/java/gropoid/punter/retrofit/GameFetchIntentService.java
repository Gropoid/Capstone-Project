package gropoid.punter.retrofit;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import javax.inject.Inject;

import gropoid.punter.domain.GameManager;
import gropoid.punter.injection.DaggerBackgroundComponent;
import gropoid.punter.injection.DataAccessModule;
import gropoid.punter.injection.NetworkModule;
import gropoid.punter.retrofit.dto.GameDTO;
import gropoid.punter.retrofit.dto.Page;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class GameFetchIntentService extends IntentService {
    private static final String ACTION_FETCH_GAMES = "gropoid.punter.retrofit.action.ACTION_FETCH_GAMES";

    public static final String FETCHING_PROGRESS = "gropoid.punter.retrofit.action.FETCHING_PROGRESS";
    public static final String FETCHING_FAILURE = "gropoid.punter.retrofit.action.FETCHING_FAILURE";
    public static final String PROGRESS_VALUE = "ProgressValue";

    @Inject
    GiantBombApi giantBombApi;
    @Inject
    GameManager gameManager;
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    PackageManager packageManager;

    private boolean notify;

    public GameFetchIntentService() {
        super("GameFetchIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerBackgroundComponent.builder()
                .networkModule(new NetworkModule())
                .dataAccessModule(new DataAccessModule(getApplicationContext()))
                .build()
                .inject(this);
    }

    /**
     * If the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startFetchGames(Context context) {
        Intent intent = new Intent(context, GameFetchIntentService.class);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        notify = true;
        if(isConnectionAvailable()) {
            fetchGames();
        } else {
            broadcast(FETCHING_FAILURE);
            toggleConnectivityReceiver(true);
        }
    }

    private void toggleConnectivityReceiver(boolean enable) {
        Timber.v("toggleConnectivityReceiver(%s)", enable);
        ComponentName componentName =
                new ComponentName(getApplicationContext(),
                        ConnectivityReceiver.class);
        packageManager.setComponentEnabledSetting(componentName,
                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    private boolean isConnectionAvailable() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void fetchGames() {
        Call<Page<GameDTO>> call = giantBombApi.getGames(gameManager.getCurrentApiGameOffset());
        try {
            Response<Page<GameDTO>> response = call.execute();
            if (response.isSuccessful()) {
                Timber.v("fetched data from api :\n%s", response.body());
                Page<GameDTO> page = response.body();
                gameManager.setCurrentApiGameOffset(page.getOffset() + page.getNumber_of_page_results());
                broadcast(FETCHING_PROGRESS);
                for (GameDTO gameDto : page.getResults()) {
                    fetchImageAndSave(gameDto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gameManager.isGameDbStarved()) {
            // we tried to download 100 games and failed
            broadcast(FETCHING_FAILURE);
        }
    }

    private void fetchImageAndSave(GameDTO gameDto) {
        if (gameDto.getImage() == null) {
            return;
        }
        if (gameDto.getImage().getMediumUrl().startsWith("https://")) {
            gameDto.getImage().setMediumUrl(gameDto.getImage().getMediumUrl().replace("https://", "http://"));
        }
        Call<ResponseBody> call = giantBombApi.getImage(gameDto.getImage().getMediumUrl());
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                Timber.v("Successfully retrieved image for game %s", gameDto.getName());
                gameManager.save(gameDto.toGame(), gameDto.getImage().getMediumUrl(), response.body().bytes());
                broadcast(FETCHING_PROGRESS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String gameDbState) {
        if (notify) {
            Intent intent = new Intent();
            intent.setAction(gameDbState);

            if (FETCHING_PROGRESS.equals(gameDbState)) {
                int progress = gameManager.getLoadingProgress() + 10; // +10 for the initial JSON fetching
                intent.putExtra(PROGRESS_VALUE, progress);
                if (progress >= 100) {
                    notify = false;
                }
            }
            getApplicationContext().sendBroadcast(intent);
        }
    }
}
