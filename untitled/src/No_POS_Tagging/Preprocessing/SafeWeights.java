package No_POS_Tagging.Preprocessing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lingologs.Script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SafeWeights {

    private final Path UI = Paths.get("untitled/src/JSON/safe_no_pos_UI.json");
    private final Path BI = Paths.get("untitled/src/JSON/safe_no_pos_BI.json");

    private final Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> M;

    public SafeWeights(Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> M)
    {
        this.M = M;
        Map.Entry<HashMap<String, HashMap<String, Integer>>, HashMap<String, HashMap<String, Integer>>> ZS = convertScriptToString(M);
        SafeInJSON(UI,ZS.getKey());
        SafeInJSON(BI,ZS.getValue());
    }

    public SafeWeights()
    {
        this.M = convertStringToScript(Map.entry(
                Objects.requireNonNull(getFromJ(UI)),
                Objects.requireNonNull(getFromJ(BI))));
    }

    public Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> getM() {
        return M;
    }

    private HashMap<String, HashMap<String, Integer>> getFromJ(Path P)
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<HashMap<String, HashMap<String, Integer>>> typeRef =
                    new TypeReference<>() {
                    };

            return objectMapper.readValue(P.toFile(), typeRef);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void SafeInJSON(Path P, HashMap<String,HashMap<String,Integer>> MT){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String JS = objectMapper.writeValueAsString(MT);
            Files.write(P, JS.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete()
    {
        deleteOldJ(UI);
        deleteOldJ(BI);
    }

    private void deleteOldJ(Path P)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String JS = objectMapper.writeValueAsString(new HashMap<>());
            Files.write(P, JS.getBytes());
            System.out.println("Map erfolgreich geloescht " + P);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map.Entry<HashMap<String, HashMap<String, Integer>>, HashMap<String, HashMap<String, Integer>>> convertScriptToString(
            Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> inputMap) {
        HashMap<String, HashMap<String, Integer>> outerMap = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> innerMap = new HashMap<>();

        for (Map.Entry<Script, HashMap<Script, Integer>> outerEntry : inputMap.getKey().entrySet()) {
            String outerKey = outerEntry.getKey().toString();
            HashMap<String, Integer> innerHashMap = new HashMap<>();

            for (Map.Entry<Script, Integer> innerEntry : outerEntry.getValue().entrySet()) {
                String innerKey = innerEntry.getKey().toString();
                int innerValue = innerEntry.getValue();
                innerHashMap.put(innerKey, innerValue);
            }

            innerMap.put(outerKey, innerHashMap);
        }

        for (Map.Entry<Script, HashMap<Script, Integer>> innerEntry : inputMap.getValue().entrySet()) {
            String innerKey = innerEntry.getKey().toString();
            HashMap<String, Integer> innerHashMap = new HashMap<>();

            for (Map.Entry<Script, Integer> entry : innerEntry.getValue().entrySet()) {
                String key = entry.getKey().toString();
                int value = entry.getValue();
                innerHashMap.put(key, value);
            }

            outerMap.put(innerKey, innerHashMap);
        }

        return Map.entry(outerMap, innerMap);
    }

    private Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> convertStringToScript(
            Map.Entry<HashMap<String, HashMap<String, Integer>>, HashMap<String, HashMap<String, Integer>>> IM)
    {
        HashMap<Script, HashMap<Script, Integer>> outerMap = new HashMap<>();
        HashMap<Script, HashMap<Script, Integer>> innerMap = new HashMap<>();

        for (Map.Entry<String, HashMap<String, Integer>> outerEntry : IM.getKey().entrySet()) {
            Script outerKey = Script.of(outerEntry.getKey());
            HashMap<Script, Integer> innerHashMap = new HashMap<>();

            for (Map.Entry<String, Integer> innerEntry : outerEntry.getValue().entrySet()) {
                Script innerKey = Script.of(innerEntry.getKey());
                int innerValue = innerEntry.getValue();
                innerHashMap.put(innerKey, innerValue);
            }

            innerMap.put(outerKey, innerHashMap);
        }

        for (Map.Entry<String, HashMap<String, Integer>> innerEntry : IM.getValue().entrySet()) {
            Script innerKey = Script.of(innerEntry.getKey());
            HashMap<Script, Integer> innerHashMap = new HashMap<>();

            for (Map.Entry<String, Integer> entry : innerEntry.getValue().entrySet()) {
                Script key = Script.of(entry.getKey());
                int value = entry.getValue();
                innerHashMap.put(key, value);
            }
            outerMap.put(innerKey, innerHashMap);
        }
        return Map.entry(outerMap, innerMap);
    }
}
