package gropoid.punter.interactor.impl;

import javax.inject.Inject;

import gropoid.punter.data.PunterState;
import gropoid.punter.interactor.MainInteractor;
import gropoid.punter.view.GoogleApiStateListener;
import gropoid.punter.view.PlayGamesHelper;
import gropoid.punter.view.impl.MainActivity;

public final class MainInteractorImpl implements MainInteractor {

    @Inject
    PunterState punterState;
    @Inject
    PlayGamesHelper playGamesHelper;

    @Inject
    public MainInteractorImpl(PunterState punterState, PlayGamesHelper playGamesHelper) {
        this.punterState = punterState;
        this.playGamesHelper = playGamesHelper;
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
        playGamesHelper.notifyFailure();
    }

    @Override
    public void setGoogleApiContext(MainActivity mainActivity) {
        playGamesHelper.setActivity(mainActivity);
    }

    @Override
    public boolean isGooglePlayClientConnected() {
        return playGamesHelper.isSignedIn();
    }
}