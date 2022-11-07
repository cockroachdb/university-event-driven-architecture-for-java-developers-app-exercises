package io.roach.movrapi.dto;


/**
 *  Data transfer object to pass login info (in an actual application, this would include other authorization information
 *  such as password)
 */

public class CredentialsDTO {

    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
