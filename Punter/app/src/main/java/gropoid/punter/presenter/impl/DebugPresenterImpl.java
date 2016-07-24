package gropoid.punter.presenter.impl;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import gropoid.punter.interactor.DebugInteractor;
import gropoid.punter.presenter.DebugPresenter;
import gropoid.punter.view.DebugView;

public final class DebugPresenterImpl extends BasePresenterImpl<DebugView> implements DebugPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final DebugInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public DebugPresenterImpl(@NonNull DebugInteractor interactor) {
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