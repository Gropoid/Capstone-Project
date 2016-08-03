package gropoid.punter.injection;

import gropoid.punter.view.impl.LeaderBoardsFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = LeaderBoardsViewModule.class)
public interface LeaderBoardsViewComponent {
    void inject(LeaderBoardsFragment fragment);
}