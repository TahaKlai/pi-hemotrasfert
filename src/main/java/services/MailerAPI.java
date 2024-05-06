package services;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailerAPI {

    static Session session;
    static Properties properties = new Properties();

    public static void sendVolunteerInformation(String username, String password, String recipient, String volunteerName, String volunteerAddress, String volunteerPhone, String volunteerMail, String volunteerAvailability, String volunteerProfession) {

        String subject = "Volunteer Information";
        String content = "Thank you for volunteering!\n\n"
+ "Here are the details:\n\n" + "Name: " + volunteerName + "\n" + "Address: " + volunteerAddress + "\n"
+ "Phone: " + volunteerPhone + "\n" + "Email: " + volunteerMail + "\n" + "Availability: " + volunteerAvailability + "\n"
 + "Profession: " + volunteerProfession + "\n\n" + "We appreciate your contribution.\n\n" + "Regards,\n"
                + "Vortex Corp";

        try {
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            Message message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress("support@vortex.com", "Vortex")); // Set sender's name here
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(content);

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
