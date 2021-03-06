package Controller;


import DBH.*;
import Model.*;

import Util.FilesHandler;
import Util.MessageAlerter;
import Util.PdfExporter;
import com.itextpdf.text.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class PatientPaneController implements Initializable, Util.JavafxPaneHandler {

    public ArrayList<Patient> ALPATIENT = new ArrayList<Patient>();
    private ObservableList<Model.Patient> Patients = FXCollections.observableArrayList(ALPATIENT);


    ArrayList<Model.patient_allergy> patient_allergyArrayList = new ArrayList<patient_allergy>();
    ArrayList<Model.patient_meal> patient_mealArrayList = new ArrayList<patient_meal>();
    ArrayList<Model.patient_medicine> patient_medicineArrayList = new ArrayList<patient_medicine>();
    ArrayList<Model.patient_meeting> patient_meetingArrayList = new ArrayList<patient_meeting>();

    DBH.patientDAO PDH = new DBH.patientDAO();

    DBH.patient_mealDAO pmealDAO = new patient_mealDAO();
    DBH.patient_meetingDAO pmeetingDAO = new patient_meetingDAO();
    DBH.patient_medicineDAO pmedDAO = new patient_medicineDAO();
    DBH.patient_allergyDAO pallergyDAO = new patient_allergyDAO();

    DBH.medicineDAO medDAO = new medicineDAO();
    DBH.AllergyDAO allDAO = new AllergyDAO();
    DBH.mealDAO mealDAO = new mealDAO();
    DBH.meetingDAO meetDAO = new meetingDAO();


    BarChart.Data medicinedata;
    BarChart.Data allergydata;
    BarChart.Data meetingdata;
    BarChart.Data mealdata;
    PdfExporter pdfExporter = new PdfExporter();

    ObservableList list = FXCollections.observableArrayList();

    MessageAlerter ma = new MessageAlerter();

    @FXML
    private Pane parent;
    @FXML
    private Button BtnPrint;
    @FXML
    public TableView<Patient> PatientTable;
    @FXML
    private TableColumn<Patient, String> ColID;
    @FXML
    private TableColumn<Patient, String> ColName;
    @FXML
    private TableColumn<Patient, String> ColAddress;
    @FXML
    private TableColumn<Patient, String> ColGender;
    @FXML
    private TableColumn<Patient, Date> ColBdate;
    @FXML
    private ChoiceBox<String> ChoicePatient;
    @FXML
    private BarChart<?, ?> PatientBarChart;
    @FXML
    private CategoryAxis y;
    @FXML
    private NumberAxis x;
    @FXML
    private Button BtnPatientFile;
    @FXML
    private Button BtnSpecPatientFile;
    @FXML
    private Button BtnPatientXML;
    @FXML
    private Button BtnRemovePatient;
    @FXML
    private Button BtnLodPatient;
    @FXML
    private Label LblPatientID;

    Util.FilesHandler fh;


    @FXML
    void onSelectPatient(ActionEvent event) throws SQLException {
        for (Patient p : Patients) {
            if (p.getName().equals(ChoicePatient.getValue())) {
                LblPatientID.setText(p.getID());
                meetingdata.setYValue(pmeetingDAO.getCount(LblPatientID.getText()));
                allergydata.setYValue(pallergyDAO.getCount(LblPatientID.getText()));
                mealdata.setYValue(pmealDAO.getCount(LblPatientID.getText()));
                medicinedata.setYValue(pmedDAO.getCount(LblPatientID.getText()));
            }
            if (ChoicePatient.getValue().equals("ALL")) {
                LblPatientID.setText("All Patients \n Is Showing");
                medicinedata.setYValue(medDAO.getCount());
                allergydata.setYValue(allDAO.getCount());
                meetingdata.setYValue(meetDAO.getCount());
                mealdata.setYValue(mealDAO.getCount());
            }
        }
    }


    @FXML
    void OnClickPatientPDF(ActionEvent event) throws IOException, SQLException, InterruptedException, DocumentException {
        if (ChoicePatient.getValue().equals("ALL")) {
            pdfExporter.Snapshotter(PatientBarChart.getLayoutX(), PatientBarChart.getLayoutY(), PatientBarChart.getWidth(), PatientBarChart.getHeight());
            ma.MessageWithoutHeader("Exported", "Patients Exported To PDF");
            pdfExporter.SavePatientPDF();
            //----------------------------------------PDF CREATE ↓↓↓↓↓------------------------------
        } else
            ma.MessageWithoutHeader("Barchart Not Screenshotted", "please choose all before exporting");

    }


    @FXML
    void OnClickRemovePatient(ActionEvent event) throws SQLException {
        String id = PatientTable.getSelectionModel().getSelectedItem().getID();
        int addressCode = PatientTable.getSelectionModel().getSelectedItem().getAddress().getAddresscode();
        PDH.removePatientByID(id, addressCode);
        PatientTable.getItems().removeAll(PatientTable.getSelectionModel().getSelectedItem());

        ma.MessageWithoutHeader("Removed", "Patient Removed Successfully :)");
    }


    @FXML
    void OnClickSpecToFile(ActionEvent event) {

        String id = "";
        try {
            id = PatientTable.getSelectionModel().getSelectedItem().getID();
        } catch (Exception e) {
            ma.MessageWithoutHeader("Unexpected Error", "Chose Specific Patient");
        }
        for (Model.Patient p : ALPATIENT)
            if (p.getID().equals(id)) {
                fh.SaveSpecificPatient(p);
                ma.MessageWithoutHeader("Exported", "Specific Patient Exported To PDF");
            }
    }


    @FXML
    void OnClickToFile(ActionEvent event) throws IOException, SQLException {

        fh.SavePatient();
        ma.MessageWithoutHeader("Exported", "Patients Exported To FILE");
    }


    //Overrided by implementing Initializable
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
             fh = new FilesHandler();
            TableInit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JavafxDiagramFill();
        JavafxChoiceFill();

        try {
            patient_meetingArrayList = pmeetingDAO.selectAll();
            patient_mealArrayList = pmealDAO.selectAll();
            patient_medicineArrayList = pmedDAO.selectAll();
            patient_allergyArrayList = pallergyDAO.selectAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CssStyler();

    }


    private void TableInit() throws SQLException {
        //Table Init
        ColID.setCellValueFactory(new PropertyValueFactory<Patient, String>("ID"));
        ColName.setCellValueFactory(new PropertyValueFactory<Patient, String>("Name"));
        ColGender.setCellValueFactory(new PropertyValueFactory<Patient, String>("Gender"));
        ColAddress.setCellValueFactory(new PropertyValueFactory<Patient, String>("Address"));
        ColBdate.setCellValueFactory(new PropertyValueFactory<Patient, Date>("date"));
        JavafxTableFill();
        PatientTable.setItems(Patients);

    }

    //Overrided by implementing JavafxPaneHandler
    @Override
    public void JavafxTableFill() throws SQLException {

        Patients = PDH.selectPatients();
        ALPATIENT = PDH.selectAll();

    }

    @Override
    public void JavafxChoiceFill() {
        list.removeAll();
        for (Patient p : Patients)
            list.add(p.getName());
        ChoicePatient.setValue("Choose Patient");
        ChoicePatient.getItems().add("ALL");
        ChoicePatient.getItems().addAll(list);
    }

    @Override
    public void JavafxDiagramFill() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis("Units", 0, 10, 1);
        NumberAxis asd = new NumberAxis();
        yAxis.setLabel("Count");

        medicinedata = new BarChart.Data(" Type", 0);
        allergydata = new BarChart.Data(" Type", 0);
        meetingdata = new BarChart.Data(" Type", 0);
        mealdata = new BarChart.Data(" Type", 0);

        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
                new BarChart.Series("Medicines", FXCollections.observableArrayList(
                        medicinedata
                )),

                new BarChart.Series("Allergies", FXCollections.observableArrayList(
                        allergydata
                )),
                new BarChart.Series("Meetings", FXCollections.observableArrayList(
                        meetingdata
                )),
                new BarChart.Series("Meals", FXCollections.observableArrayList(
                        mealdata
                ))
        );
        BarChart chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
        PatientBarChart.getData().addAll(chart.getData());


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
}
