package gropoid.punter.interactor.impl;

import javax.inject.Inject;

import gropoid.punter.data.PunterState;
import gropoid.punter.interactor.EndGameInteractor;

public final class EndGameInteractorImpl implements EndGameInteractor {

    @Inject
    PunterState punterState;

    @Inject
    public EndGameInteractorImpl(PunterState punterState) {
        this.punterState = punterState;
    }


    @Override
    public int getGameScore() {
        return punterState.getScore();
    }

}