package Part_Of_Speech_Tagging;

import No_POS_Tagging.Preprocessing.LoadTrainingData;
import Part_Of_Speech_Tagging.Training.TrainListP;

public class test {
    static LoadTrainingData L = new LoadTrainingData();

    public static void main(String[] args) {
        TrainListP trainListP = new TrainListP(L.getData(),"Henry V",true,true,true);
        trainListP.trainWeights();

    }

}
