package api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {
    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verifica tu cuenta");
        message.setText(
                "Haz clic para verificar tu cuenta:\n\n" +
                        "http://localhost:8080/api/auth/verify?token=" + token
        );

        javaMailSender.send(message);
    }
}
