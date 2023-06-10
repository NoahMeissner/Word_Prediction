package UI;

public class Feedback {
    /*
    Wenn ein Fehler kommt soll dieser als UI.Feedback vermittelt werden.
    Dafuer ist diese Klasse vorhanden
     */
    private int percent;
    private Exception e;
    private boolean stoppprogress;

    public Feedback(int percent)
    {
        this.percent = percent;
        stoppprogress = true;
    }
    public Feedback (int percent, Exception e)
    {
        this.percent = percent;
        this.e = e;
        stoppprogress = false;
    }

}
