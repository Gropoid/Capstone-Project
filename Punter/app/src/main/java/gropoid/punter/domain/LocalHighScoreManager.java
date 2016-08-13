package gropoid.punter.domain;

import javax.inject.Inject;

import gropoid.punter.data.Repository;


public class LocalHighScoreManager {

    @Inject
    Repository repository;

    @Inject
    public LocalHighScoreManager(Repository repository) {
        this.repository = repository;
    }

    /**
     * @return true if a new local high score was saved
     */
    public boolean submitScore(int score) {
        // returns true is new high score
        if (repository.findLocalHighScore() < score) {
            repository.saveLocalHighScore(score);
            return true;
        }
        return false;
    }
}
