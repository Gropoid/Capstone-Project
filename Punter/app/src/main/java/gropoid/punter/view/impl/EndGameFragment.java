package gropoid.punter.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gropoid.punter.R;
import gropoid.punter.view.EndGameView;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.presenter.EndGamePresenter;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.EndGameViewModule;
import gropoid.punter.injection.DaggerEndGameViewComponent;

import javax.inject.Inject;

public final class EndGameFragment extends BaseFragment<EndGamePresenter, EndGameView> implements EndGameView {
    @Inject
    PresenterFactory<EndGamePresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    public EndGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_end_game, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerEndGameViewComponent.builder()
                .appComponent(parentComponent)
                .endGameViewModule(new EndGameViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<EndGamePresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
