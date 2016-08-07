package gropoid.punter.interactor;

public interface MainInteractor extends BaseInteractor {

    int getCurrentState();

    void setCurrentStateHome();
    void setCurrentStateQuizz();
    void setCurrentStateEndGame();
    void setCurrentStateLeaderBoards();
}