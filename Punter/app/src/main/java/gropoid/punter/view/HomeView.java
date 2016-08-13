package gropoid.punter.view;

import android.support.annotation.UiThread;

@UiThread
public interface HomeView {
    void showGooglePlayPanelIfNotConnected();
}