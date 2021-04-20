package com.github.jaqat.testit.api.client;

import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public class TestItApiCallAdapter<T> extends CallAdapter.Factory {

    @Override
    public CallAdapter<T, ?> get(Type returnType, @NotNull Annotation[] annotations, @NotNull Retrofit retrofit) {
        if (returnType.getTypeName().startsWith(Call.class.getName())) {
            return null;
        }
        if (returnType.getTypeName().startsWith(Response.class.getName())) {
            return new ResponseCallAdapter(((ParameterizedType) returnType).getActualTypeArguments()[0]);
        }
        return new InstanceCallAdapter(returnType);
    }

    private static String getErrorMessage(Response<?> response) {
        ResponseBody errorBody = response.errorBody();
        String errorMessage;
        try {
            errorMessage = Objects.isNull(errorBody) ? response.message() : errorBody.string();
        } catch (IOException e) {
            throw new TestItApiException("Could not read error body", e);
        }
        return errorMessage;
    }

    private class ResponseCallAdapter implements CallAdapter<T, Response<T>> {

        private Type returnType;

        ResponseCallAdapter(Type returnType) {
            this.returnType = returnType;
        }

        @NotNull
        @Override
        public Type responseType() {
            return returnType;
        }

        @NotNull
        @Override
        public Response<T> adapt(Call<T> call) {
            Response<T> response;
            try {
                response = call.execute();
            } catch (IOException e) {
                throw new TestItApiException("Could not execute request", e);
            }

            if (!response.isSuccessful()) {
                throw new TestItApiException(getErrorMessage(response));
            }
            return response;
        }
    }

    private class InstanceCallAdapter implements CallAdapter<T, Object> {

        private Type returnType;

        InstanceCallAdapter(Type returnType) {
            this.returnType = returnType;
        }

        @NotNull
        @Override
        public Type responseType() {
            return returnType;
        }

        @NotNull
        @Override
        public Object adapt(Call<T> call) {
            Response<T> response;
            try {
                response = call.execute();
            } catch (IOException e) {
                throw new TestItApiException("Could not get request body", e);
            }
            if (!response.isSuccessful()) {
                throw new TestItApiException(getErrorMessage(response));
            }
            return Objects.requireNonNull(response.body());
        }
    }

}
