# Programmieren II - Projekt Werwolf #

Gruppe: Lukas Niehus, Phu Bac Duong, Penske Williano, Daniel Gaist


## Inhaltsverzeichnis ##

Das Projekt  
Bedienungsanleitung  
Erweiterungsmöglichkeiten  
Anlagen


## Das Projekt ##

Das Ziel des Projektes war, das Gemeinschaftsspiel „Die Werwölfe vom Düsterwald“ bzw. „Town of Salem“ in der Java-Programmiersprache zu implementieren, mit einer JavaFX-GUI zu versehen und ein Spiel in einem Netzwerk aus mehreren Rechner spielbar zu machen. Des Weiteren sollten mindestens vier Rollen im implementierten Spiel enthalten sein. Zudem soll ein Chat, über den Spieler kommunizieren können, realisiert werden.

Um kurz die Regeln des Spiels zu erklären:

An diesem Spiel nehmen mehrere Spieler teil, die jeweils eine Rolle im Spiel einnehmen, welche eine der beiden Hauptgruppierungen repräsentieren. Ziel der beiden Gruppierungen, welche Menschen und Werwölfe genannt werden, ist es, die jeweils andere Gruppe zu eliminieren. Als Spielerzahl empfehlen wir vier bis dreißig Spieler. Die Rollen werden zufällig zugewiesen und die Anzahl der Werwölfe ist abhängig von der Spielerzahl. Implementierte Rollen sind Seher/in, Hexe/r, Werwolf und Dorfbewohner/in.

Die Menschen (Dorfbewohner, Hexe/r, Seher/in) gewinnen, wenn sie es schaffen, dass alle Werwölfe aus dem Spiel ausscheiden, die Werwölfe hingegen gewinnen, sobald alle im Spiel verbliebenen Spieler Werwölfe sind. Um diese Ziele zu erreichen, können die Spieler im Tag-Nacht Zyklus verschiedene Aktionen ausführen.

In der Nacht dürfen die Werwölfe gemeinsam ein Ziel auswählen, welches am nächsten Tag aus dem Spiel ausscheidet. Zudem darf der Seher jede Nacht einen Spieler auswählen und dessen Rolle erfahren. Den Abschluss der Nacht bildet die Hexe. Sie besitzt zwei Aktionen, die sie jeweils einmal pro Spiel ausführen darf. Einmal darf sie einen beliebigen Spieler auswählen, welcher aus dem Spiel ausscheidet. Außerdem erfährt sie jede Nacht das Ziel der Werwölfe und darf einmal verhindern, dass der von den Werwölfen gewählte Spieler aus dem Spiel ausscheidet.





- ***Aufgabenverteilung***

**Lukas Niehus** war dafür zuständig, die Logik des Spiels zu implementieren. Dies beinhaltet alle Grundfunktionalitäten, wie beispielsweise das Erstellen eines Spiels und die Einführung des Tag-Nacht-Zyklus mit Ausführung aller rollenabhängigen Aktionen.

**Penske Williano** hat die GUI des Spiels entworfen. Sowohl das Design der Spieloberfläche, als auch die Umsetzung der Logik mittels eines User-Interfaces war sein Aufgabenbereich.

**Daniel Gaist** und **Phu Bac Duong** als Netzwerk-Team haben das Spielen mehrerer Spieler auf verschiedenen Rechnern ermöglicht. Des Weiteren haben sie eine Kommunikationsmöglichkeit in Form eines Chats zwischen den Spieler ermöglicht.

**Ziel der Aufteilung:** Nachdem alle ihre Aufgaben erfüllt hatten, wurden die einzelnen Teile zusammengeführt, um ein komplett funktionierendes Programm zu besitzen.



- ***Durchführung des Projekts***

**Die Erstellung der Logik**  
Der erste Schritt war, dass die das Grundkonzept als Konsolenspiel entworfen wurde. Abstimmungen und Aktionen wurden mittels Scanner eingegeben und das Spiel wurde auf 4 Spieler festgesetzt.Danach wurde eine zufällige Rollenverteilung und eine variable Spieleranzahl implementiert.

