package gropoid.punter.injection;

import gropoid.punter.view.impl.QuizzFragment;

import dagger.Component;

@FragmentScope
@Component(modules = {QuizzViewModule.class, DataAccessModule.class})
public interface QuizzViewComponent {
    void inject(QuizzFragment fragment);
}