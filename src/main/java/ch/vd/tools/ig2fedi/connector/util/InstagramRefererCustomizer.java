package ch.vd.tools.ig2fedi.connector.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;

@RequiredArgsConstructor(staticName = "of")
public final class InstagramRefererCustomizer<T extends ClientHttpRequest> implements RestTemplateRequestCustomizer<T> {

    private static final String REFERER_BASE = "https://www.instagram.com/%s/";

    private final String referer;

    @Override
    public void customize(T request) {
        request.getHeaders().add(HttpHeaders.REFERER, REFERER_BASE.formatted(referer));
    }
}
