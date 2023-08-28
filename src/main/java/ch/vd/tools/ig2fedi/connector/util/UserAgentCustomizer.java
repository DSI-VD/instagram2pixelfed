package ch.vd.tools.ig2fedi.connector.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;

@RequiredArgsConstructor(staticName = "of")
public final class UserAgentCustomizer<T extends ClientHttpRequest> implements RestTemplateRequestCustomizer<T> {

    private final String userAgent;

    @Override
    public void customize(T request) {
        request.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
    }
}
