package UI;

import Predictor.Loader;
import Training.DataType;
import lingologs.Script;

import java.util.ArrayList;
import java.util.List;

public class SetUp {

    private Prozessor P;
    private DataType D;
    public SetUp()
    {

    }

    public Loader start()
    {
        Chat C = new Chat(Script.of("Welcome auf unserem neuen Feld"));
        setP(C.getChat());
        if(D != null)
        {
            if(D.equals(DataType.JSON))
            {
                return setLoader();
            }
            if(D.equals(DataType.TXT))
            {
                System.out.println("Worked");
                setPipeline(); // TODO muss Loader zurueckgeben;
                return setLoader();
            }
            else{
                return null; //nicht
            }
        }
        else {
            return null;
        }
    }


    private Loader setLoader()
    {
        System.out.println("Finish");
        return P.L();
    }

    private void setPipeline()
    {
        System.out.println("Finish");
    }

    private void setP(Script S)
    {
        D = checkPath(S);
        if(D != null)
        {
            P = new Prozessor(S,D);
        }
    }

    private DataType checkPath(Script S) {
        List<Script>
                L = new ArrayList<>(S.split("\\."));

        switch ((L.get(L.size() - 1)).toString()) {
            case "json":
                return DataType.JSON;
            case "txt":
                return DataType.TXT;
            default:
                return null;
        }
    }
}
