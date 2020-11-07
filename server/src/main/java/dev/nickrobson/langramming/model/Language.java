package dev.nickrobson.langramming.model;

import lombok.Builder;

@Builder
public class Language {
    public final long id;
    public final String code;
    public final String name;
}
