package org.yetanotherorg.core.files;

import java.io.IOException;

public class FileRepositoryException extends Exception {

    public FileRepositoryException(final String msg) {
        super(msg);
    }

    public FileRepositoryException(final String msg, final IOException e) {
        super(msg, e);
    }

    public static FileRepositoryException noInputStream() {
        return new FileRepositoryException("InputStream was null");
    }

    public static FileRepositoryException ioError(final IOException e) {
        return new FileRepositoryException("An IO error occurred", e);
    }

}
