package org.yetanotherorg.core.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileRepositoryLocal implements FileRepository {

    private final Path rootFilePath;

    public FileRepositoryLocal(final Path rootFilePath) {
        this.rootFilePath = rootFilePath;
    }

    @Override public String write(final InputStream inputStream) throws FileRepositoryException {
        if (inputStream == null) {
            throw FileRepositoryException.noInputStream();
        }

        UUID fileId = UUID.randomUUID();
        try {
            final Path targetPath = rootFilePath.resolve(fileId.toString());
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw FileRepositoryException.ioError(e);
        }
        return fileId.toString();
    }

    @Override public File get(final String fileId) {
        return rootFilePath.resolve(fileId).toFile();
    }
}
