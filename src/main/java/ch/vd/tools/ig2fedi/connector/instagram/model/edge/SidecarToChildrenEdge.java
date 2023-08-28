package ch.vd.tools.ig2fedi.connector.instagram.model.edge;

import ch.vd.tools.ig2fedi.connector.instagram.model.PostInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public final class SidecarToChildrenEdge {

    private final @JsonProperty("edge_sidecar_to_children") EdgeResult<PostInfo> edgeResult;
}
