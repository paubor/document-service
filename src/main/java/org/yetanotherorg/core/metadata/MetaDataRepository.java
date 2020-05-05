package org.yetanotherorg.core.metadata;

import java.util.Set;
import java.util.stream.Stream;

import org.yetanotherorg.api.Document;

public interface MetaDataRepository {

    /**
     * Returns all Documents
     *
     * @return a stream of documents
     */
    Stream<Document> findAll();

    /**
     * Returns all Documents matching the given category
     *
     * @param formatCategory the category to search for
     * @return a stream of documents
     */
    Stream<Document> findByCategory(String formatCategory);

    /**
     * Store a document.
     *
     * @param fileName the name of the file
     * @param categories a set of categories to associate to this file
     * @param fileId the id of the file
     * @return the stored document.
     * @throws MetaDataRepositoryException if there was an error during storing
     */
    Document store(String fileName, Set<String> categories, String fileId) throws MetaDataRepositoryException;

    /**
     * Fetches a document.
     *
     * @param fileId the id of the file
     * @return the document for the given fileId
     */
    Document fetch(String fileId);
}
