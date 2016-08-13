package gropoid.punter.presenter.impl;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import gropoid.punter.interactor.HomeInteractor;
import gropoid.punter.presenter.HomePresenter;
import gropoid.punter.view.HomeView;
import gropoid.punter.view.PlayGamesHelper;

public final class HomePresenterImpl extends BasePresenterImpl<HomeView> implements HomePresenter {
    /**
     * The interactor
     */
    @NonNull
    private final HomeInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public HomePresenterImpl(@NonNull HomeInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);

        // Your code here. Your view is available using mView and will not be null until next onStop()
        assert mView != null;
        mInteractor.syncIfneeded();
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

    @Override
    public void setPlayGamesHelper(PlayGamesHelper playGamesHelper) {
        mInteractor.setPlayGamesHelper(playGamesHelper);
    }

    @Override
    public void checkGoogleApiConnected() {
        if (mView != null) {
            mView.toggleGooglePlayPanel(!mInteractor.isGooglePlayApiConnected());
            mView.toggleLeaderboardsButton(mInteractor.isGooglePlayApiConnected());
        }
    }
}