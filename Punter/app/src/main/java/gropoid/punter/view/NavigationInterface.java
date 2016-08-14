package gropoid.punter.view;


public interface NavigationInterface {
    void showHome();
    void startQuizz();
    void showLeaderboards();
    void showDebug();
    void toggleSignInNavigation(boolean isSignedIn);

    void closeDrawers();
}
