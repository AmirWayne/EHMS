package Controller;

import DBH.patientDAO;
import DBH.therapistDAO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainPaneController implements Initializable,Util.JavafxPaneHandler{
    DBH.therapistDAO tbh=new therapistDAO();
    DBH.patientDAO pbh=new patientDAO();
    @FXML
    private TableView<?> TableViewNotifications;

    @FXML
    private PieChart PieChartRequests;

    @FXML
    private PieChart PieChartActive;

    @FXML
    private Button BtnDay;

    @FXML
    private Button BrnMonthly;

    @FXML
    private Button BtnQuarterly;
    @FXML
    private Label LabelActiveTherapist;


    @FXML
    private Label LabelActivePatient;


    @FXML
    private BarChart<?, ?> BarChartNotifications;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    void OnClickDay(ActionEvent event) {
        CategoryAxis xAxis = new CategoryAxis();
        //xAxis.setCategories(FXCollections.<String>observableArrayList(month));
        NumberAxis yAxis = new NumberAxis("Units", 0.0d, 3000.0d, 1000.0d);
        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
                new BarChart.Series("Medicines", FXCollections.observableArrayList(

                        new BarChart.Data("Today", 3d)
                )),
                new BarChart.Series("Allergies", FXCollections.observableArrayList(
                        new BarChart.Data("Today", 2d)
                )),
                new BarChart.Series("Meetings", FXCollections.observableArrayList(
                        new BarChart.Data("Today", 1d)
                ))
        );
        BarChart chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
        BarChartNotifications.getData().clear();
        BarChartNotifications.getData().addAll(chart.getData());




    }

    @FXML
    void OnClickMonthly(ActionEvent event) {
        String[] month = {"1","2","3","4","5",
                            "7","8","9","10","11",
                                "12","13","14","15","16",
                                    "17","18","19","20","21",
                                        "22","23","24","25","26",
                                            "27","28","29","30","31"};
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(month));
        NumberAxis yAxis = new NumberAxis("Units", 0.0d, 3000.0d, 1000.0d);
        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
                new BarChart.Series("Medicines", FXCollections.observableArrayList(

                        new BarChart.Data(month[1], 3d),
                        new BarChart.Data(month[4], 2d),
                        new BarChart.Data(month[5], 1d),
                        new BarChart.Data(month[8], 4d),
                        new BarChart.Data(month[10], 1d),
                        new BarChart.Data(month[18], 0d),
                        new BarChart.Data(month[20], 0d),
                        new BarChart.Data(month[28], 2d)


                )),
                new BarChart.Series("Allergies", FXCollections.observableArrayList(
                        new BarChart.Data(month[3], 1d),
                        new BarChart.Data(month[7], 2d),
                        new BarChart.Data(month[9], 4d),
                        new BarChart.Data(month[14], 3d),
                        new BarChart.Data(month[19], 1d),
                        new BarChart.Data(month[21], 1d),
                        new BarChart.Data(month[24], 2d),
                        new BarChart.Data(month[27], 5d)
                )),
                new BarChart.Series("Meetings", FXCollections.observableArrayList(
                        new BarChart.Data(month[2], 7d),
                        new BarChart.Data(month[3], 4d),
                        new BarChart.Data(month[2], 1d),
                        new BarChart.Data(month[7], 3d),
                        new BarChart.Data(month[12], 6d),
                        new BarChart.Data(month[16], 2d),
                        new BarChart.Data(month[22], 3d),
                        new BarChart.Data(month[21], 4d)
                ))
        );
        BarChart chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
        BarChartNotifications.getData().clear();
        BarChartNotifications.getData().addAll(chart.getData());
    }

    @FXML
    void OnClickQuarterly(ActionEvent event) {
        String[] month = {"January","Last-Month","Current"};

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(month));
        NumberAxis yAxis = new NumberAxis("Units", 0.0d, 3000.0d, 1000.0d);
        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
                new BarChart.Series("Medicines", FXCollections.observableArrayList(

                        new BarChart.Data(month[0], 5),
                        new BarChart.Data(month[1], 0d),
                        new BarChart.Data(month[2], 3d)


                )),
                new BarChart.Series("Allergies", FXCollections.observableArrayList(
                        new BarChart.Data(month[0], 2d),
                        new BarChart.Data(month[1], 1d),
                        new BarChart.Data(month[2], 0d)


                )),
                new BarChart.Series("Meetings", FXCollections.observableArrayList(
                        new BarChart.Data(month[0], 1d),
                        new BarChart.Data(month[1], 1d),
                        new BarChart.Data(month[2], 2d)


                ))
        );
        BarChart chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
        BarChartNotifications.getData().clear();
        BarChartNotifications.getData().addAll(chart.getData());
    }

    @Override
    public void JavafxTableFill() throws SQLException {

    }

    @Override
    public void JavafxChoiceFill() {

    }

    @Override
    public void JavafxDiagramFill() throws IOException {


        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Iphone 5S", 13),
                new PieChart.Data("Samsung Grand", 25),
                new PieChart.Data("MOTO G", 10),
                new PieChart.Data("Nokia Lumia", 22));

        ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList(
                new PieChart.Data("Iphone 5S", 13),
                new PieChart.Data("Samsung Grand", 25),
                new PieChart.Data("MOTO G", 10),
                new PieChart.Data("Nokia Lumia", 22));

        PieChartRequests.setData(pieChartData);
       PieChartRequests.setTitle("Requests");
       PieChartRequests.setLegendVisible(true);
       PieChartRequests.setLabelsVisible(false);
      PieChartRequests.setLegendSide(Side.RIGHT);

        PieChartActive.setData(pieChartData2);
        PieChartActive.setTitle("Requests");
        PieChartActive.setLegendVisible(true);
        PieChartActive.setLabelsVisible(false);
        PieChartActive.setLegendSide(Side.RIGHT);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            LabelActiveTherapist.setText(Integer.toString(tbh.getCount()));
            LabelActivePatient.setText(Integer.toString(pbh.getCount()));
            JavafxDiagramFill();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

}