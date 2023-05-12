package de.simonmangel.mensaplan;

class NotAWeekdayException extends Exception {
    private String s;
    NotAWeekdayException(String s) {
        this.s = s;
    }

    public void printWeekday() {
        System.err.println("Error: \""+s+"\" ist kein Wochentag!");
    }
}