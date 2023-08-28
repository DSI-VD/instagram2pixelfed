package ch.vd.tools.ig2fedi.connector.instagram;

import ch.vd.tools.ig2fedi.configuration.InstagramProperties;
import ch.vd.tools.ig2fedi.connector.instagram.model.ApiResult;
import ch.vd.tools.ig2fedi.connector.instagram.model.PostInfo;
import ch.vd.tools.ig2fedi.connector.instagram.model.UserInfo;
import ch.vd.tools.ig2fedi.connector.instagram.model.UserResult;
import ch.vd.tools.ig2fedi.connector.instagram.model.edge.EdgeResult;
import ch.vd.tools.ig2fedi.connector.instagram.model.edge.OwnerToTimelineMediaEdge;
import ch.vd.tools.ig2fedi.connector.util.InstagramRefererCustomizer;
import ch.vd.tools.ig2fedi.connector.util.UserAgentCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
class InstagramClientImpl implements InstagramClient {

    private static final String POST_REQ_HASH = "003056d32c2554def87228bc3fd9668a";
    private static final String GRAPHQL_ENDPOINT = "/graphql/query/?query_hash={queryId}&variables={variables}";

    private final RestTemplate restTemplate;

    public InstagramClientImpl(
            RestTemplateBuilder builder,
            InstagramProperties properties
    ) {
        this.restTemplate = builder
                .rootUri("https://www.instagram.com")
                .requestCustomizers(
                        UserAgentCustomizer.of(properties.userAgent()),
                        InstagramRefererCustomizer.of(properties.targetUsername())
                )
                .build();
    }

    @Override
    @Cacheable("instagram_account-key")
    public String getAccountKey(String username) {
        log.info("Fetching account key for {}", username);
        return Objects.requireNonNull(
                this.restTemplate.exchange(
                        "/api/v1/users/web_profile_info/?username={username}",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<ApiResult<UserResult<UserInfo>>>() {
                        },
                        username
                ).getBody()
        ).getData().getUser().getId();
    }

    @Override
    public List<PostInfo> getLastPosts(String accountKey, int count) {
        Assert.isTrue(count < 50, "Count shouldn't exceed 50 posts at a time");

        log.info("Fetching last {} posts for {}", count, accountKey);
        return Objects.requireNonNull(
                this.restTemplate.exchange(
                        GRAPHQL_ENDPOINT,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<ApiResult<UserResult<OwnerToTimelineMediaEdge>>>() {
                        },
                        POST_REQ_HASH,
                        "{\"id\": \"%s\", \"first\": %d}".formatted(accountKey, count)
                ).getBody()
        ).getData().getUser().getEdgeResult().getEdges().stream().map(EdgeResult.Edge::getNode).toList();
    }
}
