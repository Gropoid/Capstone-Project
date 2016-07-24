package gropoid.punter.injection;


import dagger.Component;
import gropoid.punter.data.PunterProvider;
import gropoid.punter.retrofit.GameFetchIntentService;

@BackgroundScope
@Component(modules = {DataAccessModule.class, NetworkModule.class})
public interface BackgroundComponent {
    void inject(GameFetchIntentService service);
    void inject(PunterProvider provider);
}
