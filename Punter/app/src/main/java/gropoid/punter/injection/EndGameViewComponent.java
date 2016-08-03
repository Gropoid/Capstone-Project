package gropoid.punter.injection;

import gropoid.punter.view.impl.EndGameFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = EndGameViewModule.class)
public interface EndGameViewComponent {
    void inject(EndGameFragment fragment);
}