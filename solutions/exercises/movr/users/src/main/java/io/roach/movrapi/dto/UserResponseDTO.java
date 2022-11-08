package io.roach.movrapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object ot pass profile responses back to the front-end
 */

public class UserResponseDTO {

    @JsonProperty("user")
    private UserDTO userDTO;
    @JsonProperty("messages")
    private MessagesDTO messagesDTO;

    public UserResponseDTO(UserDTO userDTO, MessagesDTO messagesDTO) {
        this.userDTO = userDTO;
        this.messagesDTO = messagesDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public MessagesDTO getMessagesDTO() {
        return messagesDTO;
    }

    public void setMessagesDTO(MessagesDTO messagesDTO) {
        this.messagesDTO = messagesDTO;
    }
}
