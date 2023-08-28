package ch.vd.tools.ig2fedi.connector.instagram.model.edge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public final class EdgeResult<T> {

    private final Long count;
    private final @JsonProperty("page_info") PageInfo pageInfo;
    private final List<Edge<T>> edges;

    @Data
    @Builder
    @Jacksonized
    public static final class PageInfo {
        private final @JsonProperty("has_next_page") boolean hasNextPage;
        private final @JsonProperty("end_cursor") String endCursor;
    }

    @Data
    @Builder
    @Jacksonized
    public static final class Edge<T> {
        private final T node;
    }
}
