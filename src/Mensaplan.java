import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;

public class Mensaplan {
    public static void main(String[] args) {
        try {
            String day = args.length>0 ? args[0] : "Heute";

            if(!isWeekDay(day))
                throw new NotAWeekdayException(day);

            printDayMenu(day);

        } catch(IOException e) {e.printStackTrace();}
        catch(NotAWeekdayException e) {e.printWeekday();}

    }

    private static boolean isWeekDay(String day) {
        return Arrays.asList(new String[]{"Heute","Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"}).contains(day);
    }

    private static int maxLength(String[] arr) {
        int res = 0;
        for (String s:arr
             ) {
            if(s.length() > res)
                res = s.length();
        }
        return res;
    }

    private static void printDayMenu(String weekday) throws IOException {
        Document page = Jsoup.connect("https://www.studierendenwerk-aachen.de/speiseplaene/academica-w.html").get();
        Element dayCard = weekday.equals("Heute") ? page.selectFirst(".active-panel") : page.selectFirst("#"+weekday);

        if(dayCard == null) {
            System.err.printf("\nError: Keine Essensdaten für \"%s\"!",weekday);
            System.exit(1);
        }

        System.out.println();
        if(weekday.equals("Heute"))
            System.out.print("Heute, ");
        System.out.println(dayCard.parent().children().get(0).text()+" in der Mensa Academica:");

        Element dayMenu = dayCard.selectFirst(".menues");
        Elements menues = dayMenu.select("tr");
        Elements sups = menues.select("sup");
        for (Element e:sups) {
            e.remove();
        }
        String[] typ = new String[menues.size()];
        String[] gericht = new String[menues.size()];
        String[] preis = new String[menues.size()];
        for (int i = 0; i < menues.size(); i++) {
            typ[i] = menues.get(i).selectFirst(".menue-item.menue-category")!= null ?
                    menues.get(i).selectFirst(".menue-item.menue-category").text() : "N/A";
            gericht[i] = menues.get(i).selectFirst(".menue-item.menue-desc").selectFirst(".expand-nutr")!=null ?
                    menues.get(i).selectFirst(".menue-item.menue-desc").selectFirst(".expand-nutr").text().substring(1) : "N/A";
            preis[i] = menues.get(i).selectFirst(".menue-item.menue-price.large-price") != null ?
                    menues.get(i).selectFirst(".menue-item.menue-price.large-price").text() : "N/A";
        }

        for (int i = 0; i < maxLength(typ)+maxLength(gericht)+maxLength(preis)+5; i++) {
            System.out.print("-");
        }
        System.out.println();
        System.out.print("Typ");
        for (int j = 0; j < maxLength(typ)-1; j++) {
            System.out.print(" ");
        }
        System.out.print("Gericht");
        for (int j = 0; j < maxLength(gericht)-6; j++) {
            System.out.print(" ");
        }
        System.out.println("Preis");
        for (int i = 0; i < maxLength(typ)+maxLength(gericht)+maxLength(preis)+5; i++) {
            System.out.print("-");
        }
        System.out.println();

        for (int i = 0; i <menues.size() ; i++) {
            System.out.print(typ[i]+":");
            for (int j = 0; j < maxLength(typ)+1-typ[i].length(); j++) {
                System.out.print(" ");
            }
            System.out.print(gericht[i]);
            for (int j = 0; j < maxLength(gericht)+1-gericht[i].length(); j++) {
                System.out.print(".");
            }
            System.out.println(preis[i]);
        }
    }




}
