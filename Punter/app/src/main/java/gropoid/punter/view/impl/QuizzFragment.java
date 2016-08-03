package gropoid.punter.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gropoid.punter.R;
import gropoid.punter.view.QuizzView;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.presenter.QuizzPresenter;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.QuizzViewModule;
import gropoid.punter.injection.DaggerQuizzViewComponent;

import javax.inject.Inject;

public final class QuizzFragment extends BaseFragment<QuizzPresenter, QuizzView> implements QuizzView {
    @Inject
    PresenterFactory<QuizzPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    public QuizzFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quizz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerQuizzViewComponent.builder()
                .appComponent(parentComponent)
                .quizzViewModule(new QuizzViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<QuizzPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
