package com.backend.library.backend.converters;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.backend.library.backend.dto.books.TempPageRequest;
@Service
public class TempPageConverter {

    private TempPageConverter() {
        // Private constructor to prevent instantiation
    }

    public static PageRequest toPageRequest(TempPageRequest pageReq) {
        if (pageReq.sorts() == null) {
            return PageRequest.of(pageReq.pageNumber(), pageReq.pageSize());
        }

        return PageRequest.of(pageReq.pageNumber(), pageReq.pageSize(), Sort.by(pageReq.sorts()).descending());
    }
}
