package DB;

import java.util.ArrayList;

public class Experiment {
    public String startTime;
    public String endTime;
    public ArrayList<DBObject> items;

    public Experiment(String startTime, String endTime, ArrayList<DBObject> items) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.items = items;
    }

    public Experiment() {
        this("N/A", "N/A", new ArrayList<>());
    }


}
