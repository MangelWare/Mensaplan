package de.simonmangel.mensaplan;

public enum RWTHMensa {
    ACADEMICA("Academica","Mensa Academica"),
    AHORNSTRASSE("Ahornstrasse", "Mensa Ahornstrasse"),
    TEMPLERGRABEN("Templergraben", "Mensa Templergraben"),
    BAYERNALLEE("Bayernallee", "Mensa Bayernallee"),
    EUPENERSTRASSE("Eupener Strasse", "Mensa Eupener Strasse"),
    GOETHESTRASSE("Goethestrasse", "Mensa Goethestrasse"),
    SUEDPARK("Suedpark", "Mensa Suedpark"),
    VITA("Vita","Mensa Vita"),
    JUELICH("Juelich","Mensa Juelich");

    private String name, longName;
    public static final RWTHMensa[] MENSEN = {ACADEMICA,AHORNSTRASSE,TEMPLERGRABEN,BAYERNALLEE,EUPENERSTRASSE,
            GOETHESTRASSE,SUEDPARK,VITA,JUELICH};

    RWTHMensa(String name, String longName) { this.name = name; this.longName = longName;}

    public static RWTHMensa closestMensa(String input) {
        int[] lDScores= new int[MENSEN.length];
        for(int i = 0; i < MENSEN.length; i++) {
            /*lDScores[i] = Math.min(
                    LevenstheinDistance.computeLevenshteinDistance(input,MENSEN[i].longName),
                    LevenstheinDistance.computeLevenshteinDistance(input,MENSEN[i].name)
            );*/
            lDScores[i] = Math.min(
                -LevenstheinDistance.LCSubStr(input.toLowerCase(), MENSEN[i].longName.toLowerCase()),
                -LevenstheinDistance.LCSubStr(input.toLowerCase(), MENSEN[i].name.toLowerCase())
            );
        }
        return MENSEN[LevenstheinDistance.indexOfMinimum(lDScores)];
    }

    public String getName() {
        return name;
    }

    public String getLongName() {
        return longName;
    }
}
