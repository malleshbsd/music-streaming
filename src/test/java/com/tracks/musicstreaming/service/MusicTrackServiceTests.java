package com.tracks.musicstreaming.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.tracks.musicstreaming.model.MusicTrack;
import com.tracks.musicstreaming.repository.MusicTrackRepository;
import com.tracks.musicstreaming.util.SortingUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class MusicTrackServiceTests {

    @InjectMocks
    private MusicTrackServiceImpl service;

    @Mock
    private MusicTrackRepository repository;
    @Mock
    private SortingUtility sortingUtility;

    private Long savedTrackId ;

    @BeforeEach
    public void setup(){

        when(repository.save(any())).thenReturn(getMusicTrack());
        MusicTrack savedTrack = service.saveTrack(getMusicTrack());
        savedTrackId = savedTrack.getId();
    }

    @Test
    public void testAddTrack() {
        assertThat(savedTrackId).isNotNull();
    }

    @Test
    public void testGetAllTracks() {
        List<MusicTrack> songTracks = new ArrayList<>();
        songTracks.add(getMusicTrack());
        Page<MusicTrack> pagedResponse = new PageImpl(songTracks);
        when(repository.findAll(PageRequest.of(0,10))).thenReturn(pagedResponse);
        Page<MusicTrack> tracks = service.getAllTracks(0,10);
        assertThat(tracks).isNotEmpty();
    }

    @Test
    public void testGetTracksSorted() {

        Sort.Direction direction = Sort.Direction.ASC;
        var sort = Sort.by(direction, "title");
        when(sortingUtility.getSort( anyString(),anyString())).thenReturn(sort);
        when(repository.findAll((Sort) any())).thenReturn(List.of(getMusicTrack()));
        List<MusicTrack> tracks = service.getTracksSorted("title","asc");
        assertThat(tracks).isNotEmpty();
    }

    @Test
    public void testDeleteTrackById() {
        when(repository.existsById( any())).thenReturn(true);
        service.deleteTrackById(savedTrackId);
        Mockito.verify(repository).deleteById(savedTrackId);
    }

    public static MusicTrack getMusicTrack(){
        MusicTrack musicTrack = new MusicTrack();
        musicTrack.setTitle("Title");
        musicTrack.setAlbumName("AlbumName");
        musicTrack.setReleaseDate(new Date());
        musicTrack.setPlayCount(100);
        musicTrack.setId(1L);
        return musicTrack;
    }
}