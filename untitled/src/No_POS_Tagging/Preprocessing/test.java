package No_POS_Tagging.Preprocessing;

import No_POS_Tagging.Preprocessing.Test_weights.TestWeightsList;
import No_POS_Tagging.Preprocessing.Training.TrainList;
import lingologs.Script;

import java.util.HashMap;
import java.util.List;

public class test {

    public test() {

    }



    public static void main(String[] args) {
        //PosTagging P = new PosTagging(Script.of("Hey i am Noah"));
        // Text erhalten

        LoadTrainingData l = new LoadTrainingData();
        HashMap<String, List<HashMap<Script,Script>>> S = l.getData();
        System.out.println("Data loaded");
        TrainList T = new TrainList(S,"Henry V");
        System.out.println("Weights initialisiert");
        System.out.println(T.getWeights());
        TestWeightsList TE = new TestWeightsList(S,"Henry V",T.getWeights());


        System.out.println("Train Weights + Test weights initialisiert");
        // TODO Unigramme
        System.out.println(TE.calculateProbability(true,1,true));
        // TODO Bigramme
      // System.out.println(TE.calculateProbability(false,3,false));
    }

    /*
    public static void saveJsonToFile(String path, String json) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(json);
            fileWriter.close();
            System.out.println("JSON erfolgreich am Pfad '" + path + "' gespeichert.");
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der JSON-Datei: " + e.getMessage());
        }
    }
     */

    /*

    private static void makeJSON(TrainList T)
    {
        Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> res = T.getWeights();

        Map<String,String> U = checkmost(res.getKey());
        Map<String,String> B = checkmost(res.getValue());

        String JU = mapToJson(U);
        String JB = mapToJson(B);
        saveJsonToFile("/Users/noahmeissner/Documents/Data/unigram.json",JU);
        saveJsonToFile("/Users/noahmeissner/Documents/Data/bigram.json",JB);

    }

    public static String mapToJson(Map<String, String> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static Map<String, String> checkmost
            (HashMap<Script, HashMap<Script, Integer>> L )
    {
        Map<String,String> result = new HashMap<>();
        for (Script S:L.keySet())
        {
            String zwischen = S.toString();
            int Z = 0;
            for(Script A: L.get(S).keySet())
            {
                Z += L.get(S).get(A);
            }
            result.put(zwischen,String.valueOf(Z));
        }
        return result;
    }

     */
}