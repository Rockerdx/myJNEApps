package online.jne.com.jneapps.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import online.jne.com.jneapps.BuildConfig;
import online.jne.com.jneapps.model.CabangResponse;
import online.jne.com.jneapps.model.DetailResponse;
import online.jne.com.jneapps.model.HistoryResponse;
import online.jne.com.jneapps.model.OrderResponse;
import online.jne.com.jneapps.model.UserResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class ApiClient {

    public static final String server = "http://jnejemput.com/api/";
    private static ApiInterface sMasharaService;

    public static ApiInterface getTiketApiClient() {
        if (sMasharaService == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(2, TimeUnit.MINUTES);
            httpClientBuilder.readTimeout(2, TimeUnit.MINUTES);
            httpClientBuilder.writeTimeout(2, TimeUnit.MINUTES);

            if (BuildConfig.DEBUG) {
                // enable logging for debug builds
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClientBuilder.addInterceptor(loggingInterceptor);
            }
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(server)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .callFactory(httpClientBuilder.build())
                    .build();
            sMasharaService = retrofit.create(ApiInterface.class);
        }
        return sMasharaService;
    }

    public interface ApiInterface {

        @FormUrlEncoded
        @POST("?action=login")
        Call<UserResponse> authenticate(@Field("nope") String nope,
                                        @Field("password") String password,
                                        @Field("token") String token);

        @FormUrlEncoded
        @POST("?action=order")
        Call<OrderResponse> postOrder(@Field("id_customer") String id_costumer,
                                      @Field("lat") String latuser,
                                      @Field("lon") String lnguser,
                                      @Field("id_cabang") String id_cabang,
                                      @Field("kisaran_berat") String berat,
                                      @Field("keterangan") String keterangan);
        @FormUrlEncoded
        @POST("?action=register")
        Call<UserResponse> registration(@Field("email") String email,
                                        @Field("password") String password,
                                        @Field("nope") String hp,
                                        @Field("nama_customer") String name,
                                        @Field("token") String token);

        @GET("?action=list-cabang")
        Call<CabangResponse> getCabang();

        @FormUrlEncoded
        @POST("?action=riwayat-orderan")
        Call<HistoryResponse> getHistory(@Field("id_customer") String idUser);

        @FormUrlEncoded
        @POST("?action=detail-orderan")
        Call<DetailResponse> getDetail(@Field("kode_order") String codeOrder);

    }
}
