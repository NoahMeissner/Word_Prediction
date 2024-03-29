package Part_Of_Speech_Tagging.Training;

import Part_Of_Speech_Tagging.PPos;
import Part_Of_Speech_Tagging.PosTags;
import Part_Of_Speech_Tagging.ProcessPos;
import Part_Of_Speech_Tagging.RPos;
import lingolava.Tuple.Couple;
import lingologs.Script;
import lingologs.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class TrainListP {

    private final Texture<Texture<Couple<Script, PosTags>>> LS;
    private final boolean UOB;

    private final boolean preprocessing;


    public TrainListP(HashMap<String, List<HashMap<Script,Script>>> HS,
                      String testWork, boolean ROP, boolean UOB, boolean preprocessing)// testWork Name of work which will be tested set
    {
        this.UOB = UOB;
        this.LS = prepareMap(HS, testWork,ROP);
        this.preprocessing = preprocessing;
    }

    public Couple<HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>>,HashMap<Script, HashMap<PosTags, Integer>>>
    trainWeights()
    {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Couple<HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>>,HashMap<Script, HashMap<PosTags, Integer>>>
                res = new Couple<>();
        List<Callable<Object>> tasks = new ArrayList<>();
        Callable<Object> getWeightsTask = this::getWeights;
        Callable<Object> getPOSWeightsTask = this::getPOSWeights;
        tasks.add(getWeightsTask);
        tasks.add(getPOSWeightsTask);
        try {
            List<Future<Object>> futures = executorService.invokeAll(tasks);
            HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>> weightsResult =
                    (HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>) futures.get(0).get();

            HashMap<Script, HashMap<PosTags, Integer>> posWeightsResult =
                    (HashMap<Script, HashMap<PosTags, Integer>>) futures.get(1).get();

            res = new Couple<>(weightsResult,posWeightsResult);
            executorService.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return res;
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

    private  HashMap<Script, HashMap<PosTags, Integer>> getPOSWeights ()
    {
        if(UOB)
        {
            TrainUnigramWeightsList T = new TrainUnigramWeightsList(LS);
            return T.getTags();
        }
        else{
            TrainBigramWeightsList T = new TrainBigramWeightsList(LS);
            return T.getTags();
        }
    }



    private Texture<Texture<Couple<Script, PosTags>>>
    prepareMap(HashMap<String, List<HashMap<Script, Script>>> HS, String testWork,boolean ROP)
    {
        Texture<Texture<Couple<Script, PosTags>>> result = new Texture<>();
        long startTime = System.currentTimeMillis();
        for(String D : HS.keySet())
        {
            if(!D.equals(testWork))
            {
                Texture<Script> SB = new Texture<>();

                Texture<Texture<Couple<Script, PosTags>>> LZ;
                for(HashMap<Script,Script> H:HS.get(D))
                {
                    SB = SB.add(H.get(Script.of("text_entry")));
                }
                if(ROP)
                {
                    PPos P = new PPos(SB,preprocessing);
                    ProcessPos PR = new ProcessPos(P.getPosTags(),ROP);
                    LZ = PR.getCouples();
                }
                else{
                    RPos R = new RPos(SB,preprocessing);
                    ProcessPos PR = new ProcessPos(ROP,R.getPosTags());
                    LZ =  PR.getCouples();
                }
                result = result.add(LZ);
                LZ = new Texture<>();
            }
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Runtime Pos Tagging " + elapsedTime + " Milliseconds");
        return result;
    }
}
