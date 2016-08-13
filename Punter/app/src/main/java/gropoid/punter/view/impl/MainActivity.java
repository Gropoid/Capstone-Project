package gropoid.punter.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.facebook.stetho.BuildConfig;

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
        implements MainView {
    private static final String QUIZZ_FRAGMENT_TAG = "QuizzFragmentTag";
    private static final String ENDGAME_FRAGMENT_TAG = "EndGameFragmentTag";
    private static final String HOME_FRAGMENT_TAG = "HomeFragmentTag";
    @Inject
    PresenterFactory<MainPresenter> mPresenterFactory;
    @BindView(R.id.main_frame)
    FrameLayout mainFrame;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
        bindLayout();
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

    }

    @Override
    public boolean isGooglePlayApiConnected() {
        return (mPresenter != null && mPresenter.isGooglePlayClientConnected());
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

    protected void bindLayout() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            getMenuInflater().inflate(R.menu.menu_debug, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.debug:
                startActivity(new Intent(this, DebugActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
}
