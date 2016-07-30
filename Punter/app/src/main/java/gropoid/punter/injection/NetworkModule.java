package gropoid.punter.injection;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import gropoid.punter.BuildConfig;
import gropoid.punter.retrofit.GiantBombApi;
import gropoid.punter.retrofit.GiantBombInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    @Provides
    public GiantBombApi provideGiantBombApi(OkHttpClient client, Gson gson) {
        Retrofit giantBombRetrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.GiantBombBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return giantBombRetrofit.create(GiantBombApi.class);

    }

    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                //1992-02-29 00:00:00
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);
        return logger;
    }

    @Provides
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor logger) {
        return new OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(new GiantBombInterceptor())
                .build();
    }

}
