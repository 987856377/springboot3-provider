package com.springboot.provider.module.common.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Map;

public interface FileService {
    Map<String, Path> uploadSingle(MultipartFile file);
    Map<String, Path> uploadMultipart(MultipartFile[] files);

    Map<String, Path> list();

    ResponseEntity<byte[]> download(String filename) throws IOException;
    ResponseEntity<Resource> downloadFile(String filename) throws MalformedURLException;

    ResponseEntity<byte[]> downloadZipFile(String filePath) throws IOException;

    ResponseEntity<Resource> downloadPDFFile(String id) throws Exception;
}
