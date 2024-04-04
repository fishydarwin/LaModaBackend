package com.github.fishydarwin.LaModaBackend.domain;

import com.github.fishydarwin.LaModaBackend.domain.validator.Validatable;

import java.util.regex.Pattern;

public record User(long id, String name, String passwordObfuscated, String email, UserRole role)
    implements Validatable {

    private static final Pattern PASSWORD_REGEX = Pattern.compile("^\\w+$");
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    @Override
    public String validate() {
        if (this.name.trim().length() <= 0) {
            return "Vă rugăm să introduceți numele dvs.";
        }
        if (this.name.trim().length() > 100) {
            return "Numele dvs trebuie să fie cel mult 100 de caractere.";
        }
        if (this.passwordObfuscated.trim().length() < 8) {
            return "Parola dvs. trebuie să conțină cel putin 8 caractere.";
        }
        if (!this.passwordObfuscated.matches(PASSWORD_REGEX.pattern())) {
            return "Parola dvs. trebuie să conțină doar litere sau cifre.";
        }
        if (!this.email.matches(EMAIL_REGEX.pattern())) {
            return "Vă rugăm să introduceți o adresă de e-mail validă.";
        }

        return "OK";
    }

}
