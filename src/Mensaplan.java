import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Mensaplan {
    public static void main(String[] args) {
        try {
            String day;
            if(args.length>0) {
                day = args[0];
                System.out.print(day);
            } else {
                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(System.currentTimeMillis());
                day = getDayName(cal.get(Calendar.DAY_OF_WEEK));
                System.out.printf("Heute, %s",day);
            }

            System.out.println(" in der Mensa Academica:");

            if(!isWeekDay(day))
                throw new NotAWeekdayException(day);

            printDayMenu(day);

        } catch(IOException e) {e.printStackTrace();}
        catch(NotAWeekdayException e) {e.printWeekday();}

    }

    private static String getDayName(int d) {
        String[] names  = {"Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"};
        return names[(d%7)-1];
    }

    private static boolean isWeekDay(String day) {
        return Arrays.asList(new String[]{"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag","Sonntag"}).contains(day);
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
        Element dayCard = page.selectFirst("#"+weekday);
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
