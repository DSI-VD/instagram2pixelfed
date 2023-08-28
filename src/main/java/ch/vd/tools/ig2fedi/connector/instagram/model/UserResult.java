package ch.vd.tools.ig2fedi.connector.instagram.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public final class UserResult<T> {

    private final T user;
}
