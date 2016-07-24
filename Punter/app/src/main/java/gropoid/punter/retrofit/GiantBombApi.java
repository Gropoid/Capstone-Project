package gropoid.punter.retrofit;


import gropoid.punter.retrofit.dto.GameDTO;
import gropoid.punter.retrofit.dto.Page;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GiantBombApi
{
    @GET("/api/games")
    Call<Page<GameDTO>> getGames();

    @GET
    Call<ResponseBody> getPlatform(@Url String url);

    @GET
    Call<ResponseBody> getImage(@Url String url);
}
