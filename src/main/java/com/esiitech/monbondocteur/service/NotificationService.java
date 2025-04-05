package com.esiitech.monbondocteur.service;

import com.esiitech.monbondocteur.model.Validation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class NotificationService {
    JavaMailSender javaMailSender;
    public void envoyer (Validation validation) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("no-reply@gmail.com");
    message.setTo(validation.getUtilisateur().getEmail());
    message.setSubject("Votre code d'activation");
        String texte = String.format(
                "Bonjour M/Mme %s,\n" +
                        "Nous vous informons que votre demande d'inscription a été reçue.\n" +
                        "Pour finaliser votre inscription, veuillez utiliser le code d'activation suivant : %s.\n" +
                        "Ce code est valable pour les 10 prochaines minutes.\n" +
                        "Si vous n'avez pas demandé cette inscription, veuillez ignorer ce message.\n"+
                        "Cordialement,\n"+
                        "L'équipe de MonBondocteur.",
            validation.getUtilisateur().getNom(), validation.getCode());

    message.setText(texte);
    javaMailSender.send(message);

    }

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
}
