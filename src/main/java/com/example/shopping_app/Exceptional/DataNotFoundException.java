package com.example.shopping_app.Exceptional;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException (String message) {
        super(message);
    }
}
