package org.yetanotherorg.core.metadata;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.yetanotherorg.core.files.FileRepositoryException;

class MetaDataRepositoryExceptionTest {

    @Test
    void ioErrorRead() {
        final MetaDataRepositoryException sut = MetaDataRepositoryException.ioErrorRead(new File("filename1"), new IOException("bad boy"));
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(sut.getMessage()).isEqualTo("IO Error while reading meta data file - fileName: filename1");
        softly.assertThat(sut.getCause()).isExactlyInstanceOf(IOException.class).hasMessage("bad boy");
        softly.assertAll();
    }

    @Test
    void ioErrorWrite() {
        final MetaDataRepositoryException sut = MetaDataRepositoryException.ioErrorWrite(new File("filename1"), new IOException("bad boy"));
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(sut.getMessage()).isEqualTo("IO Error while writing to meta data file - fileName: filename1");
        softly.assertThat(sut.getCause()).isExactlyInstanceOf(IOException.class).hasMessage("bad boy");
        softly.assertAll();
    }
}