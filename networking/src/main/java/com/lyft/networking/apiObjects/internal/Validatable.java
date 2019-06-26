package com.lyft.networking.apiObjects.internal;

/**
 * INTERNAL USE ONLY.
 *
 * An interface used to verify that Response objects have been rendered correctly and are valid to serve to the client.
 */
public interface Validatable {

    /**
     * Invoked by the Retrofit client on the parsed object before returning it to the caller.
     * If all fields in the parsed response have been rendered correctly (i.e. fields meant to be non-null are non-null)
     * this method will return true.
     *
     * If any fields also implement {@link Validatable}, the implementation is responsible for recursively checking
     * the #isValid() method of those fields.
     *
     * @return true if all fields of the class have been parsed correctly.
     */
    boolean isValid();
}
