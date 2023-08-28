package ch.vd.tools.ig2fedi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "instagram")
public record InstagramProperties(
        /** The username for the account to sync */
        String targetUsername,
        /** A compatible user agent */
        @DefaultValue("Instagram 244.0.0.17.110 Android")
        String userAgent,
        /** The number of posts to fetch */
        @DefaultValue("5")
        int postCount
) {
}
