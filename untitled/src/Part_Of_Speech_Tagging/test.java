package Part_Of_Speech_Tagging;

import lingolava.Tuple;
import lingologs.Script;

import java.util.HashMap;

public class test {

    public static String text = "Marie was born in Paris.";

    public static void main(String[] args) {
        /*
        LoadTrainingData l = new LoadTrainingData();
        HashMap<String, List<HashMap<Script,Script>>> S = l.getData();
        System.out.println("Data loaded");


        long startTime_train = System.currentTimeMillis();

        TrainList T = new TrainList(S,"Henry V",false,false);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime_train;
        System.out.println("Skriptdauer: " + duration + " Millisekunden");

        long startTime_test = System.currentTimeMillis();

        SafeWeights D = new SafeWeights(T.trainWeights());

      //  TestWeightsList TS = new TestWeightsList(false,T.trainWeights(),true,false);
        //System.out.println(TS.testWeights(S,"Henry V"));



        long endTime_test = System.currentTimeMillis();
        long duration_test = endTime_test - startTime_test;
        System.out.println("Skriptdauer: " + duration_test + " Millisekunden");

         */
    }

    private static Tuple.Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>,
                HashMap<Script, HashMap<PosTags, Integer>>> createTestDataset() {
        // Erstellen eines Beispieldatensatzes
        HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>> firstMap = new HashMap<>();
        HashMap<PosTags, HashMap<Script, Integer>> innerMap1 = new HashMap<>();
        HashMap<Script, Integer> innerMap2 = new HashMap<>();
        innerMap2.put(new Script("Script1"), 10);
        innerMap2.put(new Script("Script2"), 20);
        innerMap1.put(PosTags.Auxilary, innerMap2);
        firstMap.put(new Script("ScriptA"), innerMap1);

        HashMap<Script, HashMap<PosTags, Integer>> secondMap = new HashMap<>();
        HashMap<PosTags, Integer> innerMap3 = new HashMap<>();
        innerMap3.put(PosTags.Adjective, 30);
        secondMap.put(new Script("fs"), innerMap3);

        // Erstellen des Testdatensatzes und Rückgabe
        return new Tuple.Couple<>(firstMap, secondMap);
    }




}
