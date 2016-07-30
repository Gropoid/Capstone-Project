package gropoid.punter.retrofit;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

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
 * <p/>
 */
public class GameFetchIntentService extends IntentService {
    private static final String ACTION_FETCH_GAMES = "gropoid.punter.retrofit.action.ACTION_FETCH_GAMES";

    @Inject
    GiantBombApi giantBombApi;
    @Inject
    GameManager gameManager;

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
        intent.setAction(ACTION_FETCH_GAMES);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH_GAMES.equals(action)) {
                fetchGames();
            }
        }
    }


    private void fetchGames() {
        Call<Page<GameDTO>> call = giantBombApi.getGames();
        try {
            Response<Page<GameDTO>> response = call.execute();
            if (response.isSuccessful()) {
                Timber.v("fetched data from api :\n%s", response.body());
                Page<GameDTO> page = response.body();
                for (GameDTO gameDto : page.getResults()) {
                    fetchImageAndSave(gameDto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
