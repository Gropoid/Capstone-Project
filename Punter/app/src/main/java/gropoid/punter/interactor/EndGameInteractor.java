package gropoid.punter.interactor;

import gropoid.punter.view.PlayGamesHelper;

public interface EndGameInteractor extends BaseInteractor {

    int getGameScore();

    void submitScore();

    void setPlayGamesHelper(PlayGamesHelper playGamesHelper);

    boolean isPlayApiConnected();
}