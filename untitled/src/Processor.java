import No_POS_Tagging.Preprocessing.LoadTrainingData;
import No_POS_Tagging.Preprocessing.Test_weights.TestWeightsListNP;
import No_POS_Tagging.Preprocessing.Training.TrainListNP;
import Part_Of_Speech_Tagging.PosTags;
import Part_Of_Speech_Tagging.Test.TestWeightsListP;
import Part_Of_Speech_Tagging.Training.TrainListP;
import Predictor.Loader;
import UI.Chat;
import UI.Tags;
import lingolava.Tuple.Couple;
import lingologs.Script;

import java.util.HashMap;
import java.util.Map;

/**
 * The Processor class serves as the central component of the project, responsible for
 * managing configuration settings, orchestrating the training and testing processes,
 * and setting up the chat interface. It plays a pivotal role in controlling the flow
 * of the project's core functionality.

 * The Processor class is constructed with configuration parameters and links that dictate
 * how the project should operate. It initializes and manages key components such as training,
 * testing, and chat setup, ensuring a seamless execution of the project's objectives.
 */
public class Processor {



    private final String testData = "Henry V";
    private final Map<Tags,Integer> config;
    private final Map<Tags,String> links;
    private boolean postag;
    private boolean preprocessing;
    private boolean howprocess;
    private boolean ngrams;
    private boolean learn;
    private final boolean testAll;
    private final boolean testConfig;
    private Couple<HashMap<Script, HashMap<PosTags, HashMap<Script, Integer>>>,
            HashMap<Script, HashMap<PosTags, Integer>>> CP;

    private Map.Entry<HashMap<Script, HashMap<Script, Integer>>, HashMap<Script, HashMap<Script, Integer>>> MNP;

    public Processor(Map<Tags,Integer> config, Map<Tags,String> links, boolean testConfig,boolean testAll){
        this.config = config;
        this.links= links;
        this.testAll = testAll;
        this.testConfig = testConfig;
        setConfig(config);
        TrainWeights();
    }

    private void setConfig(Map<Tags,Integer> config)
    {
        postag = config.get(Tags.postag) == 1;
        preprocessing = config.get(Tags.preprocessing) == 1;
        if(postag)
        {
            System.out.println(config.get(Tags.howprocess));
            howprocess = config.get(Tags.howprocess) == 1;
        }
        ngrams = config.get(Tags.ngrams) == 2;
        learn = config.get(Tags.learn) == 1;
        Loader L = new Loader(config);
    }

    private void TrainWeights(){
        long startTime = System.currentTimeMillis();

        if(postag){
            if(!(config.get(Tags.config)==1)) {
                LoadTrainingData LT = new LoadTrainingData();
                TrainListP TP = new TrainListP(LT.getData(), testData, howprocess, ngrams,preprocessing);
                TrainListNP TNP = new TrainListNP(LT.getData(), testData,preprocessing);
                Loader L = new Loader(TP.trainWeights());
                Loader LOP = new Loader(TNP.getWeights());
            }
        }
        else{
            if(!(config.get(Tags.config)==1)) {
                LoadTrainingData LT = new LoadTrainingData();
                TrainListNP TNP = new TrainListNP(LT.getData(), testData,preprocessing);
                Loader L = new Loader(TNP.getWeights());
            }
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Runtime Training " + elapsedTime + " Milliseconds");

        if(testConfig)
        {
            TestWeights();
        }
        else{
            setUpChat(postag,
                    config.get(Tags.ngrams)==2,
                    howprocess);
        }
    }

    private void setUpChat(boolean postag,boolean UOB, boolean ROP) {
        Loader L = new Loader();
        if(postag )
        {

            Chat C = new Chat(L.getDataP(),L.getDataNP(),UOB,ROP, links,preprocessing);
        }
        else{
            Chat C = new Chat(L.getDataNP(),UOB, links,preprocessing);
        }
    }

    private void TestWeights()
    {
        long startTime = System.currentTimeMillis();
        Loader L = new Loader();
        LoadTrainingData LT = new LoadTrainingData();
        if(postag)
        {
            CP = L.getDataP();
            TestWeightsListP TP = new TestWeightsListP(ngrams,CP,howprocess,learn,preprocessing,testAll);
            System.out.println(TP.testWeights(LT.getData(),testData));
        }
        else {
            MNP = L.getDataNP();
            TestWeightsListNP TNP = new TestWeightsListNP(LT.getData(), testData, MNP, ngrams, learn, preprocessing);
            System.out.println(TNP.testWeights());

        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Runtime Testing " + elapsedTime + " Milliseconds");

    }
}
