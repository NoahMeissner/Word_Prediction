import Preprocessing.PreProcessing;
import lingologs.Script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class test {


    public static void main(String[] args) {
        /*
        String t;
        try {
            t = Files.readAllLines(Path.of("/Users/noahmeissner/Documents/github/Word_Prediction/untitled/src/text.txt")).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PreProcessing preProcessing = new PreProcessing(Script.of("Hallo wie geht dir da"));
        */
        Random rand = new Random();
        int zahl = rand.nextInt(6) + 1;
        System.out.println(SearchMaxtuples(new Random(),new int[20]));

    }
    private static String[][] MakeArray(int N)
    {
        String[][] result = new String[N][N];
        for(int i = 0; i<N;i++)
        {
            for (int a = 0; a<N;N++)
            {
                result[i][a]= "["+i+","+a+"]";
            }
        }
        return result;
    }
    private static int SearchMaxtuples(Random R, int[] X)
    {
        int result = 0;
        int zwischen =0;
        int zahl=0;
        for (int i = 0; i<X.length;i++)
        {
            X[i]= R.nextInt(6)+1;
            System.out.println(X[i]);
        }
        for(int i = 0; i<X.length;i++)
        {
            if(zahl == 0)
            {
                zahl=X[i];
                zwischen = 1;
            }
            if(zahl!=X[i])
            {
                zahl = X[i];
                if(result<zwischen)
                {
                    result = zwischen;
                }
                zwischen = 1;

            }
            else{
                zwischen+=1;
            }
        }

        return result;
    }


}
