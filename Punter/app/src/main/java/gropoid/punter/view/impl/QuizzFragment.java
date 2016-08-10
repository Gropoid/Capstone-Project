package gropoid.punter.view.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import gropoid.punter.view.QuizzFragmentListener;
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

    QuizzFragmentListener host;
    @BindView(R.id.first_row)
    LinearLayout firstRow;
    @BindView(R.id.second_row)
    LinearLayout secondRow;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.progress_label)
    TextView progressLabel;

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
    public void onAttach(Context context) {
        try {
            host = (QuizzFragmentListener) context;
        } catch (ClassCastException e) {
            Timber.e("Host activity must implement QuizzFragmentListener");
        }
        super.onAttach(context);
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
                    mPresenter.submitAnswer(0);
                    break;
                case R.id.game1:
                    mPresenter.submitAnswer(1);
                    break;
                case R.id.game2:
                    mPresenter.submitAnswer(2);
                    break;
                case R.id.game3:
                    mPresenter.submitAnswer(3);
                    break;
            }
        } else {
            Timber.w("mPresenter was null");
        }
    }

    @Override
    public void showQuestion(Question question) {
        if (question == null) {
            Timber.e("No questions in db)");
            host.showEndGame();
        } else {
            this.question.setText(question.getWording());
            game0.bind(question.getGames()[0]);
            game1.bind(question.getGames()[1]);
            game2.bind(question.getGames()[2]);
            game3.bind(question.getGames()[3]);
        }
    }

    @Override
    public void showResult(boolean b) {
        Toast.makeText(getContext(), b ? "CORRECT !" : "FALSE", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEndGame() {
        host.showEndGame();
    }

    @Override
    public void showLoading(int progress) {
        Timber.v("showing progress value %s", progress);
        if (progress == 0) {
            firstRow.setVisibility(View.GONE);
            secondRow.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressLabel.setVisibility(View.VISIBLE);
        } else if (progress >= 100) {
            firstRow.setVisibility(View.VISIBLE);
            secondRow.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressLabel.setVisibility(View.GONE);
        } else {
            progressBar.setProgress(progress);
        }
    }

    @Override
    public void displayLoadingError() {
        Toast.makeText(getContext(), "No more data. Try connecting to the internet to refresh quizz data.", Toast.LENGTH_LONG).show();
        host.showHome();
    }
}
