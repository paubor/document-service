package org.yetanotherorg.core.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.yetanotherorg.api.Document;

class MetaDataRepositoryLocalTest {

    @TempDir
    public Path tempDir;
    private MetaDataRepositoryLocal sut;

    @BeforeEach
    void setUp() {
        sut = new MetaDataRepositoryLocal(tempDir);
    }

    @Test
    void findAll() {
        try {
            storeDummyDocuments();
        } catch (MetaDataRepositoryException e) {
            fail(e);
        }
        assertThat(sut.findAll()).hasSize(3);
    }

    @Test
    void findByCategory() {
        try {
            storeDummyDocuments();
        } catch (MetaDataRepositoryException e) {
            fail(e);
        }
        assertThat(sut.findByCategory("a")).hasSize(2);
        assertThat(sut.findByCategory("b")).hasSize(2);
        assertThat(sut.findByCategory("c")).hasSize(1);
        assertThat(sut.findByCategory("x")).isEmpty();
        assertThat(sut.findByCategory("")).isEmpty();
    }

    @Test
    void store() {

        try {
            final Document doc = sut.store("file1", Set.of("a"), "fileId1");
        } catch (MetaDataRepositoryException e) {
            fail(e);
        }
        assertThat(sut.documentMap).hasSize(1);
    }

    @Test
    void fetch() {
        try {
            storeDummyDocuments();
        } catch (MetaDataRepositoryException e) {
            fail(e);
        }
        final Document document = sut.fetch("fileId1");
        assertThat(document).isNotNull();
        assertThat(document.getFileName()).isEqualTo("file1");
    }

    private void storeDummyDocuments() throws MetaDataRepositoryException {
        sut.store("file1", Set.of("a"), "fileId1");
        sut.store("file2", Set.of("a", "b"), "fileId2");
        sut.store("file3", Set.of("c", "b"), "fileId3");
    }
}