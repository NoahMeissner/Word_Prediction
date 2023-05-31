package Training.normalisieren;

import lingologs.Script;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CleanText {
    /*
    Dieses Skript soll
    1. Leezeichen und Absaetze loeschen
    2. unerwuenschte Satzzeichen
    3. Urls und Hyperlinks
    4. Sonderzeichen und Symbole
    5. Formatierungen und HTML Tags
    6. TODO Stoppwoerter
     */

    // Dieser Regex Ausdruck filter html Tags, urls Sonderzeichen (ausser punkt und komma) Absaetze heraus
    Script regex = Script.of("[^\\p{L}\\p{N}\\s.,]|\\b((?:https?|ftp):\\S+)|<[^>]+>|\\\\n\\\\s*\\\\n");
    // Absaetze und Leer
    Script leer = Script.of("\\s{2,}"); // compact

    Script abveriations = Script.of("\\b[A-Z]+\\.");
    private final Script S;
    private Script result;
    public CleanText(Script S)
    {
        this.S = S;
    }

    public Script getCleanText(){
        result = S.replace(regex,"").replace(leer," ").replace(abveriations,"");
        return result;
    }

    private void cleansimple()
    {
        result = S.replace(regex,"").replace(leer," ").replace(abveriations,"");
    }
}
