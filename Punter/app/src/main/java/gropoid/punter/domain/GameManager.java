package gropoid.punter.domain;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.inject.Inject;

import gropoid.punter.data.PunterState;
import gropoid.punter.data.Repository;
import gropoid.punter.retrofit.GameFetchIntentService;
import timber.log.Timber;

public class GameManager {
    public static final String IMAGE_FOLDER = "images/";
    public static final String IMAGE_FILE = "image";
    private static final int MAX_GAME_USES = 10;
    private static final int LOW_GAMES_THRESHOLD = 20;

    @Inject
    Context context;
    @Inject
    PunterState punterState;
    @Inject
    Repository repository;

    @Inject
    public GameManager(Context context, Repository repository, PunterState punterState) {
        this.context = context;
        this.repository = repository;
        this.punterState = punterState;
    }

    @WorkerThread
    public Game save(Game game, String imageUrl, byte[] image) {
        // overwrite path if exists
        Timber.v("save game image [%s]", game.getName());
        File folder = new File(getImageFolderPath());
        //noinspection ResultOfMethodCallIgnored
        folder.mkdirs();
        File imageFile = new File(getImageFilePath(game.getId(), getImageExtension(imageUrl)));
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            fos.write(image);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        game.setImageFile(imageFile.getPath());
        repository.save(game);
        return game;
    }

    @VisibleForTesting
    String getImageExtension(String imageUrl) {
        String[] subStrings = imageUrl.split("\\.");
        return "." + subStrings[subStrings.length - 1];
    }

    private String getImageFolderPath() {
        return context.getFilesDir().getPath() + "/" + IMAGE_FOLDER;
    }

    private String getImageFilePath(long id, String extension) {
        return getImageFolderPath() + id + extension;
    }

    public List<Game> getAllGames() {
        List<Game> games = repository.findAllGames();
        Hashtable<Long, Platform> platformPool = new Hashtable<>();
        for (Platform platform : getAllPlatforms()) {
            platformPool.put(platform.getId(), platform);
        }
        for (Game game : games) {
            List<Long> platformIds = findPlatformIdsForGame(game);
            List<Platform> platforms = new ArrayList<>();
            for (Long id : platformIds) {
                platforms.add(platformPool.get(id));
            }
            game.setPlatforms(platforms);
        }
        return games;
    }

    public List<Platform> getAllPlatforms() {
        return repository.findAllPlatforms();
    }

    private List<Long> findPlatformIdsForGame(Game game) {
        return repository.findPlatformIdsForGameId(game.getId());
    }

    public void addPlannedUseToGame(Game game) {
        Timber.v("addPlannedUseToGame %s", game.getId());
        game.setPlannedUses(game.getPlannedUses() + 1);
        repository.save(game);
    }

    /**
     * Use this method after a game has been actually used is a quizz to delete it from database if required.
     *
     * @param game the game that was just used in a question.
     */
    public void checkGameUsage(Game game) {
        game.setActualUses(game.getActualUses() + 1);
        Timber.d("Deleting game ? id %s pending uses [%s], actual uses [%s], nb_platforms %s", game.getId(), game.getPlannedUses(), game.getActualUses(), game.getPlatforms().size());
        if (game.getActualUses() >= MAX_GAME_USES) {
            Timber.d("Booya, deleting %s", game.getId());
            repository.delete(game);
            fetchMoreGamesIfNeeded();
        } else {
            repository.save(game);
        }
    }

    /**
     * Use this method to check if you can use this game in a new question, or if it has been
     * scheduled in too many questions already.
     *
     * @param game the game whose planned usage you want to check
     * @return true if the game has been used more than MAX_GAME_USE times in a question.
     */
    public boolean wasGamePlannedEnough(Game game) {
        return game.getPlannedUses() >= MAX_GAME_USES;
    }

    private void fetchMoreGamesIfNeeded() {
        if (isGameDbStarved()) {
            Timber.i("Game number went below threshold(%s), fetching new ones");
            GameFetchIntentService.startFetchGames(context);
        }
    }

    public int getCurrentApiGameOffset() {
        return punterState.getCurrentApiGameOffset();
    }

    public void setCurrentApiGameOffset(int offset) {
        Timber.i("New api game offset : %s", offset);
        punterState.setCurrentApiGameOffset(offset);
    }

    public boolean isGameDbStarved() {
        return repository.getGamesCount() < LOW_GAMES_THRESHOLD;
    }

    public int getLoadingProgress() {
        int progress = repository.getGamesCount() * 90 / LOW_GAMES_THRESHOLD;
        Timber.v("Loading progress : %s", progress);
        return progress;
    }
}
