package ch.vd.tools.ig2fedi.connector.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public final class UserInfo {

    private final String id;
    private final String fbid;
    private final @JsonProperty("eimu_id") String eimuId;
}
