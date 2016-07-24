package gropoid.punter.injection;


import dagger.Component;
import gropoid.punter.retrofit.GameFetchIntentService;

@BackgroundScope
@Component(dependencies = AppComponent.class, modules = {DataAccessModule.class, NetworkModule.class})
public interface BackgroundComponent {
    void inject(GameFetchIntentService service);
}
