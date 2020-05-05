package org.yetanotherorg;

import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class DocumentApplicationConfig extends Configuration {

    private final Path filePath;

    @JsonCreator
    public DocumentApplicationConfig(
            @JsonProperty("fileLocation") final Path filePath) {
        this.filePath = filePath;
    }

    public Path getFilePath() {
        return filePath;
    }
}
