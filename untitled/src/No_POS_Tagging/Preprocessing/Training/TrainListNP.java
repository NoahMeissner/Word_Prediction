package No_POS_Tagging.Preprocessing.Training;

import Part_Of_Speech_Tagging.PreProcessing;
import lingologs.Script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/*
 * The `TrainList` class manages script training tasks and the creation of language models.
        * It operates on a collection of scripts, performing unigram and bigram training.
        * The class includes methods to train and obtain unigram and bigram model weights.
 * Usage:
        * The TrainList class is used to train unigram and bigram models on a collection of scripts.
        * Users can create an instance of this class, provide script pairs, and perform training
        * to obtain unigram and bigram model weights.
 */
public class TrainListNP {
    private final List<List<Script>> LS;
    private final boolean preprocessing;


    static class TrainCallable implements Callable<HashMap<Script, HashMap<Script,Integer>>>
    {
        private final List<List<Script>> LS;
        private final boolean Bigram;

        public TrainCallable(boolean Bigram, List<List<Script>> LS){
            this.LS = LS;
            this.Bigram = Bigram;
        }

        @Override
        public HashMap<Script, HashMap<Script, Integer>> call() {
            if(Bigram){
                TrainBigramWeights T = new TrainBigramWeights(LS);
                return T.getWeights();
            }
            else{
                TrainUnigramWeights T = new TrainUnigramWeights(LS);
                return T.getWeights();
            }
        }
    }


    public TrainListNP(HashMap<String, List<HashMap<Script,Script>>> HS, String test, boolean preprocessing)
    {
        this.LS = reduceMap(HS, test);
        this.preprocessing = preprocessing;
    }

    private List<List<Script>> reduceMap(HashMap<String, List<HashMap<Script,Script>>> HS,String test)
    {
        List<List<Script>> result = new ArrayList<>();
        for(String D : HS.keySet())
        {
            if(!D.equals(test))
            {
                List<Script> Z = new ArrayList<>();
                for(HashMap<Script,Script> H:HS.get(D))
                {
                    Z.add(H.get(Script.of("text_entry")));
                }
                PreProcessing P = new PreProcessing(Z,preprocessing);
                result.addAll(P.getListText());
            }
        }
        return result;
    }

    private Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>>
    multiParsing(List<List<Script>> LS) throws ExecutionException, InterruptedException
    {
        TrainCallable TCU = new TrainCallable(false, LS), TCB = new TrainCallable(true, LS);
        ExecutorService ExSe = Executors.newCachedThreadPool();
        Future<HashMap<Script, HashMap<Script, Integer>>> FU = ExSe.submit(TCU), FB = ExSe.submit(TCB);
        HashMap<Script, HashMap<Script, Integer>> HU = FU.get(), HB = FB.get();
        ExSe.close();
        return Map.entry(HU,HB);
    }

    public Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> getWeights()
    {
        try
        {
            return multiParsing(LS);
        }
        catch (Exception E)
        { System.out.println(E.getMessage()); }
        return null;
    }




}
