package gropoid.punter.presenter;

import gropoid.punter.view.QuizzView;

public interface QuizzPresenter extends BasePresenter<QuizzView> {

    void submitAnswer0();

    void submitAnswer1();

    void submitAnswer2();

    void submitAnswer3();
}