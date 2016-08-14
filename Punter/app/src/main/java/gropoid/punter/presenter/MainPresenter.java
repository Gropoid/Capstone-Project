package gropoid.punter.presenter;

import android.support.design.widget.NavigationView;

import gropoid.punter.view.GoogleApiStateListener;
import gropoid.punter.view.MainView;
import gropoid.punter.view.PlayGamesHelper;
import gropoid.punter.view.impl.MainActivity;

public interface MainPresenter extends BasePresenter<MainView>, NavigationView.OnNavigationItemSelectedListener {

    void loadCurrentState();

    void setCurrentStateHome();
    void setCurrentStateQuizz();
    void setCurrentStateEndGame();

    void connectPlayGamesApi();
    void registerGoogleApiListener(GoogleApiStateListener listener);
    void unregisterGoogleApiListener(GoogleApiStateListener listener);
    void notifyGoogleApiFailure();
    void setGoogleApiContext(MainActivity mainActivity);
    boolean isGooglePlayClientConnected();

    PlayGamesHelper getPlayGamesHelper();
}