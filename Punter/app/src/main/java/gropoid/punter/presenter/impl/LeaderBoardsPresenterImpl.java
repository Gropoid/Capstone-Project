package gropoid.punter.presenter.impl;

import android.support.annotation.NonNull;

import gropoid.punter.presenter.LeaderBoardsPresenter;
import gropoid.punter.view.LeaderBoardsView;
import gropoid.punter.interactor.LeaderBoardsInteractor;

import javax.inject.Inject;

public final class LeaderBoardsPresenterImpl extends BasePresenterImpl<LeaderBoardsView> implements LeaderBoardsPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final LeaderBoardsInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public LeaderBoardsPresenterImpl(@NonNull LeaderBoardsInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);

        // Your code here. Your view is available using mView and will not be null until next onStop()
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