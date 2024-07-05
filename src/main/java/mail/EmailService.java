package mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import logging.LoggerWrapper;

import java.util.Properties;

public class EmailService
{
    private static final LoggerWrapper logger = new LoggerWrapper(EmailService.class);

    private static EmailService instance;

    private final String Email;
    private final String User;
    private final String Password;
    private final String Server;
    private final String Port;

    private EmailService(String Email, String User, String Password, String Server, String Port)
    {
        this.Email = Email;
        this.User = User;
        this.Password = Password;
        this.Server = Server;
        this.Port = Port;
    }

    public static synchronized EmailService getInstance(String Email, String User, String Password, String Server, String Port)
    {
        if(instance == null || !instance.Email.equals(Email) || !instance.User.equals(User) || !instance.Password.equals(Password) || !instance.Server.equals(Server) || !instance.Port.equals(Port))
        {
            instance = new EmailService(Email, User, Password, Server, Port);
        }
        return instance;
    }
    public static synchronized EmailService getInstance()
    {
        return instance;
    }

    public void sendEmail(String receiver, String subject, String body) throws MessagingException {
        Properties properties  = new Properties();
        properties.put("mail.smtp.host", Server);
        properties.put("mail.smtp.port", Port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        logger.info("Sending email to " + receiver);

        Session session = Session.getDefaultInstance(properties, new Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(User, Password);
            }
        });

        try (Transport transport = session.getTransport("smtp"))
        {
            {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(Email));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
                message.setSubject(subject);
                // message.setText(body);
                message.setContent(body, "text/html; charset=UTF-8");

                transport.connect(Server, Integer.parseInt(Port), User, Password);
                transport.sendMessage(message, message.getAllRecipients());
            }
        }
        catch (MessagingException e)
        {
            logger.info("Failed to send email to " + receiver + " because of " + e.getMessage());
        }
    }


}
