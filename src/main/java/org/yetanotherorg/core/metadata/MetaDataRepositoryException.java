package org.yetanotherorg.core.metadata;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;

public class MetaDataRepositoryException extends Exception {

    public MetaDataRepositoryException(final String msg, final IOException e) {
        super(msg, e);
    }

    public static MetaDataRepositoryException ioErrorRead(final File file, final IOException e) {
        return new MetaDataRepositoryException(format("IO Error while reading meta data file - fileName: %s", file.getName()), e);
    }

    public static MetaDataRepositoryException ioErrorWrite(final File file, final IOException e) {
        return new MetaDataRepositoryException(format("IO Error while writing to meta data file - fileName: %s", file.getName()), e);
    }
}
