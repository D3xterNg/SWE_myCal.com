package sg.edu.ntu.scse.dgjmqz.myCal.service;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
public class EmailService {
    private  Properties emailConfig;
    public EmailService() throws IOException {
        emailConfig = new Properties();
        emailConfig.load(this.getClass().getClassLoader().getResourceAsStream("META-INF/email.properties"));
    }
    public  void snedEmail(String sendAddr,String text) throws AddressException, MessagingException {
        //make a new thread so that will not need to wait for the mail to be send before the server response  to the user
        new Thread(()->{
            try {
                String user = emailConfig.getProperty("user");
                Session session = Session.getInstance(emailConfig, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, emailConfig.getProperty("password"));

                    }
                });
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(user));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendAddr));
                message.setSubject("mcal");
                message.setContent(text,"text/html");
                Transport.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }
}
