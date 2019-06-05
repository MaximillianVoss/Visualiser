package sample;

import DB.DBObject;
import DB.Experiment;
import Parse.FileIO;
import Parse.Parser;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    @FXML
    private ComboBox cbFromMain;
    @FXML
    private ComboBox cbToMain;
    @FXML
    private ComboBox cbFrom1;
    @FXML
    private ComboBox cbTo1;
    @FXML
    private ComboBox cbFrom2;
    @FXML
    private ComboBox cbTo2;
    @FXML
    private ComboBox cbField1;
    @FXML
    private ComboBox cbField2;
    @FXML
    private ComboBox cbField;
    @FXML
    private ComboBox cbItems;
    @FXML
    private TableView tableData;
    @FXML
    private LineChart chartField;
    @FXML
    private LineChart chartField1;
    @FXML
    private LineChart chartField2;

    private ArrayList<Experiment> items;
    private int currentItemIndex;
    private int currentFieldMain;
    private int currentField1;
    private int currentField2;
    private int fromIndex;
    private int toIndex;
    private int fromIndex1;
    private int toIndex1;
    private int fromIndex2;
    private int toIndex2;
    private double timeStep = 20.0;
    private String timeTitle = "Время (мс)";
    private String errorTitle = "Ошибка";

    public void initialize() {
        this.currentItemIndex = -1;
        this.fromIndex = -1;
        this.toIndex = -1;
        items = new ArrayList<>();
        FillComboBox(cbItems, FXCollections.observableArrayList("Откройте файл с данными!"));
        FillComboBox(cbField, FXCollections.observableArrayList("Выберите параметр!"));
        FillComboBox(cbField1, FXCollections.observableArrayList("Выберите параметр!"));
        FillComboBox(cbField2, FXCollections.observableArrayList("Выберите параметр!"));
    }

    private ObservableList<XYChart.Data> GetChartData(String fieldName, int itemIndex, int fromIndex, int toIndex) throws Exception {
        ObservableList<XYChart.Data> data = FXCollections.observableArrayList();
        if (itemIndex < 0 || fromIndex < 0 || toIndex < 0 || fromIndex > toIndex)
            throw new Exception("Некорректный индекс!");
        for (int i = fromIndex; i < items.get(itemIndex).items.size() && i <= toIndex; i++) {
            Double yValue = Double.valueOf(items.get(itemIndex).items.get(i).GetField(fieldName).value);
            Double xValue = i * timeStep;
            data.add(new XYChart.Data(xValue, yValue));
        }
        return data;
    }

    private void FillTable(ArrayList<DBObject> items) {
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

    private void ClearChart(LineChart chart) {
        chart.getData().clear();
    }

    private void FillChart(LineChart chart, ObservableList<XYChart.Data> dataList, String xTitle, String yTitle, String seriesTitle) {
        chart.setTitle(seriesTitle);
        chart.getXAxis().setLabel(xTitle);
        chart.getYAxis().setLabel(yTitle);
        XYChart.Series series = new XYChart.Series(dataList);
        series.setName(seriesTitle);
        chart.getData().add(series);
    }

    private void FillComboBox(ComboBox cb, ArrayList<String> items) {
        this.FillComboBox(cb, FXCollections.observableArrayList(items));
    }

    private void FillComboBox(ComboBox cb, ObservableList<String> items) {
        if (items.size() > 0) {
            cb.setItems(items);
            cb.getSelectionModel().select(0);
        }
    }

    private void Parse(String fileName) {
        Parser parser = new Parser();
        try {
            //String path = System.getProperty("user.dir");
            //ArrayList<Experiment> items = parser.GetExperiments(path + "\\src\\Data\\ex1.txt");
            items = parser.GetExperiments(fileName);
            ObservableList<String> experimentOptions = FXCollections.observableArrayList();
            for (int i = 0; i < items.size(); i++)
                experimentOptions.add(items.get(i).startTime);
            cbItems.setItems(experimentOptions);
        } catch (Exception e) {
            e.printStackTrace();
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }

    private void UpdateChart(LineChart chart, int fieldIndex, int fromIndex, int toIndex) throws Exception {
        if (fieldIndex > -1 && fromIndex > -1 && toIndex > -1) {
            String fieldName = "N/A";
            if (items.get(this.currentItemIndex).items.size() > 0)
                fieldName = items.get(this.currentItemIndex).items.get(0).GetField(fieldIndex).name;
            ObservableList<XYChart.Data> dataList = this.GetChartData(fieldName, this.currentItemIndex, fromIndex, toIndex);
            this.FillChart(chart, dataList, fieldName, this.timeTitle, fieldName);
        }
    }

    private void ShowMessage(String message, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(message);
        alert.setHeaderText("Внимание");
        alert.setContentText(content);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
                alert.close();
            }
        });
    }

    public void btnOPen_OnClick(ActionEvent actionEvent) {
        try {
            this.items.clear();
            this.currentItemIndex = -1;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files", ".txt");
            fileChooser.setSelectedExtensionFilter(filter);
            File file = fileChooser.showOpenDialog(new Stage());
            Parse(file.getPath());
            this.ShowMessage("Файл открыт!", "Файл успешно считан, выберите нужные данные из списка.");
        } catch (Exception e) {
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }

    public void btnClearChart_Click(ActionEvent actionEvent) {
        chartField.getData().clear();
    }

    public void cbItems_Click(ActionEvent actionEvent) {
        this.currentItemIndex = -1;
        int index = cbItems.getSelectionModel().getSelectedIndex();
        if (index > -1 && index < this.items.size()) {
            this.FillTable(items.get(index).items);
            ObservableList<String> options = FXCollections.observableArrayList();
            if (items.get(index).items.size() > 0)
                for (int i = 0; i < items.get(index).items.get(0).fields.size(); i++)
                    options.add(items.get(index).items.get(0).fields.get(i).name);
            this.currentItemIndex = index;
            FillComboBox(cbField, options);
            FillComboBox(cbField1, options);
            FillComboBox(cbField2, options);
            ObservableList<String> timeOptions = FXCollections.observableArrayList();
            for (int i = 0; i < items.get(index).items.size(); i++)
                timeOptions.add(String.valueOf(i * timeStep));
            FillComboBox(cbFromMain, timeOptions);
            FillComboBox(cbToMain, timeOptions);
            FillComboBox(cbTo1, timeOptions);
            FillComboBox(cbTo2, timeOptions);
            FillComboBox(cbFrom1, timeOptions);
            FillComboBox(cbFrom2, timeOptions);
        }
    }

    public void cbField_Click(ActionEvent actionEvent) {
        try {
            this.currentFieldMain = cbField.getSelectionModel().getSelectedIndex();
            UpdateChart(chartField, this.currentFieldMain, this.fromIndex, this.toIndex);
        } catch (Exception e) {
            e.printStackTrace();
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }

    public void cbField1_Click(ActionEvent actionEvent) {
        try {
            this.currentField1 = cbField1.getSelectionModel().getSelectedIndex();
            UpdateChart(chartField1, this.currentField1, this.fromIndex1, this.toIndex1);
        } catch (Exception e) {
            e.printStackTrace();
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }

    public void cbField2_Click(ActionEvent actionEvent) {
        try {
            this.currentField2 = cbField2.getSelectionModel().getSelectedIndex();
            UpdateChart(chartField2, this.currentField2, this.fromIndex2, this.toIndex2);
        } catch (Exception e) {
            e.printStackTrace();
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }

    public void cbFromMain_Click(ActionEvent actionEvent) {
        this.fromIndex = cbFromMain.getSelectionModel().getSelectedIndex();
        try {
            ClearChart(chartField);
            UpdateChart(chartField, this.currentFieldMain, this.fromIndex, this.toIndex);
        } catch (Exception e) {
            e.printStackTrace();
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }

    public void cbToMain_Click(ActionEvent actionEvent) {
        this.toIndex = cbToMain.getSelectionModel().getSelectedIndex();
        try {
            ClearChart(chartField);
            UpdateChart(chartField, this.currentFieldMain, this.fromIndex, this.toIndex);
        } catch (Exception e) {
            e.printStackTrace();
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }

    public void btnClear1_Click(ActionEvent actionEvent) {
        ClearChart(chartField1);
    }

    public void btnClear2_Click(ActionEvent actionEvent) {
        ClearChart(chartField2);
    }

    public void cbFrom1_Click(ActionEvent actionEvent) {
        try {
            this.fromIndex1 = cbFrom1.getSelectionModel().getSelectedIndex();
            UpdateChart(chartField1, this.currentField1, this.fromIndex1, this.toIndex1);
        } catch (Exception e) {
            e.printStackTrace();
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }

    public void cbTo1_Click(ActionEvent actionEvent) {
        try {
            this.toIndex1 = cbTo1.getSelectionModel().getSelectedIndex();
            UpdateChart(chartField1, this.currentField1, this.fromIndex1, this.toIndex1);
        } catch (Exception e) {
            e.printStackTrace();
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }

    public void cbFrom2_Click(ActionEvent actionEvent) {
        try {
            this.fromIndex2 = cbFrom2.getSelectionModel().getSelectedIndex();
            UpdateChart(chartField2, this.currentField2, this.fromIndex2, this.toIndex2);
        } catch (Exception e) {
            e.printStackTrace();
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }

    public void cbTo2_Click(ActionEvent actionEvent) {
        try {
            this.toIndex2 = cbTo2.getSelectionModel().getSelectedIndex();
            UpdateChart(chartField2, this.currentField2, this.fromIndex2, this.toIndex2);
        } catch (Exception e) {
            e.printStackTrace();
            this.ShowMessage(this.errorTitle, e.getMessage());
        }
    }


    public void btnSave_Click(ActionEvent actionEvent) {
        if (this.currentItemIndex > -1 && this.fromIndex > -1 && this.toIndex > -1) {
            Experiment current = this.items.get(this.currentItemIndex);
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save File");
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files", ".txt");
                fileChooser.setSelectedExtensionFilter(filter);
                File file = fileChooser.showSaveDialog(new Stage());
                FileIO.WriteLine(file.getPath(), current.toString(this.fromIndex, this.toIndex), false);
                this.ShowMessage("Операция завершена", "Файл сохранен");

            } catch (Exception e) {
                e.printStackTrace();
                this.ShowMessage(this.errorTitle, e.getMessage());
            }
        }
    }
}
