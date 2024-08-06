package com.backend.library.backend.services.implementations;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.backend.library.backend.dto.addresses.AddressDTO;
import com.backend.library.backend.dto.addresses.CreateRequestAddress;
import com.backend.library.backend.mappers.interfaces.IUserAddressMapper;
import com.backend.library.backend.persistence.entities.Address;
import com.backend.library.backend.persistence.entities.UserAddress;
import com.backend.library.backend.persistence.repositories.AddressRepository;
import com.backend.library.backend.persistence.repositories.UserAddressRepository;
import com.backend.library.backend.services.interfaces.IUserAddressService;
import com.backend.library.backend.utils.KeycloakProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAddressServiceImpl implements IUserAddressService {

    private UserAddressRepository userAddressRepository;
    private IUserAddressMapper userAddressMapper;
    private AddressRepository addressRepository;

    public UserAddressServiceImpl(UserAddressRepository userAddressRepository, IUserAddressMapper userAddressMapper,
            AddressRepository addressRepository) {
        this.userAddressRepository = userAddressRepository;
        this.userAddressMapper = userAddressMapper;
        this.addressRepository = addressRepository;
    }

    /**
     * Get all the addresses of a user
     * @param userId the id of the user
     * @return a ResponseEntity with a list of AddressDTO, or an Internal Server Error
     * if an error occurs
     */
    @Override
    public ResponseEntity<List<AddressDTO>> getAllAddressByUserId(String userId) {
        /**
         * This method is used to get all the addresses of a user from the database
         * @param userId the id of the user
         * @return a ResponseEntity with a list of AddressDTO, or an Internal Server Error
         * if an error occurs
         */
        try {
            // Get all the addresses of the user from the database
            log.info("The addresses of the user were obtained successfully");
            return ResponseEntity.status(200)
                    .body(userAddressMapper.toListUserAddressDTO(userAddressRepository.findAllByUserId(userId)));
        } catch (Exception e) {
            // Log the error and return an Internal Server Error if an error occurs
            log.error("The addresses of the user cannot be found");
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get the addresses of a user from the token in the Authorization header
     * @param request the HTTP request object
     * @return a ResponseEntity with a list of AddressDTO, or an Internal Server Error
     * if an error occurs
     */
    @Override
    public ResponseEntity<List<AddressDTO>> getMyAddresses(HttpServletRequest request) {
        try {

            // Get the token from the Authorization header
            String token = request.getHeader("Authorization");

            // Check if the token is valid (is not null)
            if (token == null) {
                log.error("The token is not valid. Try again");
                return ResponseEntity.status(401).body(null);
            }

            // Parse the token and get the user id from it
            token = token.replaceFirst("^Bearer ", "");
            String userId = JWT.decode(token).getClaim("sub").asString();

            // Get all the addresses of the user from the database
            log.info("the addresses were obtained successfully");
            return ResponseEntity.status(200)
                    .body(userAddressMapper.toListUserAddressDTO(userAddressRepository.findAllByUserId(userId)));

        } catch (Exception e) {
            // Log the error and return an Internal Server Error if an error occurs
            log.error("The adresses cannot be found", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Create a new address for a user
     * @param request the HTTP request object
     * @param addressDTO the data to create the address
     * @return a ResponseEntity with a AddressDTO, or an Internal Server Error
     * if an error occurs
     */
    @Override
    public ResponseEntity<AddressDTO> createAddress(HttpServletRequest request, CreateRequestAddress addressDTO) {
        try {
            // Get the user ID from the JWT in the request
            String userId = KeycloakProvider.getUserIdFromToken(request);

            // Create a new Address object with the given data
            Address savedAddress = addressRepository
                    .save(new Address(null, addressDTO.road(), addressDTO.postalCode(), addressDTO.num(),
                            addressDTO.city(), addressDTO.country()));

            // Create a new UserAddress object with the given user ID and address
            UserAddress userAddress = new UserAddress(null, userId, savedAddress);
            // Save the UserAddress object in the database
            userAddressRepository.save(userAddress);

            // Return a ResponseEntity with the created Address DTO
            log.info("The address was created successfully");
            return ResponseEntity.ok(userAddressMapper.toAddressDTO(savedAddress));
        } catch (Exception e) {
            // Log the error and return an Internal Server Error if an error occurs
            log.error("The address cannot be created", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Update the information of an address of a user
     * 
     * @param request the HTTP request object
     * @param addressDTO the updated information of the address
     * @return a ResponseEntity with an AddressDTO, or a 404 if the address does not exist
     * or an Internal Server Error if an error occurs
     */

    /**
     * Update the information of an address of a user
     * 
     * @param request the HTTP request object
     * @param addressDTO the updated information of the address
     * @return a ResponseEntity with an AddressDTO, or a 404 if the address does not exist
     * or an Internal Server Error if an error occurs
     */
    @Override
    public ResponseEntity<AddressDTO> updateAddress(HttpServletRequest request, AddressDTO addressDTO) {
        try {
            // Get the userId of the user from the JWT in the request
            String userId = KeycloakProvider.getUserIdFromToken(request);

            // Get the UserAddress object from the database that matches the
            // address id and user id from the request
            Optional<UserAddress> optionalExistingAddress = userAddressRepository
                    .findByAddressIdAndUserId(addressDTO.id(), userId);

            if (optionalExistingAddress.isPresent()) {

                // Update the address in the database with the new information
                Address savedAddress = addressRepository
                        .save(new Address(addressDTO.id(), addressDTO.road(), addressDTO.postalCode(), addressDTO.num(),
                                addressDTO.city(), addressDTO.country()));

                // Save the updated UserAddress object in the database
                userAddressRepository
                        .save(new UserAddress(optionalExistingAddress.get().getId(), userId, savedAddress));

                // Return a ResponseEntity with the updated address DTO
                log.info("");
                return ResponseEntity.ok(userAddressMapper.toAddressDTO(savedAddress));
            } else {
                // Return a 404 if the address does not exist
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Log the error and return an Internal Server Error if an error occurs
            log.error("");
            return ResponseEntity.status(500).build();
        }
    }


    /**
     * Delete an address of a user
     * @param request the HTTP request object
     * @param id the id of the address to delete
     * @return a ResponseEntity with an AddressDTO, or a 404 if the address does not exist
     * or an Internal Server Error if an error occurs
     */
    @Override
    public ResponseEntity<AddressDTO> deleteAddress(HttpServletRequest request, Long id) {
        try {
            // Get the user id from the given HTTP request
            String userId = KeycloakProvider.getUserIdFromToken(request);

            // Search the user address by its id and user id
            Optional<UserAddress> optionalExistingAddress = userAddressRepository.findByIdAndUserId(id, userId);

            // Return a 404 if the address does not exist
            if (!optionalExistingAddress.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Delete the address from the database
            userAddressRepository.deleteById(id);

            // Return the deleted address as an AddressDTO with a 200 status
            return ResponseEntity.ok(userAddressMapper.toAddressDTO(optionalExistingAddress.get()));
        } catch (Exception e) {
            // Log the error and return an internal server error response if an error occurs
            log.error("Error while deleting address: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

}



