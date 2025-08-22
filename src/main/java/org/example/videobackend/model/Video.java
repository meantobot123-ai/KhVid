package org.example.videobackend.model;// Or your package

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String videoFileName;
    private String thumbnailFileName;

    // --- Constructors ---
    public Video() {}

    // ✅ This is the constructor to fix.
    // Ensure the order matches how you call it in your service.
    public Video(String name, String videoFileName, String thumbnailFileName) {
        this.name = name;
        this.videoFileName = videoFileName;
        this.thumbnailFileName = thumbnailFileName;
    }
}