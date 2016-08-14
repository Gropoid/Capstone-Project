package gropoid.punter.domain;


import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import gropoid.punter.data.PunterState;
import gropoid.punter.view.GoogleApiStateListener;
import gropoid.punter.view.PlayGamesHelper;
import timber.log.Timber;

public class PlayGamesHelperImpl implements PlayGamesHelper,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    @Inject
    GoogleApiClient googleApiClient;
    @Inject
    PunterState punterState;

    private Activity activity;
    Set<GoogleApiStateListener> listeners;

    @Inject
    public PlayGamesHelperImpl(GoogleApiClient googleApiClient, PunterState punterState) {
        this.googleApiClient = googleApiClient;
        this.punterState = punterState;
        googleApiClient.registerConnectionCallbacks(this);
        googleApiClient.registerConnectionFailedListener(this);
        listeners = new HashSet<>();
        init();
    }

    private void init() {
        if (punterState.isUserSignedIn()) {
            googleApiClient.connect();
        }
    }

    @Override
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void signIn() {
        Timber.v("signIn()");
        punterState.setIsUserSignedIn(true);
        googleApiClient.connect();
    }

    @Override
    public boolean isConnected() {
        Timber.v("isConnected() = %s | isConnected = %s (%s)", punterState.isUserSignedIn(), googleApiClient.isConnected(), googleApiClient);
        return googleApiClient.isConnected();
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        Timber.v("getGoogleApiClient() = %s", googleApiClient);
        return googleApiClient;
    }

    @Override
    public void registerListener(GoogleApiStateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(GoogleApiStateListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.v("onConnected()");
        punterState.setIsUserSignedIn(true);
        Games.setViewForPopups(googleApiClient, activity.getWindow().getDecorView());
        notifyConnection();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.v("onConnectionSuspended()");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.v("onDisconnected()");
        if (activity != null) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(activity, RC_SIGNIN);
                } catch (IntentSender.SendIntentException e) {
                    // The intent was canceled before it was sent.  Return to the default
                    // state and attempt to connect to get an updated ConnectionResult.
                    googleApiClient.connect();
                }
            } else {
                onDisconnected();
            }
        }
    }

    @Override
    //Visible because we may need to notify from the Activity after a result from the startResolutionForResult() call
    public void onDisconnected() {
        punterState.setIsUserSignedIn(false);
        for (GoogleApiStateListener listener : listeners) {
            listener.onDisconnected();
        }
    }

    @Override
    public void signOut() {
        Timber.v("signOut()");
        Games.signOut(googleApiClient);
        googleApiClient.disconnect();
        onDisconnected();
    }

    private void notifyConnection() {
        for (GoogleApiStateListener listener : listeners) {
            listener.onConnected();
        }
    }

}
