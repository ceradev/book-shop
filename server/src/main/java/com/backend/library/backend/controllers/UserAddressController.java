package com.backend.library.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.library.backend.dto.addresses.AddressDTO;
import com.backend.library.backend.dto.addresses.CreateRequestAddress;
import com.backend.library.backend.services.interfaces.IUserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/addresses")
public class UserAddressController {

    private IUserAddressService userAddressService;

    public UserAddressController(IUserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get all user addresses", description = "Get all user addresses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> getAllUserAddresses(@PathVariable String userId) {
        return userAddressService.getAllAddressByUserId(userId);
    }

    @GetMapping("/me")
    @Operation(summary = "Get all my user addresses", description = "Get all my user addresses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> getAllMyUserAddresses(HttpServletRequest request) {
        return userAddressService.getMyAddresses(request);
    }

    @PostMapping
    @Operation(summary = "Create new user address", description = "Create a new user address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<?> addUserAddress(HttpServletRequest request,
            @Valid @RequestBody CreateRequestAddress address) {
        return userAddressService.createAddress(request, address);
    }

    @PutMapping
    @Operation(summary = "Update an existing user address", description = "Update an existing user address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<?> updateUserAddress(HttpServletRequest request, @Valid @RequestBody AddressDTO address) {
        return userAddressService.updateAddress(request, address);
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Delete an existing user address", description = "Delete an existing user address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<?> deleteUserAddress(HttpServletRequest request, @Valid @PathVariable Long addressId) {
        return userAddressService.deleteAddress(request, addressId);
    }
}
