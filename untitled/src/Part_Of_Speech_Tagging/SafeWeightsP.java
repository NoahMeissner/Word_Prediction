package Part_Of_Speech_Tagging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lingolava.Tuple.Couple;
import lingologs.Script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Key Functionalities:
 * - Loading and Safely Storing POS Tagging Weights: This class handles the reading and writing of POS tagging
 *   weights, including both text-based weights and POS-specific weights.
 * - Data Conversion: It includes methods for converting between different data structures, such as converting
 *   between script-based keys and string-based keys, facilitating data storage and retrieval.
 */
public class SafeWeightsP {
    private final Path TW = Paths.get("untitled/src/JSON/safe_pos_Text.json");
    private final Path PW = Paths.get("untitled/src/JSON/safe_pos_POS.json");


    public SafeWeightsP(Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> C) {
        safePosTags(PW, C.getValue());
        safeTextWeights(TW, convertScriptToString(C.getKey()));
    }

    public SafeWeightsP() {

    }

    public Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> getWeights() {
        return new Couple<>(getTextWeights(TW), getPosTags(PW));
    }

    private HashMap<Script, HashMap<PosTags, Integer>> getPosTags(Path P) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = Files.readString(P);
            TypeReference<HashMap<String, HashMap<PosTags, Integer>>> typeReference =
                    new TypeReference<>() {
                    };

            return convertPosToScript(objectMapper.readValue(json, typeReference));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private HashMap<Script, HashMap<PosTags, Integer>> convertPosToScript(
            HashMap<String, HashMap<PosTags, Integer>> HM
    )
    {
        HashMap<Script, HashMap<PosTags, Integer>> convertedMap = new HashMap<>();

        for (String S : HM.keySet()) {
            convertedMap.put(Script.of(S), HM.get(S));
        }
        return convertedMap;
    }

    private HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>> getTextWeights(Path P) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = Files.readString(P);
            TypeReference<HashMap<String, HashMap<PosTags, HashMap<String, Integer>>>> typeReference =
                    new TypeReference<>() {
            };

            return convertStringToScript(objectMapper.readValue(json, typeReference));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void safePosTags(Path P, HashMap<Script, HashMap<PosTags, Integer>> HM) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(convertPosTagsToString(HM));
            Files.write(P, json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    private void safeTextWeights(Path P, HashMap<String, HashMap<PosTags, HashMap<String, Integer>>> HM) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(HM);
            Files.write(P, json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private HashMap<String, HashMap<PosTags, HashMap<String, Integer>>> convertScriptToString(
            HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>> OM) {
        HashMap<String, HashMap<PosTags, HashMap<String, Integer>>> TM = new HashMap<>();

        for (Map.Entry<Script, HashMap<PosTags, HashMap<Script, Integer>>> outerEntry : OM.entrySet()) {
            String outerKey = outerEntry.getKey().toString();
            HashMap<PosTags, HashMap<String, Integer>> innerMap = new HashMap<>();

            for (Map.Entry<PosTags, HashMap<Script, Integer>> middleEntry : outerEntry.getValue().entrySet()) {
                PosTags middleKey = middleEntry.getKey();
                HashMap<String, Integer> innermostMap = new HashMap<>();

                for (Map.Entry<Script, Integer> innerEntry : middleEntry.getValue().entrySet()) {
                    String innermostKey = innerEntry.getKey().toString();
                    Integer value = innerEntry.getValue();
                    innermostMap.put(innermostKey, value);
                }

                innerMap.put(middleKey, innermostMap);
            }

            TM.put(outerKey, innerMap);
        }

        return TM;
    }

    private HashMap<String, HashMap<PosTags, Integer>> convertPosTagsToString(
            HashMap<Script, HashMap<PosTags, Integer>> HM
    )
    {
        HashMap<String, HashMap<PosTags, Integer>> convertedMap = new HashMap<>();

        for (Script S : HM.keySet()) {
            convertedMap.put(S.toString(), HM.get(S));
        }
        return convertedMap;
    }

    private HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>> convertStringToScript(
            HashMap<String, HashMap<PosTags, HashMap<String, Integer>>> OM) {
        HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>> TM = new HashMap<>();

        for (Map.Entry<String, HashMap<PosTags, HashMap<String, Integer>>> outerEntry : OM.entrySet()) {
            Script outerKey = Script.of(outerEntry.getKey());
            HashMap<PosTags, HashMap<Script, Integer>> innerMap = new HashMap<>();

            for (Map.Entry<PosTags, HashMap<String, Integer>> middleEntry : outerEntry.getValue().entrySet()) {
                PosTags middleKey = middleEntry.getKey();
                HashMap<Script, Integer> innermostMap = new HashMap<>();

                for (Map.Entry<String, Integer> innerEntry : middleEntry.getValue().entrySet()) {
                    Script innermostKey = Script.of(innerEntry.getKey());
                    Integer value = innerEntry.getValue();
                    innermostMap.put(innermostKey, value);
                }

                innerMap.put(middleKey, innermostMap);
            }

            TM.put(outerKey, innerMap);
        }

        return TM;
    }
}
