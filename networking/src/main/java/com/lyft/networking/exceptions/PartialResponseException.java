package com.lyft.networking.exceptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Exception thrown when the response returned by the server was incomplete.
 * Typically, this means when non-null expected fields were parsed as null values.
 */
public class PartialResponseException extends IOException {

    Object object;

    public PartialResponseException(@NotNull Object object) {
        this.object = object;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("The following object was returned incorrectly by the network:\n")
                .append(object.toString());

        return sb.toString();
    }
}
