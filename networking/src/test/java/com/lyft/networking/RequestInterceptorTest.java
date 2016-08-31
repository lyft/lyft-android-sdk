package com.lyft.networking;

import com.lyft.networking.apis.LyftPublicApi;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RequestInterceptorTest {

    private static final String CLIENT_TOKEN = "client_token";

    @Mock
    Interceptor.Chain chain;

    private RequestInterceptor requestInterceptor;
    private ArgumentCaptor<Request> argumentCaptor;

    @Before
    public void setup() {
        requestInterceptor = new RequestInterceptor(CLIENT_TOKEN);
        chain = mock(Interceptor.Chain.class);
        argumentCaptor = ArgumentCaptor.forClass(Request.class);
        Request request = new Request.Builder().url(LyftPublicApi.API_ROOT).build();
        when(chain.request()).thenReturn(request);
    }

    @Test
    public void interceptTest_authHeader() throws IOException {
        requestInterceptor.intercept(chain);

        verify(chain).proceed(argumentCaptor.capture());
        Request requestWithHeaders = argumentCaptor.getValue();

        String authHeader = requestWithHeaders.header(RequestInterceptor.AUTHORIZATION_HEADER);
        assertEquals("Bearer " + CLIENT_TOKEN, authHeader);
    }

    @Test
    public void interceptTest_userAgentHeader() throws IOException {
        requestInterceptor.intercept(chain);

        verify(chain).proceed(argumentCaptor.capture());
        Request requestWithHeaders = argumentCaptor.getValue();

        String userAgentHeader = requestWithHeaders.header(RequestInterceptor.USER_AGENT_HEADER);
        assertTrue(userAgentHeader != null && !userAgentHeader.isEmpty());
    }
}
