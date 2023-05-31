package UI;

import Predictor.Loader;
import Training.DataType;
import Training.PreProcessing;
import lingolava.Nexus;
import lingologs.Script;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.json.*;

public class Prozessor {

    /*
    The program receives either a .txt file or a pre-trained .json file as training data.
    The Processor determines if the file is a text file and initiates the training process.
    It creates a JSON file and passes the data to the Predictor.Loader Script.
     */

    private final DataType DT;
    private final Script V = Script.of("word_prediction");
    private final Script K = Script.of("key");

    private PreProcessing P;

    private Loader L;
    private Nexus.DataNote DAN;


    public Prozessor(Script path, DataType dataType)
    {
        this.DT = dataType;
        importData(path);
    }

    private void importData(Script S)
    {
        Script T = null;
        try
        {
            T = Script.of(Files.readString(Path.of(S.toString())));

            if(DT == DataType.JSON)
            {
                if(checkJSON(T))
                {
                    L = new Loader(DAN);
                }
            }
            else
            {
                P = new PreProcessing(T);
            }
        } catch (IOException e)
        {
            // TODO nochmal einfuegen
            System.out.println("ashjdfg;");
        }

    }

    private boolean checkJSON(Script S)
    {
        Nexus.JSONProcessor JP = new Nexus.JSONProcessor();
        Nexus.DataNote DN;
        try
        {
            DN = JP.parse(S);
            DAN = DN;
        } catch (Exception e)
        {
            System.out.println("Daten konnten nicht geparst werden");
            return false;
        }
       return jsonVerification(K, DN,S);
    }

    private boolean jsonVerification(Script key, Nexus.DataNote DN, Script S)
    {
        if (DN == null)
        {
            return false;
        }
        JSONObject JO = new JSONObject(S.toString());
        Script result = Script.of(JO.getString(key.toString()));
        if(result.equals(V))
        {
            System.out.println(true);
            return true;
        }
        return false;
    }

    public Loader L ()
    {
        return L;
    }

    public PreProcessing P()
    {
        return P;
    }
}
