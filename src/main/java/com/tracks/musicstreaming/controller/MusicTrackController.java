package com.tracks.musicstreaming.controller;


import com.tracks.musicstreaming.exceptions.TrackNotFoundException;
import com.tracks.musicstreaming.model.MusicTrack;
import com.tracks.musicstreaming.service.MusicTrackService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/music/platform/v1/tracks")
@Validated
public class MusicTrackController {

    private static final Logger logger = LoggerFactory.getLogger(MusicTrackController.class);

    private static final String H2DB_SERVICE = "h2db";
    private static final String MUSIC_SERVICE = "musicService";

    @Autowired
    private MusicTrackService service;

    @PostMapping
    @CircuitBreaker(name = H2DB_SERVICE, fallbackMethod = "dbServiceFallback")
    public ResponseEntity<MusicTrack> createTrack(@Valid @RequestBody MusicTrack musicTrack) {
        musicTrack.setId(null);
        MusicTrack savedTrack = service.saveTrack(musicTrack);
        logger.info("Created track with ID: {}", savedTrack.getId());
        return new ResponseEntity<>(savedTrack, HttpStatus.CREATED);
    }

    public ResponseEntity<String> dbServiceFallback(Exception ex) {
        return new ResponseEntity<>("Database service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping
    @RateLimiter(name = MUSIC_SERVICE, fallbackMethod = "rateLimiterFallback")
    public ResponseEntity<Page<MusicTrack>> getAllTracks(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(service.getAllTracks(page, size), HttpStatus.OK);
    }

    public ResponseEntity<String> rateLimiterFallback(Exception ex) {
        return new ResponseEntity<>("Too many requests - please try again later", HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("/sorted")
    //@RateLimiter(name = MUSIC_SERVICE, fallbackMethod = "rateLimiterFallback")
    public ResponseEntity<List<MusicTrack>> getSortedTracks(
            @RequestParam(defaultValue = "title") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        logger.info("Fetching all tracks sorted by title");
        List<MusicTrack> tracks = service.getTracksSorted(sortField, sortDirection);
        return ResponseEntity.ok(tracks);
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long trackId) {
        logger.info("Attempting to delete track with ID: {}", trackId);
        if (service.existsById(trackId)) {
            service.deleteTrackById(trackId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.warn("Track with ID: {} not found", trackId);
        throw new TrackNotFoundException("Track with ID " + trackId + " does not exist");
    }
}