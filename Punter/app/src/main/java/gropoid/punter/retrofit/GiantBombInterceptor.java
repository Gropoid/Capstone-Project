package gropoid.punter.retrofit;

import java.io.IOException;

import gropoid.punter.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class GiantBombInterceptor implements Interceptor {

    public static final String API_KEY = "api_key";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = process(chain.request());
        return chain.proceed(newRequest);
    }

    private Request process(Request request) {
        HttpUrl url = request.url();
        HttpUrl newUrl = url.newBuilder()
                .addQueryParameter(API_KEY, BuildConfig.GiantBombApiKey)
                .addQueryParameter("format", "json")
                .build();
        return request.newBuilder().url(newUrl).build();
    }
}
