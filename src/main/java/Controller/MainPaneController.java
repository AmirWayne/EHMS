package Controller;

import DBH.*;
import Model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class MainPaneController implements Initializable, Util.JavafxPaneHandler {


    //DATABASE HANDLERER DECLERATIONS
    DBH.therapistDAO tDAO = new therapistDAO();
    DBH.patientDAO pDAO = new patientDAO();
    DBH.notificationDAO nDAO = new notificationDAO();
    DBH.medicineDAO mDAO = new medicineDAO();
    DBH.AllergyDAO aDAO = new AllergyDAO();
    //ARRAYLISTS DECLERATIONS
    private ArrayList<Patient> patientArrayList = new ArrayList<Patient>();
    private ObservableList patientObservableList = FXCollections.observableArrayList();
    private ArrayList<Therapist> therapistArrayList = new ArrayList<Therapist>();
    private ArrayList<Medicine> medicineArrayList = new ArrayList<Medicine>();
    private ArrayList<Allergy> allergyArrayList = new ArrayList<Allergy>();
    private ArrayList<Notification> notificationArrayList = new ArrayList<Notification>();

    //OBSERVABLE DECLERATIONS
    ObservableList notificationObservable = FXCollections.observableArrayList();

    //pieChart.data decleration
    PieChart.Data highdata;
    PieChart.Data mediumdata;
    PieChart.Data lowdata;

    PieChart.Data waterdata;
    PieChart.Data mealdata;
    PieChart.Data toiletdata;
    PieChart.Data emergencydata;

    BarChart.Data patientwaterdata;
    BarChart.Data patiermealdata;
    BarChart.Data patienttoiletdata;
    BarChart.Data patientemergencydata;

    int size;


    @FXML
    private Pane parent;
    @FXML
    private TableView<Notification> TableViewNotifications;
    @FXML
    private TableColumn<Notification, Number> ColNum;

    @FXML
    private TableColumn<Notification, String> ColType;

    @FXML
    private TableColumn<Notification, String> ColDesc;

    @FXML
    private TableColumn<Notification, String> ColPatientName;

    @FXML
    private TableColumn<Notification, String> ColPatientID;

    @FXML
    private TableColumn<Notification, Date> ColTime;

    @FXML
    private TableColumn<Notification, String> ColTreated;
    @FXML

    private TableColumn<Notification, String> ColTherapistID;
    @FXML

    private TableColumn<Notification, String> ColTherapistName;
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
    private ChoiceBox<String> ChoicePatient;

    static Thread t;


    private void BarChartRefresher() throws SQLException {

        int countpatientwaterdata = 0;
        int countpatientmealdata = 0;
        int countpatienttoiletdata = 0;
        int countpatientemergencydata = 0;

        patientArrayList = pDAO.selectAll();
        for (Patient p : patientArrayList)
            if (p.getName().equals(ChoicePatient.getSelectionModel().getSelectedItem()))
                for (Notification n : notificationArrayList) {
                    if (n.getPatient().getID().equals(p.getID())) {
                        if (n.getRequest().returnReq().equals("Water"))
                            countpatientwaterdata++;
                        if (n.getRequest().returnReq().equals("Meal"))
                            countpatientmealdata++;
                        if (n.getRequest().returnReq().equals("Toilet"))
                            countpatienttoiletdata++;
                        if (n.getRequest().returnReq().equals("YOU"))
                            countpatientemergencydata++;
                    }
                }
        patientwaterdata.setYValue(countpatientwaterdata);
        patiermealdata.setYValue(countpatientmealdata);
        patienttoiletdata.setYValue(countpatienttoiletdata);
        patientemergencydata.setYValue(countpatientemergencydata);
    }

    @FXML
    public void onSelectPatient(ActionEvent event) throws SQLException {

        BarChartRefresher();
    }


        public void BarChartInitilizer() {
        patientwaterdata = new BarChart.Data("Today", 0);
        patiermealdata = new BarChart.Data("Today", 0);
        patienttoiletdata = new BarChart.Data("Today", 0);
        patientemergencydata = new BarChart.Data("Today", 0);
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis("Units", 0.0d, 3000.0d, 1000.0d);
        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
                new BarChart.Series("Water", FXCollections.observableArrayList(
                        patientwaterdata
                )),
                new BarChart.Series("Meal", FXCollections.observableArrayList(
                        patiermealdata
                )),
                new BarChart.Series("Toilet", FXCollections.observableArrayList(
                        patienttoiletdata
                )),
                new BarChart.Series("Emergency", FXCollections.observableArrayList(
                        patientemergencydata
                ))
        );
        BarChart chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
        BarChartNotifications.getData().clear();
        BarChartNotifications.getData().addAll(chart.getData());
    }



    public static void LaunchThread() {
        t.start();


    }


    public void TableInit() throws SQLException {
        ColNum.setCellValueFactory(new PropertyValueFactory<Notification, Number>("Num"));
        ColType.setCellValueFactory(CellData -> new SimpleStringProperty(CellData.getValue().getRequest().getType()));
        ColDesc.setCellValueFactory(CellData -> new SimpleStringProperty(CellData.getValue().getRequest().getDescription()));
        ColPatientID.setCellValueFactory(CellData -> new SimpleStringProperty(CellData.getValue().getPatient().getID()));
        ColPatientName.setCellValueFactory(CellData -> new SimpleStringProperty(CellData.getValue().getPatient().getName()));
        ColTime.setCellValueFactory(new PropertyValueFactory<Notification, Date>("date"));
        ColTreated.setCellValueFactory(new PropertyValueFactory<Notification, String>("isTreated"));
        ColTherapistID.setCellValueFactory(CellData -> new SimpleStringProperty(CellData.getValue().getTherapist().getID()));
        ColTherapistName.setCellValueFactory(CellData -> new SimpleStringProperty(CellData.getValue().getTherapist().getName()));
        //add your data to the table here.
        JavafxTableFill();
        TableViewNotifications.setItems(notificationObservable);
    }


    public void manualRefreshingTable() throws SQLException {
        notificationArrayList = nDAO.selectAll();
        Collections.reverse(notificationArrayList);
        notificationObservable.setAll(notificationArrayList);
        TableViewNotifications.setItems(notificationObservable);
        int highcount = 0;
        int lowcount = 0;
        int medcount = 0;
        int watercount = 0;
        int mealcount = 0;
        int toiletcount = 0;
        int emergencycount = 0;
        for (Notification n : notificationArrayList) {
            if (n.getRequest().getType().equals("Critical Urgency"))
                highcount++;
            if (n.getRequest().getType().equals("Low Urgency"))
                lowcount++;
            if (n.getRequest().getType().equals("Medium Urgency"))
                medcount++;
            if (n.getRequest().returnReq().equals("Water"))
                watercount++;
            if (n.getRequest().returnReq().equals("Meal"))
                mealcount++;
            if (n.getRequest().returnReq().equals("Toilet"))
                toiletcount++;
            if (n.getRequest().returnReq().equals("YOU"))
                emergencycount++;
        }


        highdata.setPieValue(highcount);
        mediumdata.setPieValue(medcount);
        lowdata.setPieValue(lowcount);


        mealdata.setPieValue(mealcount);
        toiletdata.setPieValue(toiletcount);
        waterdata.setPieValue(watercount);
        emergencydata.setPieValue(emergencycount);
    }


    private void MediaPlayerBeep() {
        try {
            if (size < notificationArrayList.size()) {
                String path = new File("src/main/resources/Sound/tone.mp3").getAbsolutePath();
                File f = new File(path);
                AudioClip me = new AudioClip(f.toURI().toString());
                me.play();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void JavafxTableFill() throws SQLException {

        notificationArrayList = nDAO.selectAll();
        notificationObservable.setAll(notificationArrayList);

    }

    @Override
    public void JavafxChoiceFill() throws SQLException {
        ChoicePatient.getItems().removeAll();
        patientArrayList = pDAO.selectAll();
        for (Patient p : patientArrayList)
            ChoicePatient.getItems().add(p.getName());

    }

    @Override
    public void JavafxDiagramFill() throws IOException {
        highdata = new PieChart.Data("High Urgency", 1);
        mediumdata = new PieChart.Data("Medium Urgency", 2);
        lowdata = new PieChart.Data("Low Urgency", 3);
        waterdata = new PieChart.Data("Water", 1);
        mealdata = new PieChart.Data("Meal", 2);
        toiletdata = new PieChart.Data("Toilet", 3);
        emergencydata = new PieChart.Data("Emergency", 4);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(highdata, mediumdata, lowdata);
        ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList(waterdata, mealdata, toiletdata, emergencydata);
        PieChartRequests.setData(pieChartData);
        PieChartRequests.setTitle("Urgency Level");
        PieChartRequests.setLegendVisible(true);

        PieChartRequests.setLabelsVisible(false);
        PieChartRequests.setLegendSide(Side.RIGHT);
        PieChartActive.setData(pieChartData2);
        PieChartActive.setTitle("Requests Types");
        PieChartActive.setLegendVisible(true);
        PieChartActive.setLabelsVisible(false);
        PieChartActive.setLegendSide(Side.RIGHT);
        BarChartInitilizer();

    }

    private void CssStyler() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.load(getClass().getResource("/FXML/Settings.fxml").openStream());
            SettingsController settingsController = loader.getController();
            parent.getStylesheets().removeAll();
            if (settingsController.isCustomeDesignFlag()) {
                String css = this.getClass().getResource("/Css/UserCustomDesign.css").toExternalForm();
                parent.getStylesheets().add(css);
            } else {
                if (settingsController.getToggleMode()) {
                    String css = this.getClass().getResource("/Css/darkmode.css").toExternalForm();
                    parent.getStylesheets().add(css);
                } else if (!settingsController.getToggleMode()) {
                    String css = this.getClass().getResource("/Css/lightmode.css").toExternalForm();
                    parent.getStylesheets().add(css);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CssStyler();

        try {
            TableInit();
            JavafxChoiceFill();
            LabelActiveTherapist.setText(Integer.toString(tDAO.getCount()));
            LabelActivePatient.setText(Integer.toString(pDAO.getCount()));
            JavafxDiagramFill();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        ColNum.setSortType(TableColumn.SortType.ASCENDING);
        TableViewNotifications.getSortOrder().add(ColNum);
        TableViewNotifications.sort();
        ColTreated.setCellFactory(column -> {
            return new TableCell<Notification, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    TableRow currentRow = getTableRow();
                    if (item == null || empty)
                        setText(null);
                    else {
                        setText(item);
                        String istreated = item;
                        if (istreated.equals("true")) {
                            currentRow.setStyle("-fx-background-color : #70ff4d;");
                            setStyle("-fx-text-fill : #ffffff;");
                        }
                    }
                }
            };
        });
        ColType.setCellFactory(column -> {
            return new TableCell<Notification, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    TableRow currentRow = getTableRow();
                    if (item == null || empty) {
                        setText(null);
                        currentRow.setStyle("-fx-background-color : #inherited");
                    } else {
                        setText(item);
                        // Style by Urgency Level
                        String urgency = item;

                        if (urgency.equals("Low Urgency")) {
                            currentRow.setStyle("-fx-background-color: #66a3ff;");
                        } else if (urgency.equals("Medium Urgency"))
                            currentRow.setStyle("-fx-background-color: #f9ff57;");
                        else if (urgency.equals("Critical Urgency"))
                            currentRow.setStyle("-fx-background-color: #ff383f;");
                        else
                            currentRow.setStyle("-fx-background-color : #000000");
                    }
                }
            };
        });


        NotificationRefresherThreadCreator();
        LaunchThread();

    }


    private void NotificationRefresherThreadCreator() {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (t.isAlive()) {
                    try {
                        System.out.println("Refreshing in one second");
                        size = notificationArrayList.size();
                        manualRefreshingTable();
                        MediaPlayerBeep();
                        BarChartRefresher();
                        TimeUnit.SECONDS.sleep(2);
                    } catch (SQLException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.setName("Notification Refresher Thread.");

    }

}
