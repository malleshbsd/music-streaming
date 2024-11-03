package com.tracks.musicstreaming.controller;


import com.tracks.musicstreaming.service.MusicTrackServiceImpl;
import com.tracks.musicstreaming.service.MusicTrackServiceTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MusicTrackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MusicTrackServiceImpl musicTrackService;

    private Long testTrackId;

    @BeforeEach
    public void setup(){
        testTrackId = musicTrackService.saveTrack(MusicTrackServiceTests.getMusicTrack()).getId();
    }

    @Order(1)
    @Test
    public void testCreateTrack() throws Exception {
        String trackJson = "{\"title\":\"Lost in Echoes\", \"albumName\":\"Echoes of the Unknown\", \"releaseDate\":\"2021-07-15\", \"playCount\":5000}";

        mockMvc.perform(post("/music/platform/v1/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trackJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Lost in Echoes"));
    }
    @Test
    public void testCreateTrackValidation() throws Exception {
        String trackJson = "{\"title\":\"\", \"albumName\":\"Echoes of the Unknown\", \"releaseDate\":\"2021-07-15\", \"playCount\":5000}";

        mockMvc.perform(post("/music/platform/v1/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trackJson))
                .andExpect(status().isBadRequest());
    }

    @Order(2)
    @Test
    public void testGetAllTracks() throws Exception {
        mockMvc.perform(get("/music/platform/v1/tracks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Order(3)
    @Test
    public void testGetSortedTracks() throws Exception {
        mockMvc.perform(get("/music/platform/v1/tracks/sorted"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void testGetSortedTracksDesc() throws Exception {
        mockMvc.perform(get("/music/platform/v1/tracks/sorted?sortDirection=desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Order(4)
    @Test
    public void testDeleteTrack() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/music/platform/v1/tracks/"+testTrackId)
                        )
            .andExpect(status().isNoContent());
        assertFalse(musicTrackService.existsById(testTrackId));
    }
    @Order(5)
    @Test
    public void testDeleteNotExistingTrack() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/music/platform/v1/tracks/9999")
                )
                .andExpect(status().isNotFound());
    }
    @Test
    public void testCaching() throws Exception {
        mockMvc.perform(get("/music/platform/v1/tracks"))
                .andExpect(status().isOk());
        // Run the same request again to check if it’s served from the cache
        mockMvc.perform(get("/music/platform/v1/tracks"))
                .andExpect(status().isOk());
    }
}