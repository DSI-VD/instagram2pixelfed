package ch.vd.tools.ig2fedi.connector.instagram.model;

import ch.vd.tools.ig2fedi.connector.instagram.model.edge.MediaToCaptionEdge;
import ch.vd.tools.ig2fedi.connector.instagram.model.edge.SidecarToChildrenEdge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;

@Data
@Builder
@Jacksonized
public final class PostInfo {

    private final String id;
    private final String shortcode;
    private final @JsonProperty("__typename") String type;
    private final @JsonProperty("is_video") boolean video;
    private final @JsonProperty("display_url") String displayUrl;
    private final @Nullable @JsonProperty("video_url") String videoUrl;
    private final @JsonUnwrapped MediaToCaptionEdge caption;
    private final @Nullable @JsonUnwrapped SidecarToChildrenEdge sidecarChildren;
}
