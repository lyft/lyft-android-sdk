package com.lyft.networking.internal;

import com.lyft.networking.apiObjects.internal.Validatable;
import com.lyft.networking.exceptions.PartialResponseException;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class NullCheckErrorConverter extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);

        return new Converter<ResponseBody, Object>() {
            @Override
            public Object convert(ResponseBody value) throws IOException {
                Object result = delegate.convert(value);

                if (result instanceof Validatable) {
                    Validatable validatable = (Validatable) result;
                    if (!validatable.isValid()) {
                        throw new PartialResponseException(result);
                    }
                }

                return result;
            }
        };
    }
}
