package sample;

import DB.DBObject;
import DB.Experiment;
import Parse.Parser;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class Controller {

    @FXML
    private ComboBox cbField;
    @FXML
    private ComboBox cbItems;
    @FXML
    private TableView tableData;
    @FXML
    private LineChart chartField;

    private ArrayList<Experiment> items;
    private int currentItemIndex;

    public void initialize() {
        items = new ArrayList<>();
        ObservableList<String> options = FXCollections.observableArrayList("Откройте файл с данными!");
        cbItems.setItems(options);
    }

    public void FillTable(ArrayList<DBObject> items) {
        tableData.getColumns().clear();
        if (items.size() > 0) {
            for (int i = 0; i < items.get(0).fields.size(); i++) {
                final int finalIdx = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>();
                column.setText(items.get(0).fields.get(i).name);
                column.setCellValueFactory(
                        param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
                tableData.getColumns().add(column);
            }
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            for (int i = 0; i < items.size(); i++) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int j = 0; j < items.get(i).fields.size(); j++)
                    row.add(items.get(i).fields.get(j).value);
                data.add(row);
            }
            tableData.setItems(data);
        }
    }

    public void Parse(String fileName) {
        Parser parser = new Parser();
        try {
            //String path = System.getProperty("user.dir");
            //ArrayList<Experiment> items = parser.GetExperiments(path + "\\src\\Data\\ex1.txt");
            items = parser.GetExperiments(fileName);
            ObservableList<String> options = FXCollections.observableArrayList();
            for (int i = 0; i < items.size(); i++)
                options.add("Эксперимент " + String.valueOf(i + 1));
            cbItems.setItems(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnOPen_OnClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files", ".txt");
        fileChooser.setSelectedExtensionFilter(filter);
        File file = fileChooser.showOpenDialog(new Stage());
        Parse(file.getPath());
    }

    public void cbItems_Click(ActionEvent actionEvent) {
        this.currentItemIndex = -1;
        SingleSelectionModel selectionModel = cbItems.getSelectionModel();
        int index = selectionModel.getSelectedIndex();
        if (index > -1 && index < this.items.size()) {
            this.FillTable(items.get(index).items);
            ObservableList<String> options = FXCollections.observableArrayList();
            if (items.get(index).items.size() > 0)
                for (int i = 0; i < items.get(index).items.get(0).fields.size(); i++)
                    options.add(items.get(index).items.get(0).fields.get(i).name);
            cbField.setItems(options);
            this.currentItemIndex = index;
        }
    }

    public void cbField_Click(ActionEvent actionEvent) throws Exception {
        if (this.currentItemIndex > -1) {
            SingleSelectionModel selectionModel = cbField.getSelectionModel();
            int fieldIndex = selectionModel.getSelectedIndex();
            ObservableList<XYChart.Data> dataList = FXCollections.observableArrayList();
            for (int i = 0; i < items.get(this.currentItemIndex).items.size(); i++) {
                Double yValue = Double.valueOf(items.get(this.currentItemIndex).items.get(i).GetField(fieldIndex).value);
                Double xValue = i * 10.0;
                dataList.add(new XYChart.Data(xValue, yValue));
            }

            //final NumberAxis xAxis = new NumberAxis();
            //final NumberAxis yAxis = new NumberAxis();
            //xAxis.setLabel("Month");
            //yAxis.setLabel("Value");
            //chartField = new LineChart<Double, Double>(xAxis, yAxis);
            String fieldName = "N/A";
            if (items.get(this.currentItemIndex).items.size() > 0)
                fieldName = items.get(this.currentItemIndex).items.get(0).GetField(fieldIndex).name;
            chartField.setTitle(fieldName);
            XYChart.Series series = new XYChart.Series(dataList);
            series.setName("Значения поля "+fieldName);
            chartField.getData().add(series);
        }


    }

    public void btnClearChart_Click(ActionEvent actionEvent) {
        chartField.getData().clear();
    }
}
