package gropoid.punter.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gropoid.punter.R;
import gropoid.punter.view.LeaderBoardsView;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.presenter.LeaderBoardsPresenter;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.LeaderBoardsViewModule;
import gropoid.punter.injection.DaggerLeaderBoardsViewComponent;

import javax.inject.Inject;

public final class LeaderBoardsFragment extends BaseFragment<LeaderBoardsPresenter, LeaderBoardsView> implements LeaderBoardsView {
    @Inject
    PresenterFactory<LeaderBoardsPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    public LeaderBoardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leader_boards, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerLeaderBoardsViewComponent.builder()
                .appComponent(parentComponent)
                .leaderBoardsViewModule(new LeaderBoardsViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<LeaderBoardsPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
