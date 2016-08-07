package gropoid.punter.presenter;

import gropoid.punter.view.MainView;

public interface MainPresenter extends BasePresenter<MainView> {

    void loadCurrentStatus();
}