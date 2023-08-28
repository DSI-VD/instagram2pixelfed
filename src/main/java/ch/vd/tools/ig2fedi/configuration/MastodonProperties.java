package ch.vd.tools.ig2fedi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "mastodon")
public record MastodonProperties(
        /** Mastodon instance URL */
        String instance,
        /** Mastodon token */
        String accessToken,
        /** Mastodon carousel max size */
        @DefaultValue("4")
        int maxCarouselSize,
        /** Mastodon content language */
        @DefaultValue("fr")
        String contentLanguage,
        /** Mastodon cross-post cache file */
        @DefaultValue("/tmp/mastodon.cache")
        Path cacheFile
) {
}
