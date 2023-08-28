package ch.vd.tools.ig2fedi.connector.mastodon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;

@Data
@Builder
@Jacksonized
public final class MediaAttachement {

    private final String id;
    private final Type type;
    private final String url;
    private final @Nullable String remoteUrl;
    private final String previewUrl;
    private final @Nullable String textUrl;
    private final @Nullable String description;
    private final @Nullable String blurhash;

    public enum Type {
        @JsonProperty("audio")
        AUDIO,
        @JsonProperty("image")
        IMAGE,
        @JsonProperty("video")
        VIDEO,
        @JsonProperty("gifv")
        GIFV,
        @JsonProperty("unknown")
        UNKNOWN,
    }
}
