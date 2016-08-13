package gropoid.punter.injection;

import gropoid.punter.view.impl.SignInActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = SignInViewModule.class)
public interface SignInViewComponent {
    void inject(SignInActivity activity);
}