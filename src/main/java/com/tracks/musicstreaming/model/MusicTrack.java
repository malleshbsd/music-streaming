package com.tracks.musicstreaming.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(indexes = {@Index(name = "idx_title", columnList = "title")})
public class MusicTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Album name is required")
    private String albumName;

    @PastOrPresent(message = "Release date must be in the past or present")
    private Date releaseDate;

    @Min(value = 0, message = "Play count cannot be negative")
    private Integer playCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Title is required") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") String title) {
        this.title = title;
    }

    public @NotBlank(message = "Album name is required") String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(@NotBlank(message = "Album name is required") String albumName) {
        this.albumName = albumName;
    }

    public @PastOrPresent(message = "Release date must be in the past or present") Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(@PastOrPresent(message = "Release date must be in the past or present") Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public @Min(value = 0, message = "Play count cannot be negative") Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(@Min(value = 0, message = "Play count cannot be negative") Integer playCount) {
        this.playCount = playCount;
    }
}