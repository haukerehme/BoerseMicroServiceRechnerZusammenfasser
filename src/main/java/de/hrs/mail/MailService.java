package de.hrs.mail;
import de.hrs.model.TradeMessage;
import de.hrs.model.Tradevorhersage;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
/**
 * Created by hrs on 09.07.16.
 */
public class MailService {
    private MailSender mailSender;
    private SimpleMailMessage templateMessage;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

    public void sendMail(Tradevorhersage tradevorhersage){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("haukekatha@gmail.com");
        msg.setTo("hrs@logentis.de");
        msg.setText(tradevorhersage.toMailMessage("EUR/USD",20));

        try{
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }
}
