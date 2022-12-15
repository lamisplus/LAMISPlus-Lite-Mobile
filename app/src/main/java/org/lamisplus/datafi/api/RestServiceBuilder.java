package org.lamisplus.datafi.api;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.TokenRequest;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestServiceBuilder {

    protected static final LamisPlus mLamisPlus = LamisPlus.getInstance();

    private static String API_BASE_URL = mLamisPlus.getServerUrl() + ApplicationConstants.API.REST_ENDPOINT;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder;
    private static TokenRequest tokenRequest;

    static {
        builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(buildGsonConverter())
                        .client((httpClient).build());
    }

    public static <S> S createService(Class<S> serviceClass, String token) {
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .header("Accept", "application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);

            });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass) {
        String username=mLamisPlus.getUsername();
        String password=mLamisPlus.getPassword();
        return createService(serviceClass, new BearerApi(mLamisPlus.getServerUrl(), username, password, true).getToken());
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson myGson = gsonBuilder
                .setPrettyPrinting().setLenient()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        return GsonConverterFactory.create(myGson);
    }

    public static <S> S createServiceForPatientIdentifier(Class<S> clazz) {
        return new Retrofit.Builder()
                .baseUrl(mLamisPlus.getServerUrl() + '/')
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(clazz);
    }

    public static void changeBaseUrl(String newServerUrl) {
        API_BASE_URL = newServerUrl + ApplicationConstants.API.REST_ENDPOINT;

        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(buildGsonConverter());
    }
}
