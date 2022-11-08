package io.roach.movrapi.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.roach.movrapi.dto.AuthenticationResponseDTO;
import io.roach.movrapi.dto.CredentialsDTO;
import io.roach.movrapi.exception.NotFoundException;
import io.roach.movrapi.service.UserService;

/**
 * REST Controller to manage login/logout activities
 * (in an actual application, these functions would provide authentication checks and other security features.)
 */

@RestController
@RequestMapping("/api")
public class AccessController {

    private UserService userService;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    public AccessController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Logs in the user.
     *
     * @param credentialsDTO        a POJO holding the json (email) that was passed in
     * @return                      json indicating if the user was authenticated or not
     * @throws NotFoundException    if the passed email does not exist
     */
    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody CredentialsDTO credentialsDTO)
        throws NotFoundException {
        logger.info("[POST] /api/auth/login");

        userService.getUser(credentialsDTO.getEmail());
        // in a normal app, we would perform a security check before returning a token or some other security method
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO();
        authenticationResponseDTO.setAuthenticated(true);
        return ResponseEntity.ok(authenticationResponseDTO);
    }
}
