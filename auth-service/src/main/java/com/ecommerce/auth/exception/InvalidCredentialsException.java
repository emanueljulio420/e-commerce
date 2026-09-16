package com.ecommerce.auth.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException() {
        super("Usuario o contraseña incorrectos");
    }   
}
