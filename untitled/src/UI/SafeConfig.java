package UI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SafeConfig {

    private final Path P = Paths.get("untitled/src/JSON/config.json");

    public SafeConfig(Map<Tags, Integer> M)
    {

        safeConfig(M);
    }

    public SafeConfig()
    {

    }

    public Map<Tags,Integer> getConfig()
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = Files.readString(P);
            TypeReference<Map<String,Integer>> typeReference =
                    new TypeReference<>() {
                    };

            return convertToTags(objectMapper.readValue(json, typeReference));
        } catch (IOException e) {
            return null;
        }
    }


    private void safeConfig(Map<Tags,Integer> M)
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(convertToString(M));
            Files.write(P, json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    private Map<String,Integer> convertToString(Map<Tags, Integer> M) {
        Map<String, Integer> res = new HashMap<>();
        for(Tags T: M.keySet())
        {
            res.put(T.name(),M.get(T));
        }
        return res;
    }

    private Map<Tags, Integer> convertToTags(Map<String, Integer> M)
    {
        Map<Tags, Integer> res = new HashMap<>();
        for(String T: M.keySet())
        {
            res.put(Tags.valueOf(T),M.get(T));
        }
        return res;
    }
}