**Verknüpfung mit der GUI**  
Da die Logik zu diesem Zeitpunkt schon vollständig implementiert war, haben wir die verschiedenen Aktionen versucht, durch die GUI zu steuern. Hierfür musste Scanner ersetzt werden durch eine Auswahl mittels Tabelle.

**Verknüpfung mit dem Netzwerk**  
Zuerst wurde nur ein Chat implementiert, welche mehrere Teilnehmer erlaubt. Dabei wurde nur das Terminal und nicht eine GUI verwendet. Um dies zu ermöglichen benötigt man „ServerSockets“ und „Sockets“. Während dem ServerSocket ein Port als Integer übergeben wird, muss dem Socket eine IP in Form von einer Zahlenkombination oder dem Namen des Hosts (als String) und der dem ServerSocket übergebenen Port angegeben werden.  
Mit der aktuellen Form des Netzwerkes kann man nur eine Nachricht vom Socket zum ServerSocket hin- und zurückschicken. Um eine Kommunikationsmöglichkeit untereinander zu ermöglichen, werden „Threads“ benötigt. Threads zeichnen sich damit aus, dass man mehrere Aufgaben beinahe parallel ausführen kann.



- ***Details***

**Das Voting-System:**  
Die Abstimmung der Spieler wird gesteuert durch einen „Vote“-Button, der beim Drücken eine Stimme gegen einen Spieler speichert. Die Auswahl des Spielers erfolgt über eine Tabelle, welche alle am Spiel teilhabenden Spieler abbildet. Sobald jeder Spieler abgestimmt hat, erfolgt eine Auswertung, welche den Spieler aus dem Spiel ausscheiden lässt, der die meisten Stimmen gegen sich hat.

**Der Seher:**  
Jeder Spieler besitzt ein Attribut des Typs String, in dem die Rolle des Spielers gespeichert ist. Durch eine Tabelle kann der Seher jede Nacht eine Person auswählen, dessen Rolle er erfahren möchte. Dies geschieht über einen „Check“-Button, welchen nur der Seher aktiviert bekommt. Der Server würde die Rolle dementsprechend an den Client des Sehers übermitteln. Der Seher hat zudem die Möglichkeit, seine Aktion durch einen „Skip“-Button zu überspringen.

**Die Werwölfe:**  
Die Werwölfe benutzen zum Voten einen „Hunt“-Button, welcher das Voting-System als Grundlage benutzt, um das Ziel der Werwölfe zu bestimmen. Die Werwolf-Phase funktioniert dementsprechend wie eine Dorfabstimmung über das Voting-System, mit der Einschränkung, dass nur Werwölfe an der Abstimmung teilhaben.

**Die Hexe:**  
Die Hexe erfährt jede Nacht das Ziel der Werwölfe. Über einen „Heal“-Button kann sie verhindern, dass das Ziel der Werwölfe aus dem Spiel ausscheidet. Außerdem kann sie über eine Tabelle einen Spieler auswählen und ihn durch Betätigung eines „Kill“-Buttons aus dem Spiel ausscheiden lassen.  
Beide dieser Buttons werden nach einmaligem Betätigen für den Rest des Spiels deaktiviert.
Zusätzlich besitzt die Hexe wie der Seher die Möglichkeit, seine Aktion durch einen „Skip“-Button zu überspringen.

**Das Netzwerk:**  
Ein Netzwerk besteht aus einem Server und maximal dreißig Clients, wobei diese Zahl im Quellcode variabel änderbar ist. Der Host startet einen Client selbst und kann daher am Spiel direkt teilnehmen. Dies wurde durch Threads ermöglicht, welche in das Programm implementiert wurden. Der Server erstellt für jeden Client einen eigenen Thread. Diese Threads verwalten das Nachrichtensenden und Empfangen für die Clients. Diese werden durch OutputStreamReader und InputStreamReader ermöglicht. Der Serveraufbau basiert auf dem eines Echo-Servers und bekommt daher Aktionen von den Clients, welche an die anderen Clients weitergeschickt wird. Die Logik für das Spiel wird beim Server verwaltet. Die Clients haben keinen Zugriff auf die Logik. Die Aktion der Clientseiten wird durch einen Protocol ausgewertet. Der Client wird dementsprechend eine Rückmeldung bekommen. Der aktuelle Zustand des Spiels wird mit Hilfe von Gson an Clients geschickt.

