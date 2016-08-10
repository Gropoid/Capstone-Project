package gropoid.punter.interactor.impl;

import android.content.Context;

import javax.inject.Inject;

import gropoid.punter.domain.GameManager;
import gropoid.punter.interactor.HomeInteractor;
import gropoid.punter.retrofit.GameFetchIntentService;

public final class HomeInteractorImpl implements HomeInteractor {

    @Inject
    GameManager gameManager;
    @Inject
    Context context;

    public HomeInteractorImpl(Context context, GameManager gameManager) {
        this.context = context;
        this.gameManager = gameManager;
    }

    @Override
    public void syncIfneeded() {
        if (gameManager.isGameDbStarved()) {
            GameFetchIntentService.startFetchGames(context);
        }
    }
}