package Predictor;

import No_POS_Tagging.Preprocessing.SafeWeightsNP;
import Part_Of_Speech_Tagging.PosTags;
import Part_Of_Speech_Tagging.SafeWeightsP;
import UI.SafeConfig;
import UI.Tags;
import lingolava.Tuple.Couple;
import lingologs.Script;

import java.util.HashMap;
import java.util.Map;

/**
 * This class includes the following key functionalities:
 * 1. Configuration Handling: It allows the program to load and save project configurations using the SafeConfig class.
 * 2. Part-of-Speech Weights (POS): It enables the loading and safe retrieval of part-of-speech tagging weights
 *    for probabilistic language modeling.
 * 3. No Part-of-Speech Weights (NPOS): It enables the loading and safe retrieval of weights for scenarios where
 *    part-of-speech tagging is not used.
 */

public class Loader {

    private SafeWeightsP POS;
    private SafeWeightsNP NPOS;

    private SafeConfig SConfig;




    // SAFE CONFIG
    public Loader(Map<Tags, Integer> M)
    {
        SConfig = new SafeConfig(M);
    }

    public Loader()
    {
        SConfig = new SafeConfig();
    }

    // SAFE POS
    public Loader(Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C)
    {
        POS = new SafeWeightsP(C);
    }

    // SAFE NPOS
    public Loader(Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> M)
    {
        NPOS = new SafeWeightsNP(M);
    }

    public Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>,
                HashMap<Script, HashMap<PosTags, Integer>>> getDataP()
    {
        POS = new SafeWeightsP();
        return POS.getWeights();
    }

    public Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> getDataNP()
    {
        NPOS = new SafeWeightsNP();
        return NPOS.getM();
    }



    public Map<Tags, Integer> getConfig()
    {
        return SConfig.getConfig();
    }
}
