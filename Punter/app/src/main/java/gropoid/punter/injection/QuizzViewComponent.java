package gropoid.punter.injection;

import gropoid.punter.view.impl.QuizzFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = QuizzViewModule.class)
public interface QuizzViewComponent {
    void inject(QuizzFragment fragment);
}