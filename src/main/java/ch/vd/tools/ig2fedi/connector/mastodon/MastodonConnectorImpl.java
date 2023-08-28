package ch.vd.tools.ig2fedi.connector.mastodon;

import ch.vd.tools.ig2fedi.configuration.MastodonProperties;
import ch.vd.tools.ig2fedi.connector.mastodon.model.MediaAttachement;
import ch.vd.tools.ig2fedi.connector.mastodon.model.Status;
import ch.vd.tools.ig2fedi.connector.util.BearerTokenCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.WRITE;

@Slf4j
@Component
public class MastodonConnectorImpl implements MastodonConnector {

    private final MastodonProperties mastodonProperties;
    private final RestTemplate rest;
    private final RestTemplate mastodon;

    public MastodonConnectorImpl(
            RestTemplateBuilder builder,
            MastodonProperties mastodonProperties
    ) {
        this.mastodonProperties = mastodonProperties;
        this.rest = builder.build();
        this.mastodon = builder
                .rootUri(mastodonProperties.instance())
                .requestCustomizers(
                        BearerTokenCustomizer.of(mastodonProperties.accessToken())
                )
                .build();
    }

    @Override
    public MediaAttachement uploadMedia(String mediaUrl) {
        log.info("Uploading {} to mastodon", mediaUrl);
        var file = fetchFile(mediaUrl);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        var multipart = new LinkedMultiValueMap<String, Object>();
        var contentType = new HttpHeaders();
        contentType.setContentType(file.mediaType());
        multipart.add("file", new HttpEntity<>(
                new FileSystemResource(file.tempFile()),
                contentType
        ));

        return mastodon.exchange(
                "/api/v1/media",
                HttpMethod.POST,
                new HttpEntity<>(multipart, headers),
                MediaAttachement.class
        ).getBody();
    }

    @Override
    public Status publishPost(String title, List<String> mediaUrls) {
        log.info("Publishing new mastodon post");
        var attachments = mediaUrls.stream()
                .map(this::uploadMedia)
                .map(MediaAttachement::getId)
                .toList();

        var headers = new HttpHeaders();
        headers.add("Idempotency-Key", UUID.nameUUIDFromBytes(title.getBytes()).toString());

        var params = new LinkedMultiValueMap<String, String>();
        params.add("status", title);
        params.add("visibility", "public");
        params.add("language", mastodonProperties.contentLanguage());
        params.addAll("media_ids[]", attachments);

        return mastodon.exchange(
                "/api/v1/statuses",
                HttpMethod.POST,
                new HttpEntity<>(params, headers),
                Status.class
        ).getBody();
    }

    private FileData fetchFile(String mediaUrl) {
        var headers = rest.headForHeaders(mediaUrl);
        var contentType = headers.getContentType();
        return rest.execute(mediaUrl, HttpMethod.GET, null, response -> {
            var file = Files.createTempFile("ig-downloads-", ".tmp");
            StreamUtils.copy(response.getBody(), Files.newOutputStream(file, WRITE));
            return new FileData(contentType, file);
        });
    }

    private record FileData(
            MediaType mediaType,
            Path tempFile
    ) {
    }
}
