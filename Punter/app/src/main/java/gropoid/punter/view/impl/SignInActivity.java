package gropoid.punter.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;

import gropoid.punter.R;
import gropoid.punter.view.SignInView;
import gropoid.punter.presenter.loader.PresenterFactory;
import gropoid.punter.presenter.SignInPresenter;
import gropoid.punter.injection.AppComponent;
import gropoid.punter.injection.SignInViewModule;
import gropoid.punter.injection.DaggerSignInViewComponent;

import javax.inject.Inject;

public final class SignInActivity extends BaseActivity<SignInPresenter, SignInView> implements SignInView {
    @Inject
    PresenterFactory<SignInPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerSignInViewComponent.builder()
                .appComponent(parentComponent)
                .signInViewModule(new SignInViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<SignInPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
