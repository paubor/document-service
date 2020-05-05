package org.yetanotherorg.api;

import java.io.File;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Document {

    private final String fileId;
    private final Set<String> categories;
    private final String fileName;

    @JsonCreator
    public Document(
            @JsonProperty("categories") final Set<String> categories,
            @JsonProperty("fileId") final String uuid,
            @JsonProperty("fileName") final String fileName) {
        this.categories = categories;
        this.fileId = uuid;
        this.fileName = fileName;
    }


    public Set<String> getCategories() {
        return categories;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }
}
