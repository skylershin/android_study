package com.example.munkyushin.okhttpexample;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import rx.Single;
import rx.SingleSubscriber;

/**
 * Created by MunkyuShin on 4/5/16.
 */
public class JsonUtil {
    private ObjectMapper mapper;

    public JsonUtil() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public <T> Single<T> readValue(final String jsonText, final TypeReference<T> type) {
        return Single.create(new Single.OnSubscribe<T>() {
            @Override
            public void call(SingleSubscriber<? super T> singleSubscriber) {
                try {
                    T value = mapper.readValue(jsonText, type);
                    singleSubscriber.onSuccess(value);
                } catch (IOException e) {
                    singleSubscriber.onError(e);
                }
            }
        });
    }

    public <T> Single<T> readValue(final InputStream inputStream, final Class<T> type) {
        return Single.create(new Single.OnSubscribe<T>() {
            @Override
            public void call(SingleSubscriber<? super T> singleSubscriber) {
                try {
                    T value = mapper.readValue(inputStream, type);
                    singleSubscriber.onSuccess(value);
                } catch (IOException e) {
                    singleSubscriber.onError(e);
                }
            }
        });
    }
}
