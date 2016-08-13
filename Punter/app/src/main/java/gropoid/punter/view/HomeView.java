package gropoid.punter.view;

import android.support.annotation.UiThread;

@UiThread
public interface HomeView {
    void toggleGooglePlayPanel(boolean visible);

    void toggleLeaderboardsButton(boolean visible);
}