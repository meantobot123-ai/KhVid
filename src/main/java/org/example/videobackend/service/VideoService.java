package org.example.videobackend.service;

import com.cloudinary.utils.ObjectUtils;
import org.example.videobackend.model.Video;
import org.example.videobackend.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class VideoService {

    private final Path rootLocation;
    private final VideoRepository videoRepository;

    public VideoService(@Value("${file.upload-dir}") String uploadDir, VideoRepository videoRepository) {
        this.rootLocation = Paths.get(uploadDir);
        this.videoRepository = videoRepository;
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }


    public Resource loadVideoAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}