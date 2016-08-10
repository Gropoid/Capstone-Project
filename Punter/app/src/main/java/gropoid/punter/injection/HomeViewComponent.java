package gropoid.punter.injection;

import gropoid.punter.view.impl.HomeFragment;

import dagger.Component;

@FragmentScope
@Component(modules = {HomeViewModule.class, DataAccessModule.class})
public interface HomeViewComponent {
    void inject(HomeFragment fragment);
}