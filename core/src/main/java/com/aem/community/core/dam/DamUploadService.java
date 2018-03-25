package com.aem.community.core.dam;

import java.io.IOException;
import java.io.InputStream;
import javax.jcr.RepositoryException;

/**
 * DAM Upload Service interface definition
 *
 */
public interface DamUploadService {
    boolean uploadAsset(String fileName, String contentType, String folderPath, InputStream fileInputStream) throws IOException, RepositoryException;
}
