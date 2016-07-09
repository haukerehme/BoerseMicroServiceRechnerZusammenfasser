package de.hrs.Rechner;

import de.hrs.mail.MailService;
import de.hrs.model.TradeMessage;
import de.hrs.model.Tradevorhersage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by hrs on 21.06.16.
 */
public class AnalyseMehereVergleichsstrecken {

    public static Logger log = LoggerFactory.getLogger(AnalyseMehereVergleichsstrecken.class);
    RechnerZusammenfasser rechner;
    List<Integer> closewerte;
    Timestamp now;
    int ausgangspkt,vergleichsLaenge,auswertungslaenge;
    boolean longPosition, mehrereVergleichsstrecken, SimulatorModus;
    int zusammenfasserInterval;
    int spread;
    String instrument;
    List<Integer> listVergleichsLaenge;

//    int GewinnzaehlerLong = 0;
//    int VerlustzaehlerLong = 0;
//
//    int GewinnzaehlerShort = 0;
//    int VerlustzaehlerShort = 0;
//
//    int GenerellPlus = 0;
//    int GenerellMinus = 0;
//    int hohesMinus = 0;
//    int hohesPlus  = 0;
//
//    int hoherLongVerlust = 0;
//    int geringerLongGewinn = 0;
//    int mittlererLongGewinn = 0;
//    int hoherLongGewinn = 0;
//    int sehrHoherLongGewinn = 0;
//
//    int geringerShortGewinn = 0;
//    int mittlererShortGewinn = 0;
//    int hoherShortGewinn = 0;
//    int sehrHoherShortGewinn = 0;
//    int hoherShortVerlust = 0;
//    int anzFormFound = 0;

    public AnalyseMehereVergleichsstrecken(Timestamp now, List<Integer> intArray,int ausgangspkt,List<Integer> listVergleichsLaenge,int auswertungslaenge, int spread, String instrument){
        closewerte = intArray;
        this.now = now;
        this.ausgangspkt =ausgangspkt;
        this.listVergleichsLaenge = listVergleichsLaenge;
        this.auswertungslaenge = auswertungslaenge;
        this.spread = spread;
        this.instrument = instrument;
    }

//    void addiere(Tradevorhersage tmp){
//        GewinnzaehlerLong += tmp.getGewinnzaehlerLong();
//        VerlustzaehlerLong += tmp.getVerlustzaehlerLong();
//
//        GewinnzaehlerShort += tmp.getGewinnzaehlerShort();
//        VerlustzaehlerShort += tmp.getVerlustzaehlerShort();
//
//        GenerellPlus += tmp.getGenerellPlus();
//        GenerellMinus += tmp.getGenerellMinus();
//        hohesMinus += tmp.getHohesMinus();
//        hohesPlus  += tmp.getHohesPlus();
//
//        hoherLongVerlust += tmp.getHoherLongVerlust();
//        geringerLongGewinn += tmp.getGeringerLongGewinn();
//        mittlererLongGewinn += tmp.getMittlererLongGewinn();
//        hoherLongGewinn += tmp.getHoherLongGewinn();
//        sehrHoherLongGewinn += tmp.getSehrHoherLongGewinn();
//
//        geringerShortGewinn += tmp.getGeringerShortGewinn();
//        mittlererShortGewinn += tmp.getMittlererShortGewinn();
//        hoherShortGewinn += tmp.getHoherShortGewinn();
//        sehrHoherShortGewinn += tmp.getSehrHoherShortGewinn();
//        hoherShortVerlust += tmp.getHoherShortVerlust();
//        anzFormFound += tmp.getAnzFormFound();
//    }

