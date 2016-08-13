package gropoid.punter.presenter;

import gropoid.punter.view.HomeView;
import gropoid.punter.view.PlayGamesHelper;

public interface HomePresenter extends BasePresenter<HomeView> {

    void setPlayGamesHelper(PlayGamesHelper playGamesHelper);

    void checkGoogleApiConnected();
}