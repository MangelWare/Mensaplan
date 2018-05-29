# Mensaplan
Ein Konsolentool für den Tagesplan der Mensa Academica Aachen

## Voraussetzungen

Du brauchst nur eine funktionierende (am besten halbwegs aktuelle) JRE.


## Download & Benutzung

###Download
Klone einfach das Repository in einen Ordner deiner Wahl.

###Benutzung

####Linux/Mac
Führe das Shell-Skript "mensaplan.sh" im Wurzelverzeichnis des Repository aus. (Sollte es nicht ausführbar sein, benutze "sudo chmod +x mensaplan.sh" um es ausführbar zu machen)

Hinweis: Füge ein beliebiges alias hinzu, das das Skript ausführt, um die Benutzung komfortabler zu machen

####Andere

Hierfür ist noch kein komfortables Shellskript verfügbar. Benutze "java -Dfile.encoding=UTF-8 -classpath <Pfad des Repository>/out/production/Mensaplan:<Pfad des Repository>/jsoup-1.11.2.jar Mensaplan".
