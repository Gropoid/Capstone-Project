package gropoid.punter.interactor;

import gropoid.punter.view.GoogleApiStateListener;
import gropoid.punter.view.PlayGamesHelper;
import gropoid.punter.view.impl.MainActivity;

public interface MainInteractor extends BaseInteractor {

    int getCurrentState();

    void setCurrentStateHome();
    void setCurrentStateQuizz();
    void setCurrentStateEndGame();
    void setCurrentStateLeaderBoards();

    void connectPlayGamesApi();
    void registerGoogleApiListener(GoogleApiStateListener listener);
    void unregisterGoogleApiListener(GoogleApiStateListener listener);
    void notifyGoogleApiFailure();
    void setGoogleApiContext(MainActivity mainActivity);
    boolean isGooglePlayClientConnected();

    PlayGamesHelper getPlayGamesHelper();

}