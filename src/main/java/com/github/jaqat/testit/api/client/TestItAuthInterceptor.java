package com.github.jaqat.testit.api.client;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TestItAuthInterceptor implements Interceptor {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String APPLICATION_JSON = "application/json";
	private final String apiToken;

	public TestItAuthInterceptor(String apiToken) {
		this.apiToken = apiToken;
	}


	@Override
	public Response intercept(Chain chain) throws IOException {
		Request original = chain.request();
//        url contains '?' retrofit parse next values as @Query
		String url = original.url().toString().replaceAll("%3F", "?");
		Request.Builder requestBuilder = original.newBuilder()
				.header(AUTHORIZATION_HEADER, "PrivateToken " + apiToken)
				.header(CONTENT_TYPE, APPLICATION_JSON)
				.url(url);

		Request request = requestBuilder.build();
		return chain.proceed(request);
	}
}
