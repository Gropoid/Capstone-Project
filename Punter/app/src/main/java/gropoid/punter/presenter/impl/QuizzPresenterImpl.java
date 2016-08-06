package gropoid.punter.presenter.impl;

import android.support.annotation.NonNull;

import gropoid.punter.presenter.QuizzPresenter;
import gropoid.punter.view.QuizzView;
import gropoid.punter.interactor.QuizzInteractor;

import javax.inject.Inject;

public final class QuizzPresenterImpl extends BasePresenterImpl<QuizzView> implements QuizzPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final QuizzInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public QuizzPresenterImpl(@NonNull QuizzInteractor interactor) {
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