package No_POS_Tagging.Preprocessing.Training;
import No_POS_Tagging.Preprocessing.PreProcessing;
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
public class TrainList {
    private List<List<Script>> LS;
    private HashMap<Script, HashMap<Script, Integer>> MU;
    private HashMap<Script, HashMap<Script, Integer>> MB;
    private HashMap<String, List<HashMap<Script,Script>>> HS;

    private String S;

    class TrainCallable implements Callable<HashMap<Script, HashMap<Script,Integer>>>
    {
        private final List<List<Script>> LS;
        private final boolean Bigram;

        public TrainCallable(boolean Bigram, List<List<Script>> LS){
            this.LS = LS;
            this.Bigram = Bigram;
        }

        @Override
        public HashMap<Script, HashMap<Script, Integer>> call() throws Exception {
            if(Bigram){
                TrainBigramWeightsList T = new TrainBigramWeightsList(LS);
                return T.getWeights();
            }
            else{
                TrainUnigramWeightsList T = new TrainUnigramWeightsList(LS);
                return T.getWeights();
            }
        }
    }


    public TrainList(HashMap<String, List<HashMap<Script,Script>>> HS,String test)
    {
        this.LS = reduceMap(HS, test);
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
                PreProcessing P = new PreProcessing(Z);
                result.addAll(P.getListText());
            }
        }
        return result;
    }

    public Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>>
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
            long X1 = System.nanoTime();
            Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>>
                    R = multiParsing(LS);
            long X2 = System.nanoTime();
            System.out.println("\r\ntime: Multilearning"+
                    ": "+(double)(X2-X1)/1_000_000_000);

            return R;
        }
        catch (Exception E)
        { System.out.println(E.getMessage()); }
        return null;
    }




}
