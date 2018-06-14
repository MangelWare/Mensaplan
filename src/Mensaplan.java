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
        for (int i = 0; i < menues.size(); i++) {
            typ[i] = menues.get(i).selectFirst(".menue-item.menue-category").text();
            gericht[i] = menues.get(i).selectFirst(".menue-item.menue-desc").selectFirst(".expand-nutr").text().substring(1);
        }

        System.out.println("----------------------------------------------------------------------------------------");
        System.out.print("Typ");
        for (int j = 0; j < maxLength(typ)-1; j++) {
            System.out.print(" ");
        }
        System.out.println("Gericht");
        System.out.println("----------------------------------------------------------------------------------------");


        for (int i = 0; i <menues.size() ; i++) {
            System.out.print(typ[i]+":");
            for (int j = 0; j < maxLength(typ)+1-typ[i].length(); j++) {
                System.out.print(" ");
            }
            System.out.println(gericht[i]);
        }
    }




}
