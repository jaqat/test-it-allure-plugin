package com.github.jaqat.testit.api.client;

import com.github.jaqat.testit.api.ITestItApiClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestItApiClientBuilder {

	public static ITestItApiClient buildTestItApiClient(@NotNull String serverUrl, @NotNull String apiToken) {
		OkHttpClient.Builder httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder();
		httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
		httpClient.addInterceptor(new TestItAuthInterceptor(apiToken));

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(serverUrl)
				.addCallAdapterFactory(new TestItApiCallAdapter())
				.addConverterFactory(GsonConverterFactory.create())
				.client(httpClient.build())
				.build();

		return retrofit.create(ITestItApiClient.class);
	}
}