**Die GUI:**  
Die GUI wurde mithilfe von JavaFX und Scene Builder erstellt. FXML, eine XML-basierte Sprache, dient als die Präsentation (View) in unserem MVC-Modell. Die FXML-Elemente werden durch einen Controller gesteuert. CSS wurde auch verwendet, um dem Startbildschirm ein Hintergrundbild zu geben.  
Der Spieler kann einen anderen Spieler durch die Tabelle auswählen und verschiedene Aktionen durch die Buttons durchführen. Es gibt auch eine Chat-Box, indem Spieler mit anderen Spielern kommunizieren können.


- ***Probleme des Projekts***

Das erste große Problem, das auftrat war, dass die Logik zuerst über den Einsatz von Scanner implementiert wurde. Diese Funktion konnten wir allerdings nicht durch die GUI ansteuern, weshalb die Logik umgeschrieben werden musste, um die Logik komplett durch die GUI zu kontrollieren.  
Dieses Problem konnte behoben werden, indem man anstatt Scanner zu verwenden, eine Tabelle in die GUI einfügt, um einen Spieler auszuwählen.  
Die Aktionen, bei denen ein Spieler ausgewählt werden muss, können dann durch die Tabelle und über Buttons ausgeführt werden. Somit war der Einsatz von Scanner nicht mehr notwendig.  
Ein Problem ist, dass man in der Tabelle der Spieler einmal scrollen muss, um den Status der Spieler zu aktualisieren. Dies konnten wir bis jetzt noch nicht lösen.  
Was immer eine Problematik war, dass man die GUI nie als perfekt für den aktuellen Status des Programmes angesehen hat und man daher immer etwas Zeit für die Anpassung der GUI investieren musste.  
Bevor ein Spiel beigetreten werden kann, benötigt man die Daten des Hosts. Diese kann man nur extern beim Host selbst in Form einer anderen Kommunikationsmöglichkeit erfragen. Für den Client selbst wird nicht angezeigt, welche Spiele er beitreten kann.  
Ein weiteres Problem war, die Verknüpfung von Server ( und Clients )  mit der GUI zu erstellen. Die GUI hing während der Server gelaufen ist. Dieses Problem konnte behoben werden, indem man neue Threads für Server und Clients erstellte und diese Threads parallel mit JavaFX-Thread laufen lässt.  
Ein weiteres Problem beim Testen war, dass man den Prozess mit dem verbundenen Port manuell schließen muss, damit man wieder diesen Port benutzen kann. Dieses Problem wurde behoben durch passende Behandlung, wenn man die App schließt.  
Ein großes Problem war widerum, dass man die Exception erkennen und dementsprechend eine Behandlung dafür vorbereiten musste. Nicht alle Exceptions wurden behandelt, da es nicht so leicht war, alle Exceptions zu identifizieren.  
Das letzte Problem war, die Spieler zu verwalten. Der Server erkennt jeden Client durch seinen Namen, jedoch kann er nicht feststellen, ob mehrere Clients den gleichen Namen besitzen.


## Bedienungsanleitung ##


- ***Installation***

Das Spiel liegt als .JAR-Datei vor und kann dementsprechend über bekannte Methoden gestartet werden.

- ***Start***

Einer der Spieler muss der Host sein und dieser erstellt ein neues Spiel. Über Eingabe der IP und des Ports können andere Spieler dem Spiel beitreten. Außerdem muss jeder Spieler einen Namen eingeben, um im Spiel identifiziert zu werden. Sobald mindestens vier Spieler beigetreten sind, kann ein Spiel gestartet werden. Bei Start des Spiels wird jedem Spieler mit einem zufallsbasierten Verfahren eine Rolle zugewiesen, sodass es genau einen Seher, genau eine Hexe und eine errechnete Anzahl an Werwölfen gibt. Das Spiel startet mit der Nacht-Phase.

