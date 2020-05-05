package org.yetanotherorg.health;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.codahale.metrics.health.HealthCheck;

public class FilePathHealthCheck extends HealthCheck {

    private final Path filePath;

    public FilePathHealthCheck(final Path filePath) {
        this.filePath = filePath;
    }

    @Override protected Result check() throws Exception {
        if(Files.exists(filePath)){
            return Result.healthy();
        }else{
            return Result.unhealthy("The configured filePath is unreachable");
        }
    }
}
