package gropoid.punter.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.stetho.BuildConfig;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import gropoid.punter.R;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.DaggerMainViewComponent;
import gropoid.punter.injection.DataAccessModule;
import gropoid.punter.injection.MainViewModule;
import gropoid.punter.presenter.MainPresenter;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.view.GoogleApiStateListener;
import gropoid.punter.view.MainView;
import gropoid.punter.view.PlayGamesHelper;


public final class MainActivity extends BaseActivity<MainPresenter, MainView>
        implements MainView, GoogleApiStateListener {
    private static final String QUIZZ_FRAGMENT_TAG = "QuizzFragmentTag";
    private static final String ENDGAME_FRAGMENT_TAG = "EndGameFragmentTag";
    private static final String HOME_FRAGMENT_TAG = "HomeFragmentTag";
    private static final int RC_UNUSED = 5001;
    @Inject
    PresenterFactory<MainPresenter> mPresenterFactory;
    @BindView(R.id.main_frame)
    FrameLayout mainFrame;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    View headerLayout;
    TextView navigationHeaderText;
    ImageView navigationHeaderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
        setupView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        assert mPresenter != null;
        mPresenter.loadCurrentState();
        mPresenter.setGoogleApiContext(this);
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerMainViewComponent.builder()
                .mainViewModule(new MainViewModule())
                .dataAccessModule(new DataAccessModule(getApplicationContext()))
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<MainPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void showHome() {
        if (mPresenter != null) {
            mPresenter.setCurrentStateHome();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
        }
        unregisterPreviousGoogleApiListener();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, homeFragment, HOME_FRAGMENT_TAG)
                .commit();
        mPresenter.registerGoogleApiListener(homeFragment);
    }

    private void unregisterPreviousGoogleApiListener() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment oldFragment = fragmentManager.findFragmentById(R.id.main_frame);
        if (oldFragment != null && oldFragment instanceof GoogleApiStateListener && mPresenter != null) {
            mPresenter.unregisterGoogleApiListener((GoogleApiStateListener) oldFragment);
        }
    }


    @Override
    public void startQuizz() {
        if (mPresenter != null) {
            mPresenter.setCurrentStateQuizz();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        QuizzFragment quizzFragment = (QuizzFragment) fragmentManager.findFragmentByTag(QUIZZ_FRAGMENT_TAG);
        if (quizzFragment == null) {
            quizzFragment = QuizzFragment.newInstance();
        }
        unregisterPreviousGoogleApiListener();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, quizzFragment, QUIZZ_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void signIn() {
        if (mPresenter != null) {
            mPresenter.connectPlayGamesApi();
        }
    }

    @Override
    public void showLeaderboards() {
        if (getPlayGamesHelper() != null) {
            startActivityForResult(
                    Games.Leaderboards.getAllLeaderboardsIntent(getPlayGamesHelper().getGoogleApiClient()),
                    RC_UNUSED
            );
        }
    }

    @Override
    public void showDebug() {
        startActivity(new Intent(this, DebugActivity.class));
    }

    @Override
    public void closeDrawers() {
        drawerLayout.closeDrawers();
    }

    @Override
    public PlayGamesHelper getPlayGamesHelper() {
        return mPresenter != null ? mPresenter.getPlayGamesHelper() : null;
    }

    @Override
    public void showEndGame() {
        if (mPresenter != null) {
            mPresenter.setCurrentStateEndGame();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        EndGameFragment endGameFragment = (EndGameFragment) fragmentManager.findFragmentByTag(ENDGAME_FRAGMENT_TAG);
        if (endGameFragment == null) {
            endGameFragment = EndGameFragment.newInstance();
        }
        unregisterPreviousGoogleApiListener();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, endGameFragment, ENDGAME_FRAGMENT_TAG)
                .commit();
        mPresenter.registerGoogleApiListener(endGameFragment);
    }

    protected void setupView() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
        headerLayout = navigationView.getHeaderView(0);
        navigationHeaderText = (TextView) headerLayout.findViewById(R.id.nav_header_name);
        navigationHeaderImage = (ImageView) headerLayout.findViewById(R.id.nav_header_image);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        if (mPresenter != null) {
            mPresenter.registerGoogleApiListener(this);
        }
        navigationView.setNavigationItemSelectedListener(mPresenter);
        toggleSignInNavigation(mPresenter.isGooglePlayClientConnected());
        toggleDebugNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupDrawerContent(navigationView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PlayGamesHelper.RC_SIGNIN) {
            if (resultCode == RESULT_OK && mPresenter != null) {
                mPresenter.connectPlayGamesApi();
            } else {
                notifyGoogleApiFailure();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void notifyGoogleApiFailure() {
        // in the presenter, the helper should already be in disconnected state
        // need to notify the fragments, however
        if (mPresenter != null) {
            mPresenter.notifyGoogleApiFailure();
        }
    }

    @Override
    public void toggleSignInNavigation(boolean isSignedIn) {
        navigationView.getMenu().findItem(R.id.nav_sign_in).setVisible(!isSignedIn);
        navigationView.getMenu().findItem(R.id.nav_sign_out).setVisible(isSignedIn);
        navigationView.getMenu().findItem(R.id.nav_leaderboards).setVisible(isSignedIn);
        if (isSignedIn && getPlayGamesHelper() != null) {
            Player player = Games.Players.getCurrentPlayer(getPlayGamesHelper().getGoogleApiClient());
            Glide.with(this)
                    .load(player.getIconImageUri())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(navigationHeaderImage);
            navigationHeaderText.setText(player.getDisplayName());
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_placeholder)
                    .into(navigationHeaderImage);
            navigationHeaderText.setText("");
        }
    }

    private void toggleDebugNavigation() {
        navigationView.getMenu().findItem(R.id.nav_debug).setVisible(BuildConfig.DEBUG);
    }

    @Override
    public void onConnected() {
        toggleSignInNavigation(true);
    }

    @Override
    public void onDisconnected() {
        toggleSignInNavigation(false);
    }
}