- ***Spielgeschehen***

In den Nacht-Phasen kann jeder Spieler eine Aktion basierend auf seiner Rolle ausführen. Die Auswahl von Spielern, die in dieser Aktion getroffen werden, erfolgt über eine Tabelle und die Aktion kann dann über einen Button ausgeführt werden. Je nach Rolle sind unterschiedliche Buttons freigeschaltet. Beispielsweise besitzt der Seher die Möglichkeit, den „Check“-Button oder den „Skip“-Button zu benutzen.  
In den Tag-Phasen hat jeder Spieler die Pflicht, eine Person aus der Tabelle auszuwählen und mittels des „Vote“-Buttons für diese Person abzustimmen. Sobald jeder Spieler abgestimmt hat, wird der Server einen der Spieler bestimmen, für die am häufigsten abgestimmt wurde, und diesen aus dem Spiel ausscheiden lassen. Dann beginnt die nächste Nacht-Phase.  
Unabhängig von Tag- oder Nacht-Phase kann ein Chat zur Kommunikation verwendet werden. Dafür gibt es ein Eingabefeld und einen „Send“-Button (mit ENTER kann keine Nachricht versendet werden!).  
Wenn ein Spieler aus dem Spiel ausscheidet, kann er weiterhin dem Spielgeschehen zuschauen oder den Chat verwenden, aber er wird keinen Einfluss mehr auf Aktionen in der Tag- bzw. Nacht-Phase haben.  
Das Spiel endet, sobald eine der beiden Fraktionen (Werwölfe oder Menschen) komplett aus dem Spiel ausgeschieden ist.


## Erweiterungsmöglichkeiten ##

Da das Originalspiel („Die Werwölfe vom Düsterwald“) eine breite Variation an Rollen besitzt, könnte man das Spiel funktional so erweitern, dass weitere Rollen zu der bereits bestehenden Auswahl hinzugefügt werden könnten. Diese wären beispielsweise Jäger, Blinzelmädchen, Amor und Ähnliche. Andere Spielvorgänge aus dem Original-Spiel, wie die Wahl eines Dorfhauptmannes wäre ebenfalls eine mögliche Erweiterung.  
Eine weitere Erweiterung wäre die Implementierung eines Gruppen-Chats, beispielsweise für die Werwölfe.  
Für den Fall, dass sich nur eine geringe Spieleranzahl findet, wäre eine gute Option, dass zusätzliche Spieler mithilfe einer AI hinzugefügt werden könnten. Diese müssten ihre Rollen so einhalten, dass die aufgrund von zufälligen Entscheidungen die Möglichkeiten ihrer Rolle im vollen Umfang benutzen.  
Ein weiterer Punkt über den man nachdenken konnte, ist die Tatsache, dass aktuell jeder einem Spiel beitreten kann, solange er den Port und die IP des Hosts kennt. Deshalb könnte man noch ein Kennwort geschütztes Spiel bzw. Server erstellen.  
Um die Übersicht im Spiel zu behalten, hätte man eine extra „TextArea“ für die Aktionen erstellen können. Aktuell ist die einzige „TextArea“ für den Chat und die Informationen der Aktionen verantwortlich.  
Um Verwirrung im Spiel zu vermeiden, könnte man noch eine Überprüfung der im Server aktuell angemeldeten Namen implementieren. Damit Spieler nicht denselben Namen haben können.


## Anlagen ##

Bild für den Login-Screen:  
https://www.panic-mode.de/wp-content/uploads/2015/03/Werewolf_HD.jpg  
![](https://www.panic-mode.de/wp-content/uploads/2015/03/Werewolf_HD.jpg)  

Hintergrundbild im Spiel-Screen:
https://images.alphacoders.com/434/434772.jpg  
![](https://images.alphacoders.com/434/434772.jpg)  
