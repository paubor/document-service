package org.yetanotherorg.core.entity;

import java.io.File;

public class FileWrapper {
    private final File file;
    private final String originalFileName;

    public FileWrapper(final File file, final String originalFileName) {
        this.file = file;
        this.originalFileName = originalFileName;
    }

    public File getFile() {
        return file;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }
}
