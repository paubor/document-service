package org.yetanotherorg.core.files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class FileRepositoryExceptionTest {

    @Test
    void noInputStream() {
        assertThat(FileRepositoryException.noInputStream().getMessage())
                .isEqualTo("InputStream was null");
    }

    @Test
    void ioError() {
        final FileRepositoryException sut = FileRepositoryException.ioError(new IOException("bad boy"));
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(sut.getMessage()).isEqualTo("An IO error occurred");
        softly.assertThat(sut.getCause()).isExactlyInstanceOf(IOException.class).hasMessage("bad boy");
        softly.assertAll();
    }
}