    public void run(){
        Tradevorhersage tradevorhersageGes;
        Tradevorhersage tradeTmp;
        rechner = new RechnerZusammenfasser(this.closewerte, this.closewerte.size()-1, 240, auswertungslaenge, 30,spread,"EUR/USD",true,false);
        tradevorhersageGes = rechner.analyse(/*this.closewerte, this.closewerte.size()-1, 240, auswertungslaenge*/);
//        addiere(tradeTmp);

        rechner = new RechnerZusammenfasser(this.closewerte, this.closewerte.size()-1, 210, auswertungslaenge, 30,spread,"EUR/USD",true,false);
        tradeTmp = rechner.analyse(/*this.closewerte, this.closewerte.size()-1, 240, auswertungslaenge*/);
        tradevorhersageGes.addiere(tradeTmp);

        rechner = new RechnerZusammenfasser(this.closewerte, this.closewerte.size()-1, 180, auswertungslaenge, 20,spread,"EUR/USD",true,false);
        tradeTmp = rechner.analyse(/*this.closewerte, this.closewerte.size()-1, 240, auswertungslaenge*/);
        tradevorhersageGes = tradevorhersageGes.addiere(tradeTmp);

        rechner = new RechnerZusammenfasser(this.closewerte, this.closewerte.size()-1, 150, auswertungslaenge, 10,spread,"EUR/USD",true,false);
        tradeTmp = rechner.analyse(/*this.closewerte, this.closewerte.size()-1, 240, auswertungslaenge*/);
        tradevorhersageGes = tradevorhersageGes.addiere(tradeTmp);

        rechner = new RechnerZusammenfasser(this.closewerte, this.closewerte.size()-1, 120, auswertungslaenge, 10,spread,"EUR/USD",true,false);
        tradeTmp = rechner.analyse(/*this.closewerte, this.closewerte.size()-1, 240, auswertungslaenge*/);
        tradevorhersageGes = tradevorhersageGes.addiere(tradeTmp);
        log.info(" Formation "+tradevorhersageGes.getAnzFormFound()+" mal gefunden");

        TradeMessage tradeMessage = new TradeMessage(now, "EUR/USD", auswertungslaenge, tradevorhersageGes.getAnzFormFound(), tradevorhersageGes.getGewinnzaehlerLong(), tradevorhersageGes.getMittlererLongGewinn(), tradevorhersageGes.getHoherLongGewinn(), tradevorhersageGes.getSehrHoherLongGewinn(), tradevorhersageGes.getVerlustzaehlerLong(), tradevorhersageGes.getHoherLongVerlust(), tradevorhersageGes.getGeringerShortGewinn(), tradevorhersageGes.getMittlererShortGewinn(), tradevorhersageGes.getHoherShortGewinn(), tradevorhersageGes.getSehrHoherShortGewinn(), tradevorhersageGes.getVerlustzaehlerShort(), tradevorhersageGes.getHoherShortVerlust());

        /*try {
            tradeMessage.persistTradeMessage();
            //    public TradeMessage(Timestamp id, String instrument, int timeperiod, int anzFound, int longWin, int longWinMiddle, int longWinHigh, int longWinVeryHigh, int longLose,
            //int longLoseHigh, int shortWin, int shortWinMiddle, int shortWinHigh, int shortWinVeryHigh, int shortLose, int shortLoseHigh) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AnalyseMehererVergleichsstrecken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnalyseMehererVergleichsstrecken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AnalyseMehererVergleichsstrecken.class.getName()).log(Level.SEVERE, null, ex);
        }*/


        if(
                tradevorhersageGes.getAnzFormFound()>19 &&
                (tradevorhersageGes.getGewinnzaehlerLong() > tradevorhersageGes.getVerlustzaehlerLong()*2 ||
                tradevorhersageGes.getGewinnzaehlerShort() > tradevorhersageGes.getVerlustzaehlerShort()*2 ||
                (tradevorhersageGes.getHoherLongGewinn() > tradevorhersageGes.getHoherLongVerlust()*2 &&
                        tradevorhersageGes.getHoherLongGewinn() > 4 )||
                (tradevorhersageGes.getHoherShortGewinn() > tradevorhersageGes.getHoherShortVerlust()*2 &&
                        tradevorhersageGes.getHoherShortGewinn() > 4))){
            
            MailService.getInstance().sendMail(tradevorhersageGes);

//            String ausgabe = "";
//            if(this.spread == 1){
//                ausgabe += "\033[34mTRADEN: Mehrere Vergleichslaengen ;) Instrument: "+this.instrument+" "+this.auswertungslaenge+"min\033[0m";
//            }else{
//                ausgabe += "\033[32mTRADEN: Mehrere Vergleichslaengen ;) Instrument: "+this.instrument+ " "+this.auswertungslaenge+"min\033[0m";
//            }
//            ausgabe += "\nLong:   GEWINN: "+GewinnzaehlerLong+"/"+anzFormFound+" , "+sehrHoherLongGewinn+"/"+GewinnzaehlerLong+" , "+hoherLongGewinn+"/"+GewinnzaehlerLong+" , "+mittlererLongGewinn+"/"+GewinnzaehlerLong+" , "+geringerLongGewinn+"/"+GewinnzaehlerLong;
//            ausgabe += "\nLong:   VERLUST: "+VerlustzaehlerLong+"/"+anzFormFound+" , "+hoherLongVerlust+"/"+VerlustzaehlerLong;
//
//            ausgabe += "\nShort:   GEWINN: "+GewinnzaehlerShort+"/"+anzFormFound+" , "+sehrHoherShortGewinn+"/"+GewinnzaehlerShort+" , "+hoherShortGewinn+"/"+GewinnzaehlerShort+" , "+mittlererShortGewinn+"/"+GewinnzaehlerShort+" , "+geringerShortGewinn+"/"+GewinnzaehlerShort;
//            ausgabe += "\nShort:   VERLUST: "+VerlustzaehlerShort+"/"+anzFormFound+" , "+hoherShortVerlust+"/"+VerlustzaehlerShort+"\n";
//            log.info(ausgabe);
//            try {
//                MailClass.sendMail("haukekatha","43mitmilch","hrs@logentis.de","lemur.katha@googlemail.com","Handeln",ausgabe);
//            } catch (MessagingException ex) {
//                log.error("Fehler beim Mail senden. Error: {}", ex.toString());
//            }
        }
    }

}
