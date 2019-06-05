package DB;

import java.util.ArrayList;

public class Experiment {
    public String startTime;
    public String endTime;
    public ArrayList<DBObject> items;
    private String startToken = "<<<<<<<<<<";
    private String endToken = ">>>>>>>>>>";

    public Experiment(String startTime, String endTime, ArrayList<DBObject> items) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.items = items;
    }

    public Experiment() {
        this("N/A", "N/A", new ArrayList<>());
    }

    public String toString() {
        try {
            return this.toString(0, this.items.size());
        } catch (Exception e) {
            e.printStackTrace();
            return "error while convert to string!";
        }
    }

    public String toString(int from, int to) throws Exception {
        if (from < 0 || to < 0 || from > to)
            throw new Exception("Некорретное значение индексов!");
        String str = this.startToken + '\n';
        str += this.startTime + '\n';
        if (this.items.size() > 0) {
            for (int i = 0; i < this.items.get(0).fields.size(); i++)
                str += this.items.get(0).fields.get(i).name + ' ';
            str += '\n';
            for (int i = from; i < this.items.size() && i <= to; i++) {
                for (int j = 0; j < this.items.get(i).fields.size(); j++)
                    str += this.items.get(i).fields.get(j).value + ' ';
                str += '\n';
            }
        }
        str += this.endTime + '\n';
        str += this.endToken + '\n';
        return str;
    }
}
