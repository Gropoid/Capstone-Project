package gropoid.punter.interactor.impl;

import javax.inject.Inject;

import gropoid.punter.data.PunterState;
import gropoid.punter.interactor.MainInteractor;

public final class MainInteractorImpl implements MainInteractor {

    @Inject
    PunterState punterState;

    @Inject
    public MainInteractorImpl(PunterState punterState) {
        this.punterState = punterState;
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

}