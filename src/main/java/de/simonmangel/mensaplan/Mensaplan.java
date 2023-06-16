package de.simonmangel.mensaplan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
        
        boolean isToday = weekday.equals("Heute");
        Element dayCard =  isToday ? page.selectFirst(".active-panel") : page.selectFirst("#"+weekday);

        if(dayCard == null) {
            System.err.printf("Error: Keine Essensdaten für \"%s\"!\n",weekday);
            System.exit(1);
        }

        String parsedWeekday = dayCard.parent().children().get(0).text();


        // Parse menues
        Element dayMenu = dayCard.selectFirst(".menues");
        Elements menues = dayMenu.select("tr");
        Elements sups = menues.select("sup");
        for (Element e:sups) {
            e.remove();
        }
        List<Result.MenuEntry> menuEntries = new ArrayList<>(menues.size());
        for (int i = 0; i < menues.size(); i++) {
            String type = menues.get(i).selectFirst(".menue-item.menue-category")!= null ?
                    menues.get(i).selectFirst(".menue-item.menue-category").text() : "N/A";
            String meal = menues.get(i).selectFirst(".menue-item.menue-desc").selectFirst(".expand-nutr")!=null ?
                    menues.get(i).selectFirst(".menue-item.menue-desc").selectFirst(".expand-nutr").text().substring(1) : "N/A";
            String price = menues.get(i).selectFirst(".menue-item.menue-price.large-price") != null ?
                    menues.get(i).selectFirst(".menue-item.menue-price.large-price").text() : "N/A";
            menuEntries.add(new Result.MenuEntry(type, meal, price));
        }

        Elements extras = dayCard.selectFirst(".extras").select("tr");
        extras.select("sup").remove();
        List<Result.ExtraEntry> extraEntries = new ArrayList<>(extras.size());
        for (int i = 0; i<extras.size(); i++){
            Element extraTypeElt = extras.get(i).selectFirst(".menue-item.menue-category");
            String type = extraTypeElt != null ? extraTypeElt.text() : "N/A";

            Element extraMealElt = extras.get(i).selectFirst(".menue-item.menue-desc");
            String meal;
            if (extraMealElt != null) {
                extraMealElt.select(".seperator").prepend(" ").append(" ");
                meal = extraMealElt.text().substring(1);
            } else
                meal = "N/A";
            
            extraEntries.add(new Result.ExtraEntry(type, meal));
        }

        Result result = new Result(parsedWeekday, isToday, mensa, menuEntries, extraEntries);
        printResult(result);

    }

    private static void printResult(Result result) {
        // Print header line
        String headerLine = String.format("%s%s in der Mensa %s:", result.isToday() ? "Heute, " : "", result.getWeekday(), result.getMensa().getName());
        System.out.println(headerLine);

        // Print table
        AsciiTable at = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        at.getRenderer().setCWC(cwc);

        // Top rule
        at.addRule();


        // Table header
        at.addRow("Typ", "Gericht", "Preis");

        // Midrule 1
        at.addRule();

        // Menu entries
        for(Result.MenuEntry e: result.getMenuEntries()) {
            at.addRow(e.getType(), e.getMeal(), e.getPrice());
        }

        // Midrule 2
        at.addRule();

        // Extra entries
        for (Result.ExtraEntry e: result.getExtraEntries()) {
            at.addRow(e.getType(), e.getMeal(), "");
        }

        // Bottom rule
        at.addRule();

        // Render and print
        System.out.println(at.render(128));

    }

}
