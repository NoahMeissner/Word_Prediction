import com.fasterxml.jackson.databind.ObjectMapper;
import lingologs.Script;

public class Main {

    /*
    Start of the Program
     */

    private static final Script BP = new Script("""
                /Users/noahmeissner/Documents/Data/projectreischer/untitled/src/test.json
            """);


    public static void main(String[] args) {
        /*
        SetUp S = new SetUp();
        Loader L = null;
        while(L == null)
        {
            L = S.start();
        }

         */
        String jsonString = "{\"line_number\":\"5.4.45\",\"speaker\":\"PRINCE HENRY\",\"text_entry\":\"Cheerly, my lord	how fares your grace?\"}";

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Extrahiere die Werte aus dem JSON-String manuell
            String lineNumber = extractValue(jsonString, "line_number");
            String speaker = extractValue(jsonString, "speaker");
            String textEntry = extractValue(jsonString, "text_entry");

            // Verarbeite die extrahierten Werte wie gewünscht
            System.out.println("line_number: " + lineNumber);
            System.out.println("speaker: " + speaker);
            System.out.println("text_entry: " + textEntry);
        } catch (Exception e) {
            throw new RuntimeException(e + "prepareValues");
        }


    }

    private static String extractValue(String jsonString, String fieldName) {
        int startIndex = jsonString.indexOf(fieldName + "\":\"") + fieldName.length() + 3;
        int endIndex = jsonString.indexOf("\"", startIndex);
        return jsonString.substring(startIndex, endIndex);
    }
}






