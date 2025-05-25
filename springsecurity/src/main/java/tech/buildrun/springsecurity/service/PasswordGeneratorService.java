package tech.buildrun.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.buildrun.springsecurity.controller.dto.EmailDTO;

import java.security.SecureRandom;

@Service
public class PasswordGeneratorService {

    private final EmailService emailService;

    public PasswordGeneratorService(EmailService emailService) {
        this.emailService = emailService;
    }

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%&*()-_=+[]{}";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateStrongPassword(int length) {
        StringBuilder password = new StringBuilder(length);

        // Garante pelo menos um caractere de cada tipo
        password.append(UPPER.charAt(RANDOM.nextInt(UPPER.length())));
        password.append(LOWER.charAt(RANDOM.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(RANDOM.nextInt(SPECIAL.length())));

        for (int i = 4; i < length; i++) {
            password.append(ALL.charAt(RANDOM.nextInt(ALL.length())));
        }

        // Embaralha os caracteres
        return shuffleString(password.toString());
    }

    private String shuffleString(String input) {
        char[] a = input.toCharArray();
        for (int i = a.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
        return new String(a);
    }

    public void gerarSenhaEEnviar(String emailDestino) {
        String senha = generateStrongPassword(12);
        EmailDTO emailDTO = new EmailDTO(
        emailDestino,
        "Sua nova senha",
        "Sua senha gerada é: " + senha);
        emailService.sendEmail(emailDTO);
    }
}