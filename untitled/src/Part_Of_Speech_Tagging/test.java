package Part_Of_Speech_Tagging;

import No_POS_Tagging.Preprocessing.LoadTrainingData;
import Part_Of_Speech_Tagging.Test.TestWeightsList;
import Part_Of_Speech_Tagging.Training.TrainList;
import lingolava.Tuple;
import lingologs.Script;

import java.util.HashMap;
import java.util.List;

public class test {

    public static String text = "Marie was born in Paris.";

    public static void main(String[] args) {

        LoadTrainingData l = new LoadTrainingData();
        HashMap<String, List<HashMap<Script,Script>>> S = l.getData();
        System.out.println("Data loaded");


        long startTime_train = System.currentTimeMillis();

        TrainList T = new TrainList(S,"Henry V",false,false);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime_train;
        System.out.println("Skriptdauer: " + duration + " Millisekunden");








        long startTime_test = System.currentTimeMillis();

        TestWeightsList TS = new TestWeightsList(false,T.trainWeights(),false,false);
        System.out.println(TS.testWeights(S,"Henry V"));



        long endTime_test = System.currentTimeMillis();
        long duration_test = endTime_test - startTime_test;
        System.out.println("Skriptdauer: " + duration_test + " Millisekunden");
    }

    private static Tuple.Couple<HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>>,HashMap<Class<?>, HashMap<PosTags, Integer>>>
testData()
    {
        // HashMap<PosTags, Integer>
        HashMap<PosTags,Integer> PI = new HashMap<>();
        PI.put(PosTags.Auxilary,12);
        PI.put(PosTags.Adverb,121);
        PI.put(PosTags.Adjective,11121);

        // HashMap<Script,Integer>
        HashMap<Script,Integer> SI = new HashMap<>();
        SI.put(Script.of("asdf"),123);
        SI.put(Script.of("asasdfdf"),1213243);
        SI.put(Script.of("asasdfdf"),122433);

        // HashMap<PosTags,HashMap<Script,Integer>>
        HashMap<PosTags,HashMap<Script,Integer>> tagsHashMapHashMap = new HashMap<>();
        tagsHashMapHashMap.put(PosTags.Adjective,SI);
        tagsHashMapHashMap.put(PosTags.Adverb,SI);


        HashMap<Class<?>, HashMap<PosTags, Integer>> tags = new HashMap<>();
        tags.put(Script.of("hallo").getClass(),PI);
        tags.put(Script.of("asdf").getClass(),PI);


        HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>> weoights = new HashMap<>();
        weoights.put(Script.of("asdf"),tagsHashMapHashMap);
        weoights.put(Script.of("asdf"),tagsHashMapHashMap);

        Tuple.Couple<HashMap<Script, HashMap<PosTags,HashMap<Script,Integer>>>,HashMap<Class<?>, HashMap<PosTags, Integer>>> res = new Tuple.Couple<>(weoights,tags);
        return res;
    }


}
