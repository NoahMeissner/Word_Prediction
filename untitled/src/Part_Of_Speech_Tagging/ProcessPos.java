package Part_Of_Speech_Tagging;

import lingolava.Tuple.Couple;
import lingologs.Script;
import lingologs.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * Key Functionalities:
 * - Processing Text for POS Tagging: This class processes input text and assigns POS tags to individual tokens.
 * - Mode Selection: The tagging mode can be set to probabilistic or rule-based, allowing for different POS tagging
 *   strategies.
 * - Handling N-grams: The class handles the creation and processing of N-grams from input text.
 */

public class ProcessPos {

    private Texture<Script> ST;

    private Texture<Couple<String,String>> TS;

    private final boolean prob;

    // Probabilistic Based
    public ProcessPos(Texture<Script> ST, boolean prob)
    {
        this.ST = ST;
        this.prob = prob;
    }

    // RULE BASED
    public ProcessPos(boolean prob, Texture<Couple<String, String>> TS) {
        this.TS = TS;
        this.prob = prob;
    }

    public Texture<Couple<Script,PosTags>> getSentence(){
        if(prob){
            return processProbabilistic().at(0);
        }
        else {
            return processRuleBased(TS).at(0);
        }
    }


    public Texture<Texture<Couple<Script, PosTags>>> getCouples()
    {
        if(prob){
            return processProbabilistic();
        }
        else {
            return processRuleBased(TS);
        }
    }


    private Texture<Texture<Couple<Script, PosTags>>> processRuleBased(Texture<Couple<String,String>> T) {
        Texture<Texture<Couple<Script, PosTags>>> res = new Texture<>();
        Texture<Couple<Script, PosTags>> ZE = new Texture<>();
        for(Couple<String,String> CS :T)
        {
            if(CS.getKey().equals("$"))
            {
                res = res.add(ZE);
                ZE = new Texture<>();
            }
            else{
                ZE = ZE.add(new Couple<>(Script.of(CS.getKey()),getPosTag(Script.of(CS.getValue()))));
            }
        }
        res = res.add(ZE);
        return res;
    }

    private Texture<Texture<Couple<Script, PosTags>>> processProbabilistic() {
        Texture<Texture<Couple<Script, PosTags>>> res = new Texture<>();
        Texture<Couple<Script, PosTags>> ZE = new Texture<>();
        Script S = ST.at(0);
        List<Script> LS = makeNgrams(String.valueOf(S));
        for(int i = 0; i+1<LS.size();i+=2)
        {

            Script tag = LS.get(i+1).replace("'", "")
                    .replace("\\[", "")
                    .replace(" ","")
                    .replace("\\]","");
            Script token = LS.get(i).replace("'", "")
                    .replace("\\[", "")
                    .replace("\\]","")
                    .replace("\\\\","");

            if(token.equals(Script.of("$"))){
                res = res.add(ZE);
                ZE = new Texture<>();
            }
            else{
                if(!token.equals(new Script("")))
                {
                    ZE = ZE.add(new Couple<>(token,getPosTag(tag)));
                }
            }
        }
        res = res.add(ZE);
        return res;
    }

    private List<Script> makeNgrams(String S) {
        List<Script> splitByComma = new ArrayList<>();
        int startIndex = 0;

        for (int i = 0; i < S.length(); i++) {
            char currentChar = S.charAt(i);

            if (currentChar == ',') {
                String part = S.substring(startIndex, i);
                splitByComma.add(Script.of(part));
                startIndex = i + 1;
            }
        }

        if (startIndex < S.length()) {
            String part = S.substring(startIndex);
            splitByComma.add(new Script(part));
        }
        List<Script> res = new ArrayList<>();
        boolean comma = false;
        for(Script SA : splitByComma)
        {

            if((SA.equals(new Script(""))|| SA.equals(new Script('\\'))) && !comma)
            {
                res.add(new Script(","));
                comma = true;
            }
            else {
                res.add(SA.replace(",",""));
                comma = false;
            }

        }
        return res;
    }

    private PosTags getPosTag(Script C) {
        String S = C.toString();
        try{
            switch (S) {
                case "NN", "NNP", "NNPS", "NNS" -> { return PosTags.Noun; }
                case "CC" -> { return PosTags.Conjunction; }
                case "CD" -> { return PosTags.Numeral; }
                case "DT", "WDT" -> { return PosTags.Determiner; }
                case "JJ", "JJR", "JJS" -> { return PosTags.Adjective; }
                case "MD" -> { return PosTags.Auxilary; }
                case "PRP", "PRP$", "POS", "PDT", "WP", "WP$" -> { return PosTags.Pronoun; }
                case "RB", "RBR", "RBS", "RP", "WRB" -> { return PosTags.Adverb; }
                case "TO" -> { return PosTags.to; }
                case "UH" -> { return PosTags.Interjection; }
                case "VB", "VBD", "VBG", "VBN", "VBP", "VBZ" -> { return PosTags.Verb; }
                case "EX" -> { return PosTags.Existential; }
                case "IN" -> { return PosTags.Preposition; }
                case "LS" -> { return PosTags.list; }
                default -> { return PosTags.Default; }
            }
        }
        catch (Exception e)
        {
            return PosTags.Default;
        }
    }
}
