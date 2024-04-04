package com.github.fishydarwin.LaModaBackend.domain.validator;

public class Validator {

    public static String validate(Validatable validatable) {
        return validatable.validate();
    }

}
