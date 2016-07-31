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

import gropoid.punter.data.Repository;
import timber.log.Timber;

public class GameManager {
    public static final String IMAGE_FOLDER = "images/";
    public static final String IMAGE_FILE = "image";

    @Inject
    Context context;
    @Inject
    Repository repository;

    @Inject
    public GameManager(Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
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
}
