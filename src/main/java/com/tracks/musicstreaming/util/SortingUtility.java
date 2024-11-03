package com.tracks.musicstreaming.util;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class SortingUtility {

    /**
     * Returns a Sort object based on the given sortField and sortDirection.
     * @param sortField the field to sort by (e.g., "title", "albumName", "releaseDate", "playCount")
     * @param sortDirection the direction to sort (ascending or descending)
     * @return Sort object to be used in database query
     */
    public Sort getSort(String sortField, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(sortDirection)) {
            direction = Sort.Direction.DESC;
        }
        return Sort.by(direction, sortField);
    }
}
