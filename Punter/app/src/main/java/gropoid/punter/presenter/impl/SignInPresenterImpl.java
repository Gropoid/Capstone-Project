package gropoid.punter.presenter.impl;

import android.support.annotation.NonNull;

import gropoid.punter.presenter.SignInPresenter;
import gropoid.punter.view.SignInView;
import gropoid.punter.interactor.SignInInteractor;

import javax.inject.Inject;

public final class SignInPresenterImpl extends BasePresenterImpl<SignInView> implements SignInPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final SignInInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public SignInPresenterImpl(@NonNull SignInInteractor interactor) {
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