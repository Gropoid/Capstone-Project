package gropoid.punter.presenter.impl;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import gropoid.punter.data.PunterState;
import gropoid.punter.interactor.MainInteractor;
import gropoid.punter.presenter.MainPresenter;
import gropoid.punter.view.GoogleApiStateListener;
import gropoid.punter.view.MainView;
import gropoid.punter.view.impl.MainActivity;
import timber.log.Timber;

public final class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final MainInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public MainPresenterImpl(@NonNull MainInteractor interactor) {
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

    @Override
    public void loadCurrentState() {
        if (mView != null) {
            switch (mInteractor.getCurrentState()) {
                case PunterState.HOME:
                    mView.showHome();
                    break;
                case PunterState.QUIZZ:
                    mView.startQuizz();
                    break;
                case PunterState.END_GAME:
                    mView.showEndGame();
                    break;
            }
        } else {
            Timber.w("mView was null");
        }
    }

    @Override
    public void setCurrentStateHome() {
        mInteractor.setCurrentStateHome();
    }

    @Override
    public void setCurrentStateQuizz() {
        mInteractor.setCurrentStateQuizz();
    }

    @Override
    public void setCurrentStateEndGame() {
        mInteractor.setCurrentStateEndGame();
    }

    @Override
    public void connectPlayGamesApi() {
        mInteractor.connectPlayGamesApi();
    }

    @Override
    public void registerGoogleApiListener(GoogleApiStateListener listener) {
        mInteractor.registerGoogleApiListener(listener);
    }

    @Override
    public void unregisterGoogleApiListener(GoogleApiStateListener listener) {
        mInteractor.unregisterGoogleApiListener(listener);
    }

    @Override
    public void notifyGoogleApiFailure() {
        mInteractor.notifyGoogleApiFailure();
    }

    @Override
    public void setGoogleApiContext(MainActivity mainActivity) {
        mInteractor.setGoogleApiContext(mainActivity);
    }

    @Override
    public boolean isGooglePlayClientConnected() {
        return mInteractor.isGooglePlayClientConnected();
    }
}