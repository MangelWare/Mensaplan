package de.simonmangel.mensaplan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

public class Mensaplan {
    public static void main(String[] args) {

        try {
            String day = "Heute";
            RWTHMensa mensa = RWTHMensa.ACADEMICA;

            Stack<LinkedList<String>> arguments = new Stack<>();
            for(int i = 0; i<args.length;i++) {
                if (args[i].startsWith("-")) {
                    LinkedList<String> l = new LinkedList<String>(){
                        @Override
                        public String toString() {
                            if(!this.isEmpty()){
                                return this.removeFirst() + this.toString();
                            } else { return ""; }
                        }
                    };
                    l.add(args[i]);
                    arguments.push(l);
                } else {
                    if(!arguments.isEmpty())
                        arguments.peek().add(args[i]);
                }
            }

            while(!arguments.isEmpty()) {
                LinkedList<String> l = arguments.pop();
                String cmd = l.removeFirst();
                String options = l.toString();

                switch(cmd) {
                    case "-d":   day = options;
                                break;
                    case "-m":   mensa = RWTHMensa.closestMensa(options);
                                break;
                    default:    System.err.println("Invalid Argument: "+cmd+"\nTry -h for help!");
                                System.exit(1);
                }
            }

            if(!isWeekDay(day))
                throw new NotAWeekdayException(day);

            printDayMenu(day,mensa);

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

    private static void printDayMenu(String weekday,RWTHMensa mensa) throws IOException {
        Document page = Jsoup.connect("https://www.studierendenwerk-aachen.de/speiseplaene/"+mensa.name().toLowerCase()+"-w.html").get();
        Element dayCard = weekday.equals("Heute") ? page.selectFirst(".active-panel") : page.selectFirst("#"+weekday);

        if(dayCard == null) {
            System.err.printf("Error: Keine Essensdaten für \"%s\"!\n",weekday);
            System.exit(1);
        }

        if(weekday.equals("Heute"))
            System.out.print("Heute, ");
        System.out.println(dayCard.parent().children().get(0).text()+" in der Mensa "+mensa.getName()+":");

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

        Elements extras = dayCard.selectFirst(".extras").select("tr");
        extras.select("sup").remove();
        String[] extraTypes = new String[extras.size()];
        String[] extraMeals = new String[extras.size()];
        for (int i = 0; i<extras.size(); i++){
            Element extraTypeElt = extras.get(i).selectFirst(".menue-item.menue-category");
            extraTypes[i] = extraTypeElt != null ? extraTypeElt.text() : "N/A";

            Element extraMealElt = extras.get(i).selectFirst(".menue-item.menue-desc");
            if (extraMealElt != null) {
                extraMealElt.select(".seperator").prepend(" ").append(" ");
                extraMeals[i] = extraMealElt.text().substring(1);
            } else
                extraMeals[i] = "N/A";
            
        }

        int vLineLen = maxLength(typ)+maxLength(gericht)+maxLength(preis)+5;
        String vLine = "-".repeat(vLineLen);

        System.out.println(vLine);
        System.out.print("Typ");
        for (int j = 0; j < maxLength(typ)-1; j++) {
            System.out.print(" ");
        }
        System.out.print("Gericht");
        for (int j = 0; j < maxLength(gericht)-6; j++) {
            System.out.print(" ");
        }
        System.out.println("Preis");

        System.out.println(vLine);

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

        System.out.println(vLine);

        for (int i = 0; i<extras.size(); i++) {
            System.out.printf(
                "%s:%s%s\n",
                extraTypes[i],
                " ".repeat(maxLength(typ)+1-extraTypes[i].length()),
                extraMeals[i]
            );
        }

        System.out.println(vLine);
    }




}
