package gropoid.punter.interactor.impl;

import android.content.Context;

import com.google.android.gms.games.Games;

import javax.inject.Inject;

import gropoid.punter.R;
import gropoid.punter.data.PunterState;
import gropoid.punter.interactor.MainInteractor;
import gropoid.punter.view.GoogleApiStateListener;
import gropoid.punter.view.PlayGamesHelper;
import gropoid.punter.view.impl.MainActivity;

public final class MainInteractorImpl implements MainInteractor, GoogleApiStateListener {

    @Inject
    Context context;
    @Inject
    PunterState punterState;
    @Inject
    PlayGamesHelper playGamesHelper;

    @Inject
    public MainInteractorImpl(Context context, PunterState punterState, PlayGamesHelper playGamesHelper) {
        this.context = context;
        this.punterState = punterState;
        this.playGamesHelper = playGamesHelper;
        playGamesHelper.registerListener(this);
    }

    @Override
    public int getCurrentState() {
        return punterState.getCurrentState();
    }

    @Override
    public void setCurrentStateHome() {
        punterState.setStateHome();
    }

    @Override
    public void setCurrentStateQuizz() {
        punterState.setStateQuizz();
    }

    @Override
    public void setCurrentStateEndGame() {
        punterState.setStateEndGame();
    }

    @Override
    public void setCurrentStateLeaderBoards() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void connectPlayGamesApi() {
        playGamesHelper.signIn();
    }

    @Override
    public void registerGoogleApiListener(GoogleApiStateListener listener) {
        playGamesHelper.registerListener(listener);
    }

    @Override
    public void unregisterGoogleApiListener(GoogleApiStateListener listener) {
        playGamesHelper.unregisterListener(listener);
    }

    @Override
    public void notifyGoogleApiFailure() {
        playGamesHelper.onDisconnected();
    }

    @Override
    public void setGoogleApiContext(MainActivity mainActivity) {
        playGamesHelper.setActivity(mainActivity);
    }

    @Override
    public boolean isGooglePlayClientConnected() {
        return playGamesHelper.isConnected();
    }

    @Override
    public PlayGamesHelper getPlayGamesHelper() {
        return playGamesHelper;
    }

    @Override
    public void onConnected() {
        // submit score that were achieved before singing in, if one is pending
        int bufferedScore = punterState.getHighScoreLocalBuffer();
        if (bufferedScore != -1) {
            Games.Leaderboards.submitScore(playGamesHelper.getGoogleApiClient(), context.getString(R.string.leaderboard_high_score), bufferedScore);
            punterState.setHighScoreLocalBuffer(-1);
        }
    }

    @Override
    public void onDisconnected() {
        // nothing
    }
}