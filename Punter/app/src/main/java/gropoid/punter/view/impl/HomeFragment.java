package gropoid.punter.view.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import gropoid.punter.view.HomeFragmentListener;
import gropoid.punter.view.HomeView;
import timber.log.Timber;

public final class HomeFragment extends BaseFragment<HomePresenter, HomeView> implements HomeView {
    @Inject
    PresenterFactory<HomePresenter> mPresenterFactory;
    @BindView(R.id.new_game)
    Button newGame;

    private HomeFragmentListener host;

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
            host = (HomeFragmentListener)context;
        } catch (ClassCastException e) {
            Timber.e("Host activity must implement HomeFragmentListener");
        }
    }

    @OnClick(R.id.new_game)
    public void onClick() {
        host.startQuizz();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
}
