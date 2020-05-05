package org.yetanotherorg.core.files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

import org.assertj.core.internal.InputStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.google.common.io.CharSource;

class FileRepositoryLocalTest {

    private static final File DOC = loadFileFromResource("document.docx");
    private static final File PDF = loadFileFromResource("document1.pdf");
    private static final File PNG = loadFileFromResource("redis-logo.png");

    @TempDir
    public Path tempDir;
    private FileRepositoryLocal sut;

    @BeforeEach
    void setUp() {
        sut = new FileRepositoryLocal(tempDir);
    }

    @Test
    void write() throws FileNotFoundException {
        try {
            final String result = sut.write(new FileInputStream(DOC));
            assertThat(result).isNotNull();
        } catch (FileRepositoryException e) {
            fail(e);
        }
    }

    @Test
    void writeIOException() throws IOException {
        //Not really sure how to test this
        InputStream mockedInputStream = mock(InputStream.class);
        when(mockedInputStream.transferTo(any())).thenThrow(new IOException("bla"));
        assertThatExceptionOfType(FileRepositoryException.class)
                .isThrownBy(() -> sut.write(mockedInputStream));
    }

    @Test
    void writeFileInputStreamNull() {
        assertThatExceptionOfType(FileRepositoryException.class)
                .isThrownBy(() -> sut.write(null));
    }

    @Test
    void get() {
        final String fileId = fillWith(DOC);
        final File file = sut.get(fileId);
        assertThat(file).hasSameBinaryContentAs(DOC);
    }

    private static File loadFileFromResource(final String fileName) {
        return new File(Objects.requireNonNull(FileRepositoryLocalTest.class.getClassLoader().getResource(fileName)).getFile());
    }

    private String fillWith(final File file) {
        try {
            return sut.write(new FileInputStream(file));
        } catch (FileRepositoryException | FileNotFoundException e) {
            fail(e);
        }
        return "";
    }
}