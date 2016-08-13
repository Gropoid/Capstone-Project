package gropoid.punter.view.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gropoid.punter.R;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.DaggerEndGameViewComponent;
import gropoid.punter.injection.DataAccessModule;
import gropoid.punter.injection.EndGameViewModule;
import gropoid.punter.presenter.EndGamePresenter;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.view.EndGameFragmentInterface;
import gropoid.punter.view.EndGameView;
import gropoid.punter.view.GoogleApiStateListener;
import timber.log.Timber;

public final class EndGameFragment extends BaseFragment<EndGamePresenter, EndGameView>
        implements EndGameView, GoogleApiStateListener {
    @Inject
    PresenterFactory<EndGamePresenter> mPresenterFactory;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.home)
    Button home;
    @BindView(R.id.new_game)
    Button newGame;
    @BindView(R.id.leaderboards)
    Button leaderboards;

    private EndGameFragmentInterface host;

    // Your presenter is available using the mPresenter variable

    public EndGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_end_game, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart
    }

    @Override
    public void onStart() {
        super.onStart();
        assert mPresenter != null;
        if (host != null) {
            mPresenter.setPlayGamesHelper(host.getPlayGamesHelper());
            mPresenter.submitScore();
            mPresenter.checkGoogleApiConnected();
        }
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerEndGameViewComponent.builder()
                .endGameViewModule(new EndGameViewModule())
                .dataAccessModule(new DataAccessModule((getContext())))
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<EndGamePresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            host = (EndGameFragmentInterface) context;
        } catch (ClassCastException e) {
            Timber.e("Host activity must implement EndGameFragmentListener");
        }

    }

    @OnClick({R.id.home, R.id.new_game})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                host.showHome();
                break;
            case R.id.new_game:
                host.startQuizz();
                break;
        }
    }

    public static EndGameFragment newInstance() {
        return new EndGameFragment();
    }

    @Override
    public void displayScore(int gameScore) {
        score.setText(String.valueOf(gameScore));
    }

    @Override
    public void toggleLeaderboardsButton(boolean visible) {
        leaderboards.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onConnectionSuccessful() {
        leaderboards.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionFailed() {
        leaderboards.setVisibility(View.GONE);
    }

    @OnClick(R.id.leaderboards)
    public void onClick() {
        host.showLeaderboards();
    }
}
