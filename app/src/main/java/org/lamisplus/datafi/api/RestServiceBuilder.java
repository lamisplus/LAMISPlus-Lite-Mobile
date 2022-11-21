package org.lamisplus.datafi.api;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.LoginRequest;
import org.lamisplus.datafi.classes.TokenRequest;
import org.lamisplus.datafi.models.Resource;
import org.lamisplus.datafi.models.Token;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.ResourceSerializer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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

    public static void createNewBearer(String username, String password, boolean rememberMe){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                LoginRequest loginRequest = new LoginRequest(username, password, rememberMe);
                String loginPayload = new Gson().toJson(loginRequest);

                OkHttpClient client = new OkHttpClient().newBuilder()

                        .build();

                MediaType mediaType = MediaType.parse("application/json");

                RequestBody body = RequestBody.create(mediaType, loginPayload);

                Request request = new Request.Builder()

                        .url(LamisPlus.getInstance().getServerUrl() + ApplicationConstants.API.REST_ENDPOINT + "authenticate")

                        .method("POST", body)

                        .addHeader("Content-Type", "application/json")

                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    tokenRequest = new Gson().fromJson(Objects.requireNonNull(response.body()).string(), TokenRequest.class);
                    Log.v("Baron", tokenRequest.getId_token());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });
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
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//            httpClient.addInterceptor(new SnooperInterceptor());
//            httpClient.addInterceptor(logging);

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass) {
        String username=mLamisPlus.getUsername();
        String password=mLamisPlus.getPassword();
        return createService(serviceClass, new BearerApi(username, password, true).getToken());
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson myGson = gsonBuilder
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeHierarchyAdapter(Resource.class, new ResourceSerializer()).setLenient()
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
