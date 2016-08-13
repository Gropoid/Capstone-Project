package gropoid.punter.presenter.impl;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import gropoid.punter.interactor.EndGameInteractor;
import gropoid.punter.presenter.EndGamePresenter;
import gropoid.punter.view.EndGameView;

public final class EndGamePresenterImpl extends BasePresenterImpl<EndGameView> implements EndGamePresenter {
    /**
     * The interactor
     */
    @NonNull
    private final EndGameInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public EndGamePresenterImpl(@NonNull EndGameInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);

        // Your code here. Your view is available using mView and will not be null until next onStop()
        assert mView != null;
        mView.displayScore(mInteractor.getGameScore());
        mView.displayLeaderboardsButtonIfConnected();
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()

        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */

        super.onPresenterDestroyed();
    }
}