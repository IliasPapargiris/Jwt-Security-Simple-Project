package com.logicea.demo.util;

import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

public class SortUtils {

    private static final List<String> ALLOWED_SORT_DIRECTIONS = Arrays.asList("asc", "desc");

    public static Sort validateSort(String[] sortParams, List<String> allowedSortFieldsList) {
        Sort sort = null;
        //each sort field must be followed by asc or desc
        int maxAllowedSortParams = allowedSortFieldsList.size() * 2;
        if (sortParams != null && sortParams.length > 0) {
            if (sortParams.length > maxAllowedSortParams) {
                throw new IllegalArgumentException("Invalid sort parameters, only 3 sorting options available");
            }

            for (int i = 0; i < sortParams.length; i += 2) {
                String sortField = sortParams[i];
                //in the case that i have a sort field with no sort direction, an asc direction will be assigned
                String sortDirection = (i + 1) < sortParams.length ? sortParams[i + 1] : "asc";

                if (!allowedSortFieldsList.contains(sortField)) {
                    throw new IllegalArgumentException("Invalid sort field: " + sortField);
                }

                if (!ALLOWED_SORT_DIRECTIONS.contains(sortDirection) && !allowedSortFieldsList.contains(sortDirection)) {
                    throw new IllegalArgumentException("Invalid sort direction: " + sortDirection);
                }else if(allowedSortFieldsList.contains(sortDirection)){
                    sortDirection = "asc";
                    i -=1;
                }

                if (sort == null) {
                    sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
                } else {
                    sort = sort.and(Sort.by(Sort.Direction.fromString(sortDirection), sortField));
                }
            }
            return sort;
        }
        return sort;
    }
}
