package gropoid.punter.domain;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import gropoid.punter.retrofit.dto.GameDTO;
import timber.log.Timber;

public class GameManager {
    public static final String IMAGE_FOLDER = "images/";
    public static final String IMAGE_FILE = "image";
    private Context context;

    public GameManager(Context context) {
        this.context = context;
    }

    @WorkerThread
    public void save(GameDTO game, byte[] image) {
        // overwrite path if exists
        Timber.v("save game image [%s]", game.getName());
        Log.v("GameManager", "allo quoi");
        File folder = new File(getImageFolderPath());
        //noinspection ResultOfMethodCallIgnored
        folder.mkdirs();
        File imageFile = new File(getImageFilePath(game.getId(), getImageExtension(game.getImage().getMediumUrl())));
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
}
