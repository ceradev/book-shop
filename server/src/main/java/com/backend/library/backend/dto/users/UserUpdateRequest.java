package com.backend.library.backend.dto.users;/**
 * UserUpdateDTO
 */
public record UserUpdateRequest(
        String username,
        String name,
        String surname) {
}