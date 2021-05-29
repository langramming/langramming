package dev.nickrobson.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Builder
@JsonAutoDetect
public class PageInfoDTO<T> {

    @JsonProperty("first")
    public final Optional<String> first;

    @JsonProperty("last")
    public final Optional<String> last;

    @JsonProperty("total")
    public final int total;

    @JsonProperty("items")
    public final List<T> items;
}
