package gropoid.punter.view.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.common.SignInButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gropoid.punter.R;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.DaggerHomeViewComponent;
import gropoid.punter.injection.DataAccessModule;
import gropoid.punter.injection.HomeViewModule;
import gropoid.punter.presenter.HomePresenter;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.view.GoogleApiStateListener;
import gropoid.punter.view.HomeFragmentInterface;
import gropoid.punter.view.HomeView;
import timber.log.Timber;

public final class HomeFragment extends BaseFragment<HomePresenter, HomeView> implements HomeView, GoogleApiStateListener {
    @Inject
    PresenterFactory<HomePresenter> mPresenterFactory;
    @BindView(R.id.new_game)
    Button newGame;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;
    @BindView(R.id.sign_in_bar)
    LinearLayout signInBar;

    private HomeFragmentInterface host;

    // Your presenter is available using the mPresenter variable

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerHomeViewComponent.builder()
                .homeViewModule(new HomeViewModule())
                .dataAccessModule(new DataAccessModule(getContext()))
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<HomePresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            host = (HomeFragmentInterface) context;
        } catch (ClassCastException e) {
            Timber.e("Host activity must implement HomeFragmentListener");
        }
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @OnClick({R.id.new_game, R.id.sign_in_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_game:
                host.startQuizz();
                break;
            case R.id.sign_in_button:
                host.signIn();
                break;
        }
    }

    @Override
    public void onConnectionSuccessful() {
        signInBar.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed() {
        signInButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showGooglePlayPanelIfNotConnected() {
        if (host.isGooglePlayApiConnected()) {
            onConnectionSuccessful();
        } else {
            onConnectionFailed();
        }
    }
}
