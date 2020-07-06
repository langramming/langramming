package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

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
