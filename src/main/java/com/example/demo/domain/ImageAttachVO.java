package com.example.demo.domain;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
@Log4j2
public class ImageAttachVO {
    private String uuid;
    private String uploadPath;
    private String fileName;

    public String upload(MultipartFile uploadFile) {
        String uploadFolder = "C:\\hotple_manager";

        String uploadFolderPath = getFolder();

        // make folder -----
        File uploadPath = new File(uploadFolder, getFolder());
        log.info("upload path: " + uploadPath);

        if(uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }
        // make yyyy/MM/dd folder

        log.info("--------------------");
        log.info("Upload File Name: " + uploadFile.getOriginalFilename());
        log.info("Upload File Size: " + uploadFile.getSize());

        String uploadFileName = uploadFile.getOriginalFilename();

        // IE has file path
        uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);

        // 띄어쓰기 제거
        uploadFileName = uploadFileName.replaceAll(" ", "");

        log.info("only file name: " + uploadFileName);
        this.fileName = uploadFileName;

        UUID uuid = UUID.randomUUID();

        uploadFileName = uuid.toString() + "_" + uploadFileName;

        try {
            File saveFile = new File(uploadPath, uploadFileName);
            uploadFile.transferTo(saveFile);

            this.uuid = uuid.toString();
            this.uploadPath = uploadFolderPath;

            FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
            Thumbnailator.createThumbnail(uploadFile.getInputStream(), thumbnail, 100, 100);
            thumbnail.close();

        } catch (Exception e) {
            log.info(e.getMessage());
        }

        String url = uploadPath + "\\" + uploadFileName;
        log.info(url);
        String serverUrl = url.replaceFirst("C:\\\\hotple_manager", "hotpleImage");
        serverUrl = serverUrl.replaceAll("\\\\", "/");
        log.info(serverUrl);
        return "/" + serverUrl;
    }

    private String getFolder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        String str = sdf.format(date);

        return str.replace("-", File.separator);
    }

    public void deleteFiles(String uuid, String uploadPath, String fileName) {
        try {
            Path file = Paths.get("C:\\hotple_manager\\" + uploadPath + "\\" + uuid + "_" + fileName);

            Files.deleteIfExists(file);

            if(Files.probeContentType(file).startsWith("image")) {
                Path thumbNail = Paths.get("C:\\hotple_manager\\" + uploadPath + "\\s_" + uuid + "_" + fileName);
                Files.delete(thumbNail);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        log.info("삭제됨");
    }
}
