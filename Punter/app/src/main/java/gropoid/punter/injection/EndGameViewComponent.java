package gropoid.punter.injection;

import gropoid.punter.view.impl.EndGameFragment;

import dagger.Component;

@FragmentScope
@Component(modules = {EndGameViewModule.class, DataAccessModule.class})
public interface EndGameViewComponent {
    void inject(EndGameFragment fragment);
}