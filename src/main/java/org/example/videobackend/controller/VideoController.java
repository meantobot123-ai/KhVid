package org.example.videobackend.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.videobackend.model.Video;
import org.example.videobackend.repository.VideoRepository;
import org.example.videobackend.service.VideoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class VideoController {

    private final VideoService videoService;
    private final VideoRepository videoRepository;

    public VideoController(VideoService videoService, VideoRepository videoRepository) {
        this.videoService = videoService;
        this.videoRepository = videoRepository;
    }

    // The key for streaming
    // AFTER
    @GetMapping("/videos/{filename:.+}") // Use .+ to handle filenames with dots
    public ResponseEntity<Resource> streamVideo(@PathVariable String filename) {
        Resource video = videoService.loadVideoAsResource(filename);

        // Check if the resource exists
        if (!video.exists() || !video.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(video);
    }

    // This endpoint is simplified for demonstration
    @GetMapping("/thumbnails/{filename}")
    public ResponseEntity<Resource> getThumbnail(@PathVariable String filename) {
        // In a real app, you would load the actual thumbnail file.
        // Here we'll just return a video resource for testing purposes.
        Resource file = videoService.loadVideoAsResource(filename); // Placeholder logic
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png") // You'd set the correct image type
                .body(file);
    }

    @GetMapping("/videos/all")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoRepository.findAll();
        return ResponseEntity.ok(videos);
    }


}