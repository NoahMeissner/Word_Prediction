package Part_Of_Speech_Tagging.Test;

import Part_Of_Speech_Tagging.PosTags;
import lingolava.Tuple;
import lingologs.Script;
import lingologs.Texture;

import java.util.HashMap;

/**
 * The TestCombination class is responsible for evaluating the performance of a part-of-speech (POS) tagging system
 * using a combination of unigram and bigram-based weights. It tests the weights on a provided test set and calculates
 * various evaluation metrics such as true positives, false positives, not found tags, and correctly tagged positions.
 */

public class TestCombination {

    private final boolean UOB;
    private final boolean learn;

    private final float accuracy = 0.8F;
    private HashMap<Script, HashMap<Script, Integer>> HNP;
    private Tuple.Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> CP;

    private Texture<Texture<Tuple.Couple<Script,PosTags>>> LS;

    public TestCombination(boolean UOB,
                           Tuple.Couple<HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> CP,
                           boolean learn,
                           Texture<Texture<Tuple.Couple<Script,PosTags>>> LS,
                           HashMap<Script, HashMap<Script, Integer>> HNP)
    {
        this.CP = CP;
        this.HNP = HNP;
        this.UOB = UOB;
        this.learn = learn;
        this.LS = LS;
    }

    public Tuple.Quaple<Integer,Integer,Integer,Integer>testSystem(){
        int positive = 0;
        int negative = 0;
        int notFoundPOS = 0;
        int notEnough =0;
        int UOBN = 0;
        HashMap<Script, HashMap<PosTags, HashMap<Script,Integer>>> tokensPOS = CP.getKey();
        HashMap<Script, HashMap<PosTags, Integer>> tagsPOS = CP.getValue();
        HashMap<Script, HashMap<Script, Integer>> NPOS = HNP;
        if(UOB)
        {
            UOBN = 1;
        }
        else{
            UOBN = 2;
        }

        for(Texture<Tuple.Couple<Script,PosTags>> SEN : LS)
        {
            for(int i = 0; i<SEN.toList().size();i++)
            {
                boolean POS = true;
                if(i+1<SEN.toList().size())
                {
                    Script token = getToken(SEN,i);
                    Script tag = getTag(SEN,i);
                    Script tokennext = SEN.at(UOBN).getKey();
                    HashMap<PosTags, Integer> MPOS = tagsPOS.get(tag);
                    if(MPOS != null)
                    {
                        int ZR = 0;
                        int maxP =0;
                        PosTags PE = PosTags.Default;
                        for(PosTags P : MPOS.keySet())
                        {
                            int n = MPOS.get(P);
                            ZR+= n;
                            if(n>maxP)
                            {
                                maxP = n;
                                PE = P;
                            }
                        }
                        if((float) maxP/ZR >= accuracy)
                        {
                            HashMap<PosTags, HashMap<Script,Integer>> Mtok = tokensPOS.get(token);
                            if(Mtok != null)
                            {
                                HashMap<Script,Integer> MPOK = Mtok.get(PE);
                                if(MPOK!= null)
                                {
                                    ZR = 0;
                                    maxP =0;
                                    Script res = new Script("");
                                    for(Script S:MPOK.keySet())
                                    {
                                       int n = MPOK.get(S);
                                       ZR+= n;
                                       if(n>maxP)
                                       {
                                           maxP = n;
                                           res = S;
                                       }
                                    }
                                    if((float)maxP/ZR >= accuracy)
                                    {
                                        if(res.equals(tokennext))
                                        {
                                            positive++;// TODO learn
                                            MPOK.put(tokennext,MPOK.get(tokennext)+1);
                                            Mtok.put(PE,MPOK);
                                            MPOS.put(PE,MPOS.get(PE)+1);
                                            tagsPOS.put(tag,MPOS);
                                            tokensPOS.put(token,Mtok);
                                        }
                                        else{
                                            negative++;
                                        }
                                    }
                                    else{
                                        POS = false;
                                    }
                                }
                                else {
                                    POS = false;
                                }
                            }
                            else{
                                POS = false;
                            }
                        }
                        else{
                            POS = false;
                        }
                    }
                    if(!POS)
                    {
                        notFoundPOS++;

                        HashMap<Script,Integer> HS = NPOS.get(token);
                        if(HS!= null)
                        {
                            int ZR = 0;
                            int maxI =0;
                            Script res = new Script("");
                            for(Script S:HS.keySet())
                            {
                                int n = HS.get(S);
                                ZR+= n;
                                if(n>maxI)
                                {
                                    maxI = n;
                                    res = S;
                                }
                            }
                            if((float)maxI/ZR >= accuracy){
                                if(res.equals(tokennext))
                                {
                                    positive++;
                                    HS.put(res,HS.get(res)+1);
                                    NPOS.put(token,HS);
                                }
                                else {
                                    negative++;
                                }
                            }
                            else{
                                notEnough++;
                            }

                        }
                        else {
                            notEnough++;
                        }

                    }
                }
            }
        }
        return new Tuple.Quaple<>(positive,negative,notFoundPOS,notEnough);
    }

    private Script getTag(Texture<Tuple.Couple<Script, PosTags>> sen, int i) {
        if(UOB)
        {
            return Script.of(sen.at(i).getValue().name());
        }
        else{
            return Script.of(sen.at(i).getValue()+" "+ sen.at(i+1).getValue());
        }
    }

    private Script getToken(Texture<Tuple.Couple<Script, PosTags>> sen, int i) {
        if(UOB)
        {
            return sen.at(i).getKey();
        }
        else{
            return Script.of(sen.at(i).getKey()+" "+ sen.at(i+1).getKey());
        }
    }
}
