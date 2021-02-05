package neumont.donavon.tommy.LabOne.components;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.beans.Encoder;
import java.nio.charset.StandardCharsets;

@Component
public class EmailEventConsumer {

    private final JavaMailSender emailSender;

    EmailEventConsumer(JavaMailSenderImpl emailSender){

        this.emailSender = emailSender;
    }

    @RabbitListener(queues = "emailQueue")
    public void sendSimpleMessage(String[] message){
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(message[0]);
        email.setSubject("New Password");
        email.setText(String.format("New Password: %s", message[1]));
        emailSender.send(email);

    }
}
