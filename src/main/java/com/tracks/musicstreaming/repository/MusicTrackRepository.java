package com.tracks.musicstreaming.repository;

import com.tracks.musicstreaming.model.MusicTrack;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicTrackRepository extends JpaRepository<MusicTrack, Long> {

    List<MusicTrack> findAll(Sort sort);
}