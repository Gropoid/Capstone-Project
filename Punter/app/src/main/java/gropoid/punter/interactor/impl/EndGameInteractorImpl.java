package gropoid.punter.interactor.impl;

import android.content.Context;

import com.google.android.gms.games.Games;

import javax.inject.Inject;

import gropoid.punter.R;
import gropoid.punter.data.PunterState;
import gropoid.punter.domain.LocalHighScoreManager;
import gropoid.punter.interactor.EndGameInteractor;
import gropoid.punter.view.PlayGamesHelper;

public final class EndGameInteractorImpl implements EndGameInteractor {

    @Inject
    Context context;
    @Inject
    PunterState punterState;
    @Inject
    LocalHighScoreManager localHighScoreManager;

    PlayGamesHelper playGamesHelper;

    @Inject
    public EndGameInteractorImpl(Context context, PunterState punterState, LocalHighScoreManager localHighScoreManager) {
        this.context = context;
        this.punterState = punterState;
        this.localHighScoreManager = localHighScoreManager;
    }


    @Override
    public int getGameScore() {
        return punterState.getScore();
    }

    @Override
    public void submitScore() {
        int score = punterState.getScore();
        if (localHighScoreManager.submitScore(score)) {
            if (playGamesHelper.isConnected()) {
                Games.Leaderboards.submitScore(playGamesHelper.getGoogleApiClient(), context.getString(R.string.leaderboard_high_score), punterState.getScore());
            } else {
                punterState.setHighScoreLocalBuffer(punterState.getScore());
            }
        }
    }

    @Override
    public void setPlayGamesHelper(PlayGamesHelper playGamesHelper) {
        this.playGamesHelper = playGamesHelper;
    }

    @Override
    public boolean isPlayApiConnected() {
        return playGamesHelper.isConnected();
    }

}