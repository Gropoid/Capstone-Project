package gropoid.punter.view;


import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;

public interface PlayGamesHelper {
    int RC_SIGNIN = 9001;

    void setActivity(Activity activity);
    void signIn();
    boolean isSignedIn();
    GoogleApiClient getGoogleApiClient();
    void registerListener(GoogleApiStateListener listener);
    void unregisterListener(GoogleApiStateListener listener);
    void notifyFailure();
}
