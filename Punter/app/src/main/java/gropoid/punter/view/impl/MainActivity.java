package gropoid.punter.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import gropoid.punter.R;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.DaggerMainViewComponent;
import gropoid.punter.injection.MainViewModule;
import gropoid.punter.presenter.MainPresenter;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.view.MainView;

public final class MainActivity extends BaseActivity<MainPresenter, MainView>
        implements MainView {
    private static final String QUIZZ_FRAGMENT_TAG = "QuizzFragmentTag";
    @Inject
    PresenterFactory<MainPresenter> mPresenterFactory;
    @BindView(R.id.main_frame)
    FrameLayout mainFrame;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void onStart() {
        super.onStart();
        assert mPresenter != null;
        mPresenter.loadCurrentStatus();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerMainViewComponent.builder()
                .appComponent(parentComponent)
                .mainViewModule(new MainViewModule())
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
        Toast.makeText(this, "Home !!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void startQuizz() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        QuizzFragment quizzFragment = (QuizzFragment) fragmentManager.findFragmentByTag(QUIZZ_FRAGMENT_TAG);
        if (quizzFragment == null) {
            quizzFragment = QuizzFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, quizzFragment, QUIZZ_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void showLeaderBoards() {
        Toast.makeText(this, "LeaderBoards !!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void showEndGame() {
        Toast.makeText(this, "End Game !!", Toast.LENGTH_LONG).show();
    }
}
