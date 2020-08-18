package com.github.langramming.database.model.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Urls {
    @Column(name = "urls_url")
    public String url;

    @Column(name = "urls_preview")
    public String preview;
}
