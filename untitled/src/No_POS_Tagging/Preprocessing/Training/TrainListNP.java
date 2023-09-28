package No_POS_Tagging.Preprocessing.Training;

import Part_Of_Speech_Tagging.PreProcessing;
import lingolava.Tuple;
import lingologs.Script;
import lingologs.Texture;

import java.util.HashMap;
import java.util.List;
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
    private final Texture<Texture<Script>> LS;
    private final boolean preprocessing;

    public TrainListNP(HashMap<String, List<HashMap<Script,Script>>> HS, String test, boolean preprocessing)
    {
        this.LS = reduceMap(HS, test);
        this.preprocessing = preprocessing;
    }


    static class TrainCallable implements Callable<HashMap<Script, HashMap<Script,Integer>>>
    {
        private final Texture<Texture<Script>> LS;
        private final boolean Bigram;

        public TrainCallable(boolean Bigram, Texture<Texture<Script>> LS){
            this.LS = LS;
            this.Bigram = Bigram;
        }

        @Override
        public HashMap<Script, HashMap<Script, Integer>> call() {
            if(Bigram){
                TrainBigramWeights T = new TrainBigramWeights(LS);
                return T.getResults();
            }
            else{
                TrainUnigramWeights T = new TrainUnigramWeights(LS);
                return T.getResults();
            }
        }
    }

    private Texture<Texture<Script>> reduceMap(HashMap<String, List<HashMap<Script,Script>>> HS,String test)
    {
        Texture<Texture<Script>> result = new Texture<>();
        for(String D : HS.keySet())
        {
            if(!D.equals(test))
            {
                Texture<Script> Z = new Texture<>();
                for(HashMap<Script,Script> H:HS.get(D))
                {
                    Z = Z.add(H.get(Script.of("text_entry")));
                }
                PreProcessing P = new PreProcessing(Z,preprocessing,false);
                result = result.add(P.getListText());
            }
        }
        return result;
    }

    private Tuple.Couple<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>>
    multiThreading(Texture<Texture<Script>> LS) throws ExecutionException, InterruptedException
    {
        TrainCallable TCU = new TrainCallable(false, LS), TCB = new TrainCallable(true, LS);
        ExecutorService ExSe = Executors.newCachedThreadPool();
        Future<HashMap<Script, HashMap<Script, Integer>>> FU = ExSe.submit(TCU), FB = ExSe.submit(TCB);
        HashMap<Script, HashMap<Script, Integer>> HU = FU.get(), HB = FB.get();
        ExSe.close();
        return Tuple.Couple.of(HU,HB);
    }

    public Tuple.Couple<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> getWeights()
    {
        try
        {
            return multiThreading(LS);
        }
        catch (Exception E)
        { System.out.println(E.getMessage()); }
        return null;
    }




}
