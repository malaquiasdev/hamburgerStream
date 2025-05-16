package dev.malaquias.order.infra.dto;

import java.util.List;

public record PagedResponse<T>(
        List<T> data,
        int page,
        int size,
        long totalItems,
        int totalPages,
        Integer nextPage,
        Integer previousPage
) {}