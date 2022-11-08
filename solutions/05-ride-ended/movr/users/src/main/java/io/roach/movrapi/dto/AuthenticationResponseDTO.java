package io.roach.movrapi.dto;

/**
 * Data transfer object containing info for an authenticated user (in a real app this would contain session
 * info or a token of some sort)
 */

public class AuthenticationResponseDTO {

    private boolean isAuthenticated;

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }
}
