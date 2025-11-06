package com.skilluser.user.utility;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;


// 12-Sept-2025  by Shrunkha;

public class PaginationUtil {

    public static Pageable createPageRequest(int page, int size, String sortBy, String direction) {
        Sort sort = (direction.equalsIgnoreCase("desc"))
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return PageRequest.of(page, size, sort);
    }

    public static <T> Map<String, Object> buildResponse(Page<T> pageData) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", pageData.getContent());
        response.put("currentPage", pageData.getNumber());
        response.put("totalItems", pageData.getTotalElements());
        response.put("totalPages", pageData.getTotalPages());
        response.put("pageSize", pageData.getSize());
        response.put("isLast", pageData.isLast());
        return response;
    }
}
