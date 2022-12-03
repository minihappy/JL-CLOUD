package com.service.source.dto;

public interface RegisterDao {
    Long getId();

    String getName();

    default String toStringInfo() {
        return getId() + " " + getName();
    }
}
