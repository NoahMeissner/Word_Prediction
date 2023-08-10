public class testka {

   static class TimeData {
        private final int hour;
        private final int min;
        private final int sec;

        public TimeData(int hour, int min, int sec) throws Exception {
            if (hour >= 24 || hour < 0  || min >= 60 || min < 0 || sec >= 60 || sec < 0) {
                throw new Exception("falsche Werte");
            }
            this.hour = hour;
            this.min = min;
            this.sec = sec;
        }

        public TimeData(TimeData time) throws Exception {
            this(time.hour, time.min, time.sec);
            System.out.println(this.hour);
        }
    }

    public static void main(String[] args) throws Exception {
        Object O = new String();
      //  Integer N = new Number();
    }
}

