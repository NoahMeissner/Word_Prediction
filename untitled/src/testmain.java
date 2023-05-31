import Training.DataType;
import UI.Chat;
import UI.Prozessor;
import lingolava.Nexus;
import lingologs.Script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.newBufferedReader;

public class testmain {

    public static void main(String[] args) {
        long X1 = System.nanoTime();
        Nexus.JSONProcessor JP = new Nexus.JSONProcessor();
        List<String> D = null;
        String A;
        try
        {
            String P = ("/Users/noahmeissner/Documents/Data/projectreischer/untitled/src/shakespeare (2).json");
            D = readFileToList(P);
        }catch (Exception e)
        {

        }
        A = "["+String.join(",",D)+"]";
        Nexus.DataNote DN = JP.parse(A);
        System.out.println(DN.asList().size());
        long X2 = System.nanoTime();
        System.out.println((double)(X2-X1)/1000000000);

    }
    private static List<String> readFileToList(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
