package org.lamisplus.datafi.api;

import android.util.Log;

import okhttp3.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HttpConnectionManager {
    private final OkHttpClient httpClient = new OkHttpClient();

    public String get(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("custom-key", "lamisplus")  // add request headers
                .addHeader("User-Agent", "OkHttp Bot")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            return Objects.requireNonNull(response.body()).string();
        }
    }

    public String post(Object obj, String token, String url) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),  obj.toString());
        //String hash = Hashing.sha256().hashBytes(bytes).toString();
        Request request = new Request.Builder()
                .url(url)
//                .addHeader("User-Agent", "OkHttp Bot")
//                .addHeader("Hash-Value", hash)
//                .addHeader("token", "lamisplus")
//                .addHeader("Authorization", token)
                .post(body)
                .build();

        try (Response response = httpClient
                .newBuilder()
                .connectTimeout(80, TimeUnit.MINUTES)
                .readTimeout(80, TimeUnit.MINUTES)
                .writeTimeout(80, TimeUnit.MINUTES)
                .build()
                .newCall(request).execute()
        ) {
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//            return Objects.requireNonNull(response.body()).string();
            return Objects.requireNonNull(response.body()).string();
        }
    }
}
