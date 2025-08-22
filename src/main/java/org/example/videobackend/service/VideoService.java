package org.example.videobackend.service;

import com.cloudinary.Cloudinary;
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
    private final Cloudinary cloudinary;
    private final VideoRepository videoRepository;

    public VideoService(@Value("${file.upload-dir}") String uploadDir, VideoRepository videoRepository, Cloudinary cloudinary) {
        this.rootLocation = Paths.get(uploadDir);
        this.videoRepository = videoRepository;
        this.cloudinary = cloudinary;
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public Video saveVideo(MultipartFile file, String name) {
        try {
            // Define parameters for the upload
            Map params = ObjectUtils.asMap(
                    "resource_type", "video", // Tell Cloudinary this is a video
                    "public_id", "videos/" + UUID.randomUUID().toString() // Optional: Set a public ID in a folder
            );

            // Upload the file's bytes to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

            // Get the secure URL of the uploaded video
            String videoUrl = (String) uploadResult.get("secure_url");

            // For a real app, you could generate a thumbnail URL from the video URL
            // Cloudinary can do this automatically with transformations
            String thumbnailUrl = videoUrl.replace(".mp4", ".jpg"); // Simple placeholder

            // Save metadata to your database
            Video video = new Video(name, videoUrl, thumbnailUrl);
            return videoRepository.save(video);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary.", e);
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