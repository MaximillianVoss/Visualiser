package Parse;

import DB.DBObject;
import DB.Experiment;

import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private String startToken = "<<<<<<<<<<";
    private String endToken = ">>>>>>>>>>";

    public Parser() {

    }

    private ArrayList<String> Split(String str) {
        ArrayList<String> result = new ArrayList<>();
        String[] items = str.split(" ");
        for (int j = 0; j < items.length; j++)
            if (items[j].length() > 0)
                result.add(items[j]);
        return result;
    }

    public ArrayList<Experiment> GetExperiments(String filename) throws Exception {
        FileIO fileIO = new FileIO();
        ArrayList<Experiment> experiments = new ArrayList<>();
        ArrayList<DBObject> items = new ArrayList<>();
        String[] lines = fileIO.GetAllLines(filename);
        int counter = 0;
        String startTime = "", endTime = "";
        ArrayList<String> fieldNames = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() > 0) {
                if (lines[i].equals(this.startToken))
                    counter = 0;
                if (counter == 1) {
                    startTime = lines[i];
                } else if (counter == 2) {
                    fieldNames = Split(lines[i]);
                } else if (counter > 2 && !lines[i].equals(this.startToken) && !lines[i].equals(this.endToken)) {
                    ArrayList<String> fieldValues = Split(lines[i]);
                    if (fieldValues.size() == fieldNames.size())
                        items.add(new DBObject(fieldNames, fieldValues));
                }
                if (lines[i].equals(this.endToken)) {
                    if (i - 1 > 0)
                        endTime = lines[i - 1];
                    experiments.add(new Experiment(startTime, endTime, items));
                    items = new ArrayList<>();
                    fieldNames = new ArrayList<>();
                }
                counter++;
            }
        }
        return experiments;
    }
}
