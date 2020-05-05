package org.yetanotherorg.core.files;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

public interface FileRepository {

    /**
     * Writes a given input stream to a predefined location (implementation dependent).
     * Generates and returns a UUID associated with this file.
     *
     * @param inputStream The {@see InputStream} associated with a file.
     * @return A unique file id as string
     */
    String write(InputStream inputStream) throws FileRepositoryException;

    /**
     * Returns a file given a file id.
     *
     * @param fileId A unique file id
     * @return a file
     */
    File get(String fileId);
}
