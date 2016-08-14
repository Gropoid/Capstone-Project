package gropoid.punter.domain;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import gropoid.punter.data.Repository;


public class LocalHighScoreManager {
    public static final String NEW_HIGH_SCORE_ACTION = "gropoid.punter.LocalHighScoreManager.ACTION_NEW_HIGH_SCORE";

    @Inject
    Context context;
    @Inject
    Repository repository;

    @Inject
    public LocalHighScoreManager(Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
    }

    /**
     * @return true if a new local high score was saved
     */
    public boolean submitScore(int score) {
        // returns true is new high score
        if (repository.findLocalHighScore() < score) {
            repository.saveLocalHighScore(score);
            notifyWidgets();
            return true;
        }
        return false;
    }

    private void notifyWidgets() {
        Intent intent = new Intent(NEW_HIGH_SCORE_ACTION);
        context.sendBroadcast(intent);
    }

    public int getHighScore() {
        return repository.findLocalHighScore();
    }
}
