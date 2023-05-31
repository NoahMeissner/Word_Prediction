import Training.PreProcessing;
import Training.normalisieren.CleanText;
import lingologs.Script;

import javax.swing.tree.TreeNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;


public class classtest {
    public static void main(String[] args) {



        String T;
        try {
             T = Files.readString(Path.of("/Users/noahmeissner/Documents/Data/projectreischer/untitled/src/text.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PreProcessing P = new PreProcessing(Script.of(T));
        P.start();


    }


}
