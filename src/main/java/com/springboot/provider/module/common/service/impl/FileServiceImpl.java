package com.springboot.provider.module.common.service.impl;

import cn.hutool.core.util.ZipUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import com.springboot.provider.module.common.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    @Value("${spring.servlet.multipart.location}")
    private String location;

    private static final String EMPTY = "";
    private static final String PDF = ".pdf";

    private final ObjectMapper objectMapper;

    public FileServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public Map<String, Path> uploadSingle(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (file.isEmpty() || fileName.contains("..")) {
            return null;
        }

        Map<String, Path> map = new HashMap<>();
        // Get the file and save it somewhere
        Path path = Paths.get(location + File.separator + fileName);
        try {
            // write
            Path write = Files.write(path, file.getBytes());

            // copy
//            InputStream inputStream = file.getInputStream();
//            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
//            inputStream.close();

            map.put(fileName, write);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, Path> uploadMultipart(MultipartFile[] files) {
        Map<String, Path> map = new HashMap<>();

        for (MultipartFile file : files) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            if (file.isEmpty() || fileName.contains("..")) {
                continue;
            }

            // Get the file and save it somewhere
            Path path = Paths.get(location + File.separator + fileName);
            try {
                // write
                Path write = Files.write(path, file.getBytes());

                // copy
//                InputStream inputStream = file.getInputStream();
//                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
//                inputStream.close();

                map.put(fileName, write);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public Map<String, Path> list() {
        Map<String, Path> map = new HashMap<>();

        File files = new File(location);
        if (files.exists()) {
            File[] list = files.listFiles();

            for (File file : list) {
                map.put(file.getName(), Paths.get(file.getAbsolutePath()));
            }

//            Stream<Path> stream = null;
//            try {
//                stream = Files.list(Paths.get(location));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            assert stream != null;
//            stream.forEach(file -> {
//                String s = file.getFileName().toString();
//                map.put(s, file.getFileName());
//            });
        }
        return map;
    }

    @Override
    public ResponseEntity<byte[]> download(String filename) throws IOException {
        if (!StringUtils.hasText(filename) || filename.contains("..")) {
            return null;
        }
        File file = new File(location + File.separator + filename);

        if (file.exists()) {
            HttpHeaders headers = new HttpHeaders();
            filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(Files.readAllBytes(file.toPath()), headers, HttpStatus.CREATED);
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> downloadZipFile(String filePath) throws IOException {
        if (!StringUtils.hasText(filePath) || filePath.contains("..")) {
            return null;
        }
        File file = new File(filePath);

        if (file.exists()) {
            File zip = ZipUtil.zip(file);

            HttpHeaders headers = new HttpHeaders();
            String attachment = new String(zip.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

            headers.setContentDispositionFormData("attachment", attachment);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(Files.readAllBytes(zip.toPath()), headers, HttpStatus.CREATED);
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String filename) throws MalformedURLException {
        if (!StringUtils.hasText(filename) || filename.contains("..")) {
            return null;
        }
        Path path = Paths.get(location + File.separator + filename);
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists()) {
            filename = new String(Objects.requireNonNull(resource.getFilename()).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"")
                    .body(resource);
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> downloadPDFFile(String id) throws Exception {
        String json = "{\n" +
                "                \"inspectId\":\"20220102DCK0255\",\n" +
                "                \"inspectName\":\"新冠核酸检测(10混1)+新冠核酸检测\",\n" +
                "                \"inspectTime\":\"2022-01-02 10:49:10\",\n" +
                "                \"inspectStatus\":\"3\",\n" +
                "                \"reportTime\":\"2022-01-02 14:32:27\",\n" +
                "                \"examTime\":\"2022-01-02 11:32:44\",\n" +
                "                \"departmentCode\":\"1497\",\n" +
                "                \"departmentName\":\"发热门诊\",\n" +
                "                \"doctorCode\":\"\",\n" +
                "                \"doctorName\":\"侯敬轩\",\n" +
                "                \"executeDepartmentCode\":\"1385\",\n" +
                "                \"executeDepartmentName\":\"医学检验中心(东)\",\n" +
                "                \"reporter\":\"王梦飞\",\n" +
                "                \"auditor\":\"\",\n" +
                "                \"hospitalName\":\"中心医院\",\n" +
                "                \"patientName\":\"丁来源\",\n" +
                "                \"patientSex\":\"男\",\n" +
                "                \"patientAge\":\"40\",\n" +
                "                \"SampleTypeName\":\"咽拭子\",\n" +
                "                \"sourceid\":\"\",\n" +
                "                \"inspectionItems\":[\n" +
                "                    {\n" +
                "                        \"itemName\":\"新型冠状病毒核酸检测\",\n" +
                "                        \"result\":\"阴性\",\n" +
                "                        \"refRange\":\"阴性(-)--\",\n" +
                "                        \"resultUnit\":\"\",\n" +
                "                        \"abnormal\":\"\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"itemName\":\"新型冠状病毒核酸检测\",\n" +
                "                        \"result\":\"阴性\",\n" +
                "                        \"refRange\":\"阴性(-)--\",\n" +
                "                        \"resultUnit\":\"\",\n" +
                "                        \"abnormal\":\"\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }";

        Map map = objectMapper.readValue(json, Map.class);

//        ImmutableMap<String, String> of = ImmutableMap.of("organization", "zbzx", "form", "NucleicAcidTest");
//        ImmutableMap<String, String> of1 = ImmutableMap.of("organization", "zbzx", "form", "NucleicAcidTest");

        File file = generatePdfByTemplate(id, "templates/Test.pdf", Arrays.asList(map));

        Resource resource = new UrlResource(file.toURI());
        if (resource.exists()) {
            String filename = new String(Objects.requireNonNull(resource.getFilename()).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"")
                    .body(resource);
        }
        return null;
    }

    public File generatePdfByTemplate(String filename, String template, Map<String, Object> map) throws Exception {
        // 构建文件存放位置
        String format = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 创建文件目录
        File path = new File(location + File.separator + format + File.separator);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path + File.separator + filename + PDF);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        // 模板路径
        Resource templateResource = ResourcePatternUtils.getResourcePatternResolver(null).getResource(template);

        // 读取pdf模板;
        PdfReader reader = new PdfReader(templateResource.getURL());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, byteArrayOutputStream);

        // 填充数据
        AcroFields acroFields = stamper.getAcroFields();
        acroFields.setGenerateAppearances(true);
        for (String formKey : acroFields.getFields().keySet()) {
            Object value = map.getOrDefault(formKey, EMPTY);
            if (value instanceof String ) {
                acroFields.setField(formKey, (String) value);
            } else if (value instanceof Collection) {
                acroFields.setField(formKey, recursionItems((Collection<Object>) value));
            }
        }

        // 如果为false那么生成的PDF文件还能编辑，一定要设为true
        stamper.setFormFlattening(true);
        stamper.close();

        // 输出PDF文件
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, fileOutputStream);
        document.open();
        PdfImportedPage importPage = copy.getImportedPage(new PdfReader(byteArrayOutputStream.toByteArray()), 1);
        copy.addPage(importPage);

        byteArrayOutputStream.close();
        copy.close();
        fileOutputStream.close();
        document.close();

        return file;
    }

    public File generatePdfByTemplate(String filename, String template, List<Map<String, Object>> mapList) throws Exception {
        // 构建文件存放位置
        String format = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 创建文件目录
        File path = new File(location + File.separator + format + File.separator);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path + File.separator + filename + PDF);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        // 模板路径
        Resource templateResource = ResourcePatternUtils.getResourcePatternResolver(null).getResource(template);

        List<PdfReader> pdfReaders = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            // 读取pdf模板;
            PdfReader reader = new PdfReader(templateResource.getURL());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, byteArrayOutputStream);

            // 填充数据
            AcroFields acroFields = stamper.getAcroFields();
            acroFields.setGenerateAppearances(true);
            for (String formKey : acroFields.getFields().keySet()) {
                Object value = map.getOrDefault(formKey, EMPTY);
                if (value instanceof String ) {
                    acroFields.setField(formKey, (String) value);
                } else if (value instanceof Collection) {
                    acroFields.setField(formKey, recursionItems((Collection<Object>) value));
                }
            }
            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.setFormFlattening(true);
            stamper.close();

            pdfReaders.add(new PdfReader(byteArrayOutputStream.toByteArray()));
            byteArrayOutputStream.close();
        }

        // 输出PDF文件
        Document document = new Document();
        PdfCopy pdfCopy = new PdfCopy(document, fileOutputStream);
        document.open();
        for (PdfReader pdfReader : pdfReaders) {
            document.newPage();
            pdfCopy.addDocument(pdfReader);
        }
        pdfCopy.close();
        document.close();
        fileOutputStream.close();

        return file;
    }

    private String recursionItems(Collection<Object> list) {
        StringBuilder builder = new StringBuilder("");
        for (Object obj : list) {
            if (obj instanceof Map){
                Map<String, Object> map = (Map<String, Object>) obj;
                map.forEach((key, value) -> {
                    if (value != null) {
                        if (value instanceof String) {
                            builder.append(value).append("                 ");
                        } else if (value instanceof Collection) {
                            builder.append(recursionItems((Collection<Object>) value));
                        }
                    }
                });
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
