package gropoid.punter.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gropoid.punter.R;
import gropoid.punter.domain.Question;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.DaggerQuizzViewComponent;
import gropoid.punter.injection.DataAccessModule;
import gropoid.punter.injection.QuizzViewModule;
import gropoid.punter.presenter.QuizzPresenter;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.view.QuizzView;
import timber.log.Timber;

public final class QuizzFragment extends BaseFragment<QuizzPresenter, QuizzView> implements QuizzView {
    @Inject
    PresenterFactory<QuizzPresenter> mPresenterFactory;
    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.game0)
    GameView game0;
    @BindView(R.id.game1)
    GameView game1;
    @BindView(R.id.game2)
    GameView game2;
    @BindView(R.id.game3)
    GameView game3;

    // Your presenter is available using the mPresenter variable

    public static QuizzFragment newInstance() {
        return new QuizzFragment();
    }

    public QuizzFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quizz, container, false);
        ButterKnife.bind(this, v);
        return v;
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
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerQuizzViewComponent.builder()
                .quizzViewModule(new QuizzViewModule())
                .dataAccessModule(new DataAccessModule(getContext()))
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<QuizzPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @OnClick({R.id.game0, R.id.game1, R.id.game2, R.id.game3})
    public void onClick(View view) {
        if (mPresenter != null) {
            switch (view.getId()) {
                case R.id.game0:
                    mPresenter.submitAnswer0();
                    break;
                case R.id.game1:
                    mPresenter.submitAnswer1();
                    break;
                case R.id.game2:
                    mPresenter.submitAnswer2();
                    break;
                case R.id.game3:
                    mPresenter.submitAnswer3();
                    break;
            }
        } else {
            Timber.w("mPresenter was null");
        }
    }

    @Override
    public void showQuestion(Question question) {
        this.question.setText(question.getQuestionText());
        game0.bind(question.getGames()[0]);
        game1.bind(question.getGames()[1]);
        game2.bind(question.getGames()[2]);
        game3.bind(question.getGames()[3]);
    }
}
