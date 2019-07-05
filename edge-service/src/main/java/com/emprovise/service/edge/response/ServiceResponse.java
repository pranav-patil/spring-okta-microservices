package com.emprovise.service.edge.response;

public class ServiceResponse {

    private String messageId;
    private MessageSeverity severity;
    private String message;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public MessageSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(MessageSeverity severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
