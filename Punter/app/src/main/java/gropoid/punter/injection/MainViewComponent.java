package gropoid.punter.injection;

import gropoid.punter.view.impl.MainActivity;

import dagger.Component;

@ActivityScope
@Component( modules = {MainViewModule.class, DataAccessModule.class, PlayGamesApiModule.class})
public interface MainViewComponent {
    void inject(MainActivity activity);
}