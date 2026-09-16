package com.ecommerce.auth.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Nombre de usuario ya existe: " + username);
    }
}
