package No_POS_Tagging.Preprocessing;

import lingolava.Nexus;
import lingologs.Script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class is responsible for loading the Data from the .json file.
 */

public class LoadTrainingData {

    public LoadTrainingData()
    {
    }

    public HashMap<String,List<HashMap<Script,Script>>> getData()
    {
        return prepareData(getString());
    }

    private Script getString() {
        String result = "";

        try (BufferedReader reader = new BufferedReader(
                new FileReader("json/data.json"))) {
            String line;
            StringBuilder jsonContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            result = jsonContent.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Script.of(result);
    }

    private HashMap<String, List<HashMap<Script,Script>>> prepareData(Script SC)  {
        HashMap<String, List<HashMap<Script,Script>>> HM = new HashMap<>();
        try {
            Nexus.DataNote DN = Nexus.DataNote.byJSON(SC);
            Map<Nexus.DataNote, Nexus.DataNote> res = DN.asMap();
            for (Nexus.DataNote D : res.keySet())
            {
                if(D!= null&&res.get(D).asList()!=null)
                {
                    List<HashMap<Script,Script>> L = prepareValues(res.get(D).asList());
                    HM.put(D.asString(),L);
                }
            }
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return HM;
    }

    private List<HashMap<Script,Script>> prepareValues(List<Nexus.DataNote> LD)
    {
        List<HashMap<Script,Script>> result = new ArrayList<>();

        for(Nexus.DataNote D :LD)
        {
            String jsonString = D.asJSON().replace("\\t", "\\\\t");
            try {
                HashMap<Script,Script> HZ = new HashMap<>();
                Script lineNumber = Script.of(extractValue(jsonString, "line_number"));
                Script speaker = Script.of(extractValue(jsonString, "speaker"));
                Script textEntry = Script.of(extractValue(jsonString, "text_entry"));
                HZ.put(Script.of("line_number"),lineNumber);
                HZ.put(Script.of("speaker"),speaker);
                HZ.put(Script.of("text_entry"),textEntry);
                result.add(HZ);
            } catch (Exception e) {
                System.out.println(jsonString);
                throw new RuntimeException(e + "prepareValues");
            }
        }
        return result;
    }

    private String extractValue(String jsonString, String fieldName) {
        int startIndex = jsonString.indexOf(fieldName + "\":\"") + fieldName.length() + 3;
        int endIndex = jsonString.indexOf("\"", startIndex);
        return jsonString.substring(startIndex, endIndex);
    }
}
