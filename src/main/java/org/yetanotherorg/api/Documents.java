package org.yetanotherorg.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Documents {

    private List<Document> data;

    @JsonCreator
    public Documents(@JsonProperty("data") final List<Document> data) {
        this.data = data;
    }

    public List<Document> getData() {
        return data;
    }
}
