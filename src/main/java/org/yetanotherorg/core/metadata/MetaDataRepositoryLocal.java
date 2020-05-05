package org.yetanotherorg.core.metadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yetanotherorg.api.Document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MetaDataRepositoryLocal implements MetaDataRepository {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final String META_FILE_NAME = "metadata.json";
    private static final Logger LOG = LoggerFactory.getLogger(MetaDataRepositoryLocal.class);

    protected final Map<String, Document> documentMap;
    protected final File metaFile;

    public MetaDataRepositoryLocal(final Path rootFilePath) {
        this.metaFile = rootFilePath.resolve(META_FILE_NAME).toFile();
        documentMap = readMetaFile();
    }

    @Override public Stream<Document> findAll() {
        return documentMap.values().stream();
    }

    @Override public Stream<Document> findByCategory(final String category) {
        return documentMap.values().stream()
                .filter(document -> document.getCategories().contains(category));
    }

    @Override public Document store(final String fileName, final Set<String> categories, final String fileId)
            throws MetaDataRepositoryException {
        final Document document = new Document(categories, fileId, fileName);
        documentMap.put(fileId, document);
        updateMetaFile();
        return document;
    }

    @Override public Document fetch(final String fileId) {
        return documentMap.get(fileId);
    }

    protected Map<String, Document> readMetaFile() {
        try {
            if(!metaFile.createNewFile()){
                LOG.info("Found Meta data File - parsing json!");
                return JSON_MAPPER.readValue(metaFile, new TypeReference<>() {

                });
            }else{
                LOG.info("Meta data File created!");
                return new HashMap<>();
            }
        } catch (IOException e) {
            LOG.error("Error while operating on meta data file", e);
            return new HashMap<>();
        }
    }

    protected void updateMetaFile() throws MetaDataRepositoryException {
        try {
            JSON_MAPPER.writeValue(metaFile, documentMap);
        } catch (IOException e) {
            throw MetaDataRepositoryException.ioErrorWrite(metaFile, e);
        }
    }
}
