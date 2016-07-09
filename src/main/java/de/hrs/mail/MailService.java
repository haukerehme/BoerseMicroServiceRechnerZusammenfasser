package de.hrs.mail;
import de.hrs.model.TradeMessage;
import de.hrs.model.Tradevorhersage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
/**
 * Created by hrs on 09.07.16.
 */
public class MailService {

    private static MailService ourInstance = null;

    private MailService(){
        ourInstance = this;
    }

    public static MailService getInstance() {
        return ourInstance;
    }

    public static Logger log = LoggerFactory.getLogger(MailService.class);

    private MailSender mailSender;
    private SimpleMailMessage templateMessage;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

    public void sendMailString(String text){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("haukekatha@gmail.com");
        msg.setTo("hrs@logentis.de");
        msg.setSubject("CFD");
        msg.setText(text);

        try{
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            log.error(ex.getMessage());
        }
    }

    public void sendMail(Tradevorhersage tradevorhersage){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("haukekatha@gmail.com");
        msg.setTo("hrs@logentis.de");
        msg.setSubject("CFD");
        msg.setText(tradevorhersage.toMailMessage("EUR/USD",20));

        try{
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            log.error(ex.getMessage());
        }
    }
}
