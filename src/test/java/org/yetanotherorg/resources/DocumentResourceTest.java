package org.yetanotherorg.resources;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.assertj.core.api.SoftAssertions;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yetanotherorg.api.Document;
import org.yetanotherorg.api.Documents;
import org.yetanotherorg.core.DocumentService;
import org.yetanotherorg.core.entity.FileWrapper;

@ExtendWith(MockitoExtension.class)
class DocumentResourceTest {

    @Mock
    private DocumentService service;
    private DocumentResource sut;

    private static final File DOCUMENT = loadFileFromResource("document.docx");

    @BeforeEach
    void setUp() {
        sut = new DocumentResource(service);
    }

    private static File loadFileFromResource(final String fileName) {
        return new File(Objects.requireNonNull(DocumentResourceTest.class.getClassLoader().getResource(fileName)).getFile());
    }

    @Nested
    class Upload {

        @Test
        void succeeds() throws IOException {
            final String validUUID = UUID.randomUUID().toString();
            lenient().when(service.insert(any(), any(), any()))
                    .thenReturn(new Document(Set.of("cat1", "cat2"), validUUID, "userFileName"));

            final Document document = sut
                    .uploadFile(new FileInputStream(DOCUMENT),
                            FormDataContentDisposition.name("file").fileName("fileNameDocument").build(),
                            "document");

            verify(service).insert(any(), any(), any());
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(document.getFileId()).as("fileId").isEqualTo(validUUID);
            softly.assertThat(document.getFileName()).as("fileName").isEqualTo("userFileName");
            softly.assertThat(document.getCategories()).as("categories").containsOnly("cat1", "cat2");
            softly.assertAll();
        }

        @Test
        void fails() {
            when(service.insert(any(), any(), any())).thenThrow(new WebApplicationException("blabla"));

            assertThatExceptionOfType(WebApplicationException.class)
                    .isThrownBy(() -> sut.uploadFile(new FileInputStream(DOCUMENT),
                            FormDataContentDisposition.name("test").fileName("fileNameDocument")
                                    .build(),
                            "document"))
                    .withMessage("blabla");

            verify(service).insert(any(), any(), any());
        }
    }

    @Nested
    class Download {

        @Test
        void file() {
            String validFileId = UUID.randomUUID().toString();
            when(service.findFileById(validFileId))
                    .thenReturn(new FileWrapper(DOCUMENT, "validFileName.doc"));

            final Response byId = sut.getById(validFileId);

            verify(service).findFileById(anyString());
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(byId.getStatus()).as("response status").isEqualTo(200);
            softly.assertThat(byId.getEntity()).as("response payload").isEqualTo(DOCUMENT);
            softly.assertThat(byId.getStringHeaders()).as("headers")
                    .flatExtracting("Content-Disposition")
                    .contains("attachment;filename=validFileName.doc");
            softly.assertAll();
        }
    }

    @Nested
    class Query {

        @Test
        void all() {
            when(service.find("")).thenReturn(getDocumentStream());

            final Documents documents = sut.get("");

            verify(service).find(anyString());
            assertThat(documents.getData()).hasSize(3);
        }

        @Test
        void byCategoryA() {
            when(service.find("a")).thenReturn(getDocumentStream());

            final Documents documentsA = sut.get("a");

            verify(service).find(eq("a"));
            assertThat(documentsA.getData()).hasSize(3);
        }
    }

    private Stream<Document> getDocumentStream() {
        return Stream.of(
                new Document(Set.of("a"), "1", "fileName1"),
                new Document(Set.of("a"), "2", "fileName2"),
                new Document(Set.of("a"), "3", "fileName3"));
    }

}