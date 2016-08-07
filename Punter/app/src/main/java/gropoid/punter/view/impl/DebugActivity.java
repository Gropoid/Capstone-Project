package gropoid.punter.view.impl;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gropoid.punter.R;
import gropoid.punter.domain.QuestionManager;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.DaggerDebugViewComponent;
import gropoid.punter.injection.DataAccessModule;
import gropoid.punter.injection.DebugViewModule;
import gropoid.punter.presenter.DebugPresenter;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.retrofit.GameFetchIntentService;
import gropoid.punter.view.DebugView;

public final class DebugActivity extends BaseActivity<DebugPresenter, DebugView> implements DebugView {
    @Inject
    PresenterFactory<DebugPresenter> mPresenterFactory;
    @Inject
    QuestionManager questionManager;


    @BindView(R.id.fetch_games_button)
    Button fetchGamesButton;
    @BindView(R.id.generate_questions_button)
    Button generateQuestionsButton;
    @BindView(R.id.get_questions_button)
    Button getQuestionsButton;
    @BindView(R.id.main_activity_button)
    Button mainActivityButton;


    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerDebugViewComponent.builder()
                .debugViewModule(new DebugViewModule())
                .dataAccessModule(new DataAccessModule(getBaseContext()))
                .build()
                .inject(this);
    }

    @OnClick(R.id.fetch_games_button)
    void fetchGames() {
        GameFetchIntentService.startFetchGames(getBaseContext());
    }

    @OnClick(R.id.generate_questions_button)
    void generateQuestions() {
        AsyncTask<Void, Void, Void> t = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                questionManager.generateQuestions(100);

                return null;
            }
        };
        t.execute();
    }

    @OnClick(R.id.get_questions_button)
    void getQuestions() {
        questionManager.getQuestions(5);
        Toast.makeText(this, "retrieved questions", Toast.LENGTH_LONG).show();
    }

    @NonNull
    @Override
    protected PresenterFactory<DebugPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @OnClick(R.id.main_activity_button)
    public void onClick() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
