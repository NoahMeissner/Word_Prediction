package Training;

import Training.normalisieren.CleanText;
import lingologs.Script;
import lingologs.Texture;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PreProcessing {

    /*
        This Script will Get the Data from the Processor
        The Script will Prepare the Data for the Training Process
        The Pipeline will Normalise the Words in the Text, the separate the Text in each Sentence.
        The cut it in to N - Grams and safe the Text as Uni-grams and Bi-grams

        // TODO Normalisierungspipeline
    // TODO Zeilenumbrueche Zitate etc. loeschen alles was zwischen klammern ist etc
    // Gross und kleinschreibung bleibt drinnen
    // TODO Abkuerzungen ersetzen & and etc.
     */
    private Script text;
    Texture<Script> T;

    public PreProcessing(Script text)
    {
        this.text = text;
    }

    public void start(){
        Script S = normaliseText();
        System.out.println("10");
        List<Script> D = cutIntoNgrams(S).toList();
        System.out.println(D);
    }

    private Script normaliseText()
    {
        return new CleanText(text).getCleanText();
    }


    private Texture<Script> cutIntoNgrams(Script S)
    {
        String R = "\\.|\\n+";
        Script D = S.replace(R," . ");
        String F = "\\,";
        Script H = D.replace(F," ,");
        Texture<Script> result = new Texture<>(H.split(" "));
        return result;
    }

    private void safeUniGramList()
    {

    }

    private void safeBiGramList()
    {

    }


}
