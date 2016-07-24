package gropoid.punter.injection;

import gropoid.punter.view.impl.DebugActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = DebugViewModule.class)
public interface DebugViewComponent {
    void inject(DebugActivity activity);
}