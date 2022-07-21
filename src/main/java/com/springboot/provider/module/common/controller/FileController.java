package com.springboot.provider.module.common.controller;

import com.springboot.provider.common.ResultCode;
import com.springboot.provider.common.ResultJson;
import com.springboot.provider.module.common.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Map;

/**
 * @Description
 * @Project development
 * @Package com.spring.development.module.user.controller
 * @Author xuzhenkui
 * @Date 2019/9/19 9:33
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "/uploadSingle", method = RequestMethod.POST)
    public ResultJson singleFileUpload(MultipartFile file) {
        Map<String, Path> map = fileService.uploadSingle(file);
        if (map.size() == 0){
            return ResultJson.failure(ResultCode.INTERNAL_SERVER_ERROR);
        }
        return ResultJson.success(map);
    }

    @RequestMapping(value = "/uploadMultipart", method = RequestMethod.POST)
    public ResultJson mulFileUpload(MultipartFile[] files) {
        Map<String, Path> map = fileService.uploadMultipart(files);
        if (map.size() == 0){
            return ResultJson.failure(ResultCode.INTERNAL_SERVER_ERROR);
        }
        return ResultJson.success(map);
    }

    @RequestMapping(value = "list",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson downloadFileList() {
        return ResultJson.success(fileService.list());
    }

    @RequestMapping(value = "download",method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@RequestParam("filename") String filename) {
        ResponseEntity<byte[]> responseEntity = null;
        try {
            responseEntity = fileService.download(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseEntity;
    }

    @RequestMapping(value = "downloadZipFile",method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadZipFile(@RequestParam("filePath") String filePath) {
        ResponseEntity<byte[]> responseEntity = null;
        try {
            responseEntity = fileService.downloadZipFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseEntity;
    }

    @RequestMapping(value = "downloadFile",method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) {
        ResponseEntity<Resource> responseEntity = null;
        try {
            responseEntity = fileService.downloadFile(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return responseEntity;
    }

    @RequestMapping("/downloadPDFFile")
    public ResponseEntity<Resource> downloadPDFFile(@RequestParam("id") String id) {
        ResponseEntity<Resource> responseEntity = null;
        try {
            responseEntity = fileService.downloadPDFFile(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseEntity;
    }
}
