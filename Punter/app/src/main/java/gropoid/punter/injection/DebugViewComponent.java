package gropoid.punter.injection;

import gropoid.punter.view.impl.DebugActivity;

import dagger.Component;

@ActivityScope
@Component( modules = {DebugViewModule.class, DataAccessModule.class})
public interface DebugViewComponent {
    void inject(DebugActivity activity);
}