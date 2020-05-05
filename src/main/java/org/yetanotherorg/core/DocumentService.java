package org.yetanotherorg.core;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import org.yetanotherorg.api.Document;
import org.yetanotherorg.core.entity.FileWrapper;
import org.yetanotherorg.core.exceptions.DocumentServiceException;
import org.yetanotherorg.core.files.FileRepository;
import org.yetanotherorg.core.files.FileRepositoryException;
import org.yetanotherorg.core.metadata.MetaDataRepository;
import org.yetanotherorg.core.metadata.MetaDataRepositoryException;

public class DocumentService {

    private final FileRepository fileRepo;
    private final MetaDataRepository metaDataRepo;

    public DocumentService(final FileRepository fileRepo, final MetaDataRepository metaDataRepo) {
        this.fileRepo = fileRepo;
        this.metaDataRepo = metaDataRepo;
    }

    public Stream<Document> find(String category) {
        final Stream<Document> result;
        if (isEmpty(category)) {
            result = metaDataRepo.findAll();
        } else {
            result = metaDataRepo.findByCategory(formatCategory(category));
        }
        return result;
    }

    public Document insert(InputStream inputStream, String fileName, String categories) {
        final Document result;
        if (isEmpty(fileName)) {
            throw new IllegalArgumentException("FileName is null or empty");
        }
        if (isEmpty(categories)) {
            throw new IllegalArgumentException("Categories is is null or empty");
        }
        try {
            String fileId = fileRepo.write(inputStream);
            result = metaDataRepo.store(fileName, formatCategories(categories), fileId);
        } catch (FileRepositoryException | MetaDataRepositoryException e) {
            throw new DocumentServiceException(e.getMessage(), e.getCause());
        }
        return result;
    }

    public FileWrapper findFileById(String fileId) {
        if (isEmpty(fileId)) {
            throw DocumentServiceException.fileIdInvalid(fileId);
        }
        File rawFile =  fileRepo.get(fileId);
        Document document = metaDataRepo.fetch(fileId);
        return new FileWrapper(rawFile, document.getFileName());
    }

    //Could be also in a utility class
    private Set<String> formatCategories(final String categories) {
        return Arrays.stream(categories.trim().split(","))
                .map(this::formatCategory)
                .collect(toSet());
    }

    private String formatCategory(final String category) {
        return category.trim().toLowerCase();
    }

}
