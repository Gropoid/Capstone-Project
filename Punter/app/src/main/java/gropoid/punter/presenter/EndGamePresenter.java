package gropoid.punter.presenter;

import gropoid.punter.view.EndGameView;
import gropoid.punter.view.PlayGamesHelper;

public interface EndGamePresenter extends BasePresenter<EndGameView> {

    void setPlayGamesHelper(PlayGamesHelper playGamesHelper);

    void submitScore();

    void checkGoogleApiConnected();
}