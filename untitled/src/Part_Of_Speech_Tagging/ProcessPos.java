package Part_Of_Speech_Tagging;

import lingolava.Tuple.Couple;
import lingologs.Script;

import java.util.ArrayList;
import java.util.List;

public class ProcessPos {

    private Script S;
    private List<Couple<String,String>> LC;

    private final boolean prob;

    public ProcessPos(Script S)
    {
        this.S = S;
        this.prob = true;
    }

    public ProcessPos(List<Couple<String,String>> LS)
    {
        this.LC = LS;
        this.prob = false;
    }

    public List<Couple<Script,PosTags>> getCouples()
    {
        if(prob){
            return processProbabilistic();
        }
        else {
            return processRuleBased();
        }
    }

    private  List<Couple<Script,PosTags>> processRuleBased()
    {
        List<Couple<Script,PosTags>> res = new ArrayList<>();
        for(Couple<String,String> stringStringCouple : LC)
        {
            res.add(new Couple<>(
                    Script.of(stringStringCouple.getKey())
                    ,getPosTag(Script.of(stringStringCouple.getValue()))));
        }
        return res;
    }

    private List<Couple<Script,PosTags>> processProbabilistic() {
        List<Couple<Script,PosTags>> res = new ArrayList<>();
        Script D = S.replace("\\[","");
        D = D.replace("\\]","");
        List<String> LS = splitByComma(D.toString());
        for(int i = 0; i+1<LS.size();i+=2)
        {
            String token =",";
            if(LS.get(i).length()>=3)
            {
                token = LS.get(i).substring(2, LS.get(i).length()-1);
            }
            res.add(new Couple<>(Script.of(token), getPosTag(Script.of(LS.get(i+1)))));

        }
        return res;
    }

    private List<String> splitByComma (String  S)
    {
        List<String> tokens = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < S.length(); i++) {
            if (S.charAt(i) == ',') {
                tokens.add(S.substring(start, i));
                start = i + 1;
            }
        }
        tokens.add(S.substring(start));
        return tokens;

    }



    private PosTags getPosTag(Script characts) {
        String S = characts.toString();
        try{
            String D = S.substring(2,S.length()-1);
            //System.out.println(D);
            return switch (D) {
                case "NN", "NNP", "NNPS", "NNS" -> PosTags.Noun;
                case "CC" -> PosTags.Conjunction;
                case "CD" -> PosTags.Numeral;
                case "DT", "WDT" -> PosTags.Determiner;
                case "JJ", "JJR", "JJS" -> PosTags.Adjective;
                case "MD" -> PosTags.Auxilary;
                case "PRP", "PRP$", "POS", "PDT", "WP", "WP$" -> PosTags.Pronoun;
                case "RB", "RBR", "RBS", "RP", "WRB" -> PosTags.Adverb;
                case "TO" -> PosTags.to;
                case "UH" -> PosTags.Interjection;
                case "VB", "VBD", "VBG", "VBN", "VBP", "VBZ" -> PosTags.Verb;
                case "EX" -> PosTags.Existential;
                case "IN" -> PosTags.Preposition;
                case "LS" -> PosTags.list;
                default -> PosTags.Default;
            };
        }
        catch (Exception e)
        {
            return PosTags.Default; // Wenn wir durch kommas Splitten, wird das Komma entfernt und wir erhalten ein leeren String dieser wird dann als Default returned
        }
    }
}
