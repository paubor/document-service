package org.yetanotherorg.core.exceptions;

import static java.lang.String.format;

public class DocumentServiceException extends RuntimeException {

    public DocumentServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DocumentServiceException(final String message) {
        super(message);
    }

    public static DocumentServiceException fileIdInvalid(final String fileId) {
        return new DocumentServiceException(format("The fileId is invalid - fileId: %s", fileId));
    }
}
