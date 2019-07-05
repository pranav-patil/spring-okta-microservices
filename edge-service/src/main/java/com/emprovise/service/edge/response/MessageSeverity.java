package com.emprovise.service.edge.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageSeverity {

    SUCCESS("success"),
    ERROR("error"),
    WARNING("warning");

    private String value;

    MessageSeverity(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
