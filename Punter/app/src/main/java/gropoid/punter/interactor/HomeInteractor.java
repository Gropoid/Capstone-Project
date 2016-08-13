package gropoid.punter.interactor;

import gropoid.punter.view.PlayGamesHelper;

public interface HomeInteractor extends BaseInteractor {

    void syncIfneeded();

    void setPlayGamesHelper(PlayGamesHelper playGamesHelper);

    boolean isGooglePlayApiConnected();
}