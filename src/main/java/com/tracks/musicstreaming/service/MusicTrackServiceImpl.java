package com.tracks.musicstreaming.service;

import com.tracks.musicstreaming.model.MusicTrack;
import com.tracks.musicstreaming.repository.MusicTrackRepository;
import com.tracks.musicstreaming.util.SortingUtility;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicTrackServiceImpl implements MusicTrackService{

    private static final Logger logger = LoggerFactory.getLogger(MusicTrackServiceImpl.class);

    @Autowired
    private MusicTrackRepository repository;
    @Autowired
    private SortingUtility sortingUtility;

    @Transactional
    @CacheEvict(value = {"tracks", "sortedTracks"}, allEntries = true)
    @Override
    public MusicTrack saveTrack(MusicTrack track) {
        logger.debug("save track {}", track);
        return repository.save(track);
    }

    @Cacheable("tracks")
    @Override
    public Page<MusicTrack>  getAllTracks(int page, int size){
        logger.debug("getAllTracks  page : {}, size : {} ", page,size);
        return repository.findAll(PageRequest.of(page,size));
    }

    @Cacheable("sortedTracks")
    @Override
    public List<MusicTrack> getTracksSorted(String sortField, String sortDirection) {
        logger.debug("save sortField :: {}, sortDirection :: {}", sortField, sortDirection);
        Sort sort = sortingUtility.getSort(sortField, sortDirection);
        return repository.findAll(sort);
    }

    @Transactional
    @CacheEvict(value = {"tracks", "sortedTracks"}, allEntries = true)
    @Override
    public void deleteTrackById(Long id) {
        logger.debug(" deleteTrackById track id {}", id);
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        logger.debug(" existsById track id {}", id);
        return repository.existsById(id);
    }
}
