package com.ecommerce.project.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String field;
    String fieldName;
    int fieldId;

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s : '%s'", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String field, String resourceName, int fieldId) {
        super(String.format("%s not found with %s : '%d'", resourceName, field, fieldId));
        this.field = field;
        this.resourceName = resourceName;
        this.fieldId = fieldId;
    }
}
