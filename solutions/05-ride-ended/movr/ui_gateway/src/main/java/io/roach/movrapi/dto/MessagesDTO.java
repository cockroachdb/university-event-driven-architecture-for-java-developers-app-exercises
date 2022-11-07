package io.roach.movrapi.dto;

/**
 * Data transfer object to hold error or status messages
 */

public class MessagesDTO {

    public MessagesDTO() {
        this.messages = new String[]{};
    }

    public MessagesDTO(String[] messages) {
        this.messages = messages;
    }

    public MessagesDTO(String message) {
        this.messages = new String[]{message};
    }

    private String[] messages;

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }
}
