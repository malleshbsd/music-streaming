package com.tracks.musicstreaming.service;

import com.tracks.musicstreaming.model.MusicTrack;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MusicTrackService {

    MusicTrack saveTrack(MusicTrack track);
    Page<MusicTrack> getAllTracks(int page, int size);
    List<MusicTrack> getTracksSorted(String sortField, String sortDirection);
    void deleteTrackById(Long id);
    boolean existsById(Long id);
}
