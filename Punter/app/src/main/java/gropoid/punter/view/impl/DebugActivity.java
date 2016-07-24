package gropoid.punter.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gropoid.punter.R;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.DaggerDebugViewComponent;
import gropoid.punter.injection.DebugViewModule;
import gropoid.punter.presenter.DebugPresenter;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.retrofit.GameFetchIntentService;
import gropoid.punter.view.DebugView;

public final class DebugActivity extends BaseActivity<DebugPresenter, DebugView> implements DebugView {
    @Inject
    PresenterFactory<DebugPresenter> mPresenterFactory;

    @BindView(R.id.fetch_games_button)
    Button fetchGamesButton;


    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
        ButterKnife.bind(this);
        fetchGamesButton = (Button) findViewById(R.id.fetch_games_button);
        fetchGamesButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    GameFetchIntentService.startFetchGames(v.getContext());

                                                }
                                            }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerDebugViewComponent.builder()
                .appComponent(parentComponent)
                .debugViewModule(new DebugViewModule())
                .build()
                .inject(this);
    }

    @OnClick(R.id.fetch_games_button)
    void fetchGames() {
        GameFetchIntentService.startFetchGames(getBaseContext());
    }


    @NonNull
    @Override
    protected PresenterFactory<DebugPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
