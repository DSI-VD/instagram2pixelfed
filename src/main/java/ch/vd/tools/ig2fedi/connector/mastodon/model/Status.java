package ch.vd.tools.ig2fedi.connector.mastodon.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;

@Data
@Builder
@Jacksonized
public final class Status {

    private final String url;
    private final String title;
    private final String description;
    private final @Nullable String image;
}
