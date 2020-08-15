package Controller;

import Util.JavafxPaneHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class PatientPaneController implements Initializable,Util.JavafxPaneHandler {

    @FXML
    private Button BtnPrint;

    @FXML
    private TableView<?> PatientTable;

    @FXML
    private ChoiceBox<String> ChoicePatient;
    ObservableList list = FXCollections.observableArrayList();

    @FXML
    private BarChart<?, ?> PatientBarChart;

    @FXML
    private Button BtnPatientFile;

    @FXML
    private Button BtnPatientXML;

    @FXML
    private Button BtnRemovePatient;

    @FXML
    private Button BtnLodPatient;

    @FXML
    private Label LblPatientID;


    @FXML
    void OnClickLoadPatient(ActionEvent event) {

    }

    @FXML
    void OnClickPatientXML(ActionEvent event) {

    }

    @FXML
    void OnClickPrint(ActionEvent event) {

    }

    @FXML
    void OnClickRemovePatient(ActionEvent event) {

    }

    @FXML
    void OnClickToFile(ActionEvent event) {
    System.out.println(1);
    }


    //Overrided by implementing Initializable
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JavafxChoiceFill();
    }


    //Overrided by implementing JavafxPaneHandler
    @Override
    public void JavafxTableFill() {

    }

    @Override
    public void JavafxChoiceFill() {
        list.removeAll();
        String a="Amir";
        String b="Alam";
        String c="Osnat";
        list.addAll(a,b);
        ChoicePatient.setValue("Choose Patient");
        ChoicePatient.getItems().addAll(list);
    }

    @Override
    public void JavafxDiagramFill() {

    }







}
