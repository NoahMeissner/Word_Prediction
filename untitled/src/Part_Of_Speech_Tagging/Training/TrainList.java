package Part_Of_Speech_Tagging.Training;

import Part_Of_Speech_Tagging.PPos;
import Part_Of_Speech_Tagging.PosTags;
import Part_Of_Speech_Tagging.ProcessPos;
import Part_Of_Speech_Tagging.RPos;
import lingolava.Tuple.Couple;
import lingologs.Script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class TrainList {

    private final List<List<Couple<Script, PosTags>>> LS;
    private final boolean UOB;


    public Couple<HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>>,HashMap<Class<?>, HashMap<PosTags, Integer>>>
    trainWeights()
    {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Couple<HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>>,HashMap<Class<?>, HashMap<PosTags, Integer>>>
                res = new Couple<>();
        List<Callable<Object>> tasks = new ArrayList<>();
        Callable<Object> getWeightsTask = this::getWeights;
        Callable<Object> getPOSWeightsTask = () -> {
            if(UOB)
            {
                return getPOSWeights(PosTags.class);
            }
            else {
                return getPOSWeights(Script.class);
            }
        };
        tasks.add(getWeightsTask);
        tasks.add(getPOSWeightsTask);
        try {
            List<Future<Object>> futures = executorService.invokeAll(tasks);
            HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>> weightsResult =
                    (HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>) futures.get(0).get();

            HashMap<Class<?>, HashMap<PosTags, Integer>> posWeightsResult =
                    (HashMap<Class<?>, HashMap<PosTags, Integer>>) futures.get(1).get();

            res = new Couple<>(weightsResult,posWeightsResult);
            executorService.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return res;
    }


    public TrainList(HashMap<String, List<HashMap<Script,Script>>> HS, String testWork,boolean ROP,boolean UOB)// testWork Name of work which will be tested set
    {
        this.UOB = UOB;
        this.LS = prepareMap(HS, testWork,ROP);

    }

    private HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>> getWeights()
    {
        if(UOB)
        {
            TrainUnigramWeightsList T = new TrainUnigramWeightsList(LS);
            return T.getWeights();
        }
        else{
            TrainBigramWeightsList T = new TrainBigramWeightsList(LS);
            return T.getWeights();

        }
    }

    private  <T> HashMap<T, HashMap<PosTags, Integer>> getPOSWeights (Class<T> k)
    {
        if(UOB)
        {
            TrainUnigramWeightsList T = new TrainUnigramWeightsList(LS);
            return (HashMap<T, HashMap<PosTags, Integer>>) T.getTags();
        }
        else{
            TrainBigramWeightsList T = new TrainBigramWeightsList(LS);
            return (HashMap<T, HashMap<PosTags, Integer>>) T.getTags();
        }
    }



    private List<List<Couple<Script, PosTags>>>
    prepareMap(HashMap<String, List<HashMap<Script, Script>>> HS, String testWork,boolean ROP)
    {
        List<List<Couple<Script, PosTags>>> result = new ArrayList<>();
        for(String D : HS.keySet())
        {
            if(!D.equals(testWork))
            {
                List<Couple<Script,PosTags>> LZ;
                StringBuilder SB = new StringBuilder();
                for(HashMap<Script,Script> H:HS.get(D))
                {
                    SB.append(H.get(Script.of("text_entry")));
                }
                String text = SB.toString();
                if(ROP)
                {
                    PPos P = new PPos(Script.of(text));
                    ProcessPos PR = new ProcessPos(P.getPosTags().get(0));
                    LZ = PR.getCouples();
                }
                else{
                    RPos R = new RPos(Script.of(text));
                    ProcessPos PR = new ProcessPos(R.getPosTags());
                    LZ =  PR.getCouples();
                }
                result.add(LZ);
            }
        }
        return result;
    }
}
