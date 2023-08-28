package ch.vd.tools.ig2fedi.connector.instagram.model.edge;

import ch.vd.tools.ig2fedi.connector.instagram.model.TextNode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public final class MediaToCaptionEdge {

    private final @JsonProperty("edge_media_to_caption") EdgeResult<TextNode> edgeResult;
}
