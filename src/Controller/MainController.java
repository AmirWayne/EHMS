package Controller;

import Util.FxmlLoader;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private AnchorPane AnchorMainPane;
    @FXML
    public BorderPane BorderMainPane;
    @FXML
    private Button BtnNursing;
    @FXML
    private Button BtnPatient;
    @FXML
    private Button BtnMeals;
    @FXML
    private Button BtnMed;
    @FXML
    private Button BtnReports;
    @FXML
    private Button BtnStaff;
    @FXML
    private Button BtnConn;
    @FXML
    private Button BtnSett;

    @FXML
    private ImageView LogoHome;



    TranslateTransition openNav;
    TranslateTransition closeNav;

    @FXML
    private VBox NavBox;
    @FXML
    private Button btest;
    @FXML
    private ImageView ImageSlide;

    @FXML
    void OnMouseClickedSlide(MouseEvent event) {
        if (NavBox.getTranslateX() != 0) {
            BorderMainPane.setLeft(NavBox);
            openNav.play();

        }
        else {
            closeNav.setToX(-(NavBox.getWidth())+50);
            closeNav.play();

        }

    }


    private void drawerAction() {
         openNav = new TranslateTransition(new Duration(350), NavBox);
        openNav.setToX(0);
         closeNav = new TranslateTransition(new Duration(350), NavBox);


    }


    @FXML
    void LogoHomeClicked(MouseEvent event) throws IOException {
        System.out.println("LOGOOOOOOOO Clicked");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("MainPane");
        BorderMainPane.setCenter(view);
    }


    @FXML
    void OnClickConn(ActionEvent event) {

        System.out.println("Workssssss");
    }

    @FXML
    void OnClickMeals(ActionEvent event) throws IOException {
        System.out.println("Meals Clicked");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("Meals");
        BorderMainPane.setCenter(view);
    }

    @FXML
    void OnClickMed(ActionEvent event) throws IOException {
        System.out.println("Medicine Clicked");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("MedicinePane");
        BorderMainPane.setCenter(view);

    }

    @FXML
    void OnClickNursing(ActionEvent event) throws IOException {
    System.out.println("Nursing Clicked");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("TherapistPane");
        BorderMainPane.setCenter(view);
    }

    @FXML
    void OnClickPatient(ActionEvent event) throws IOException {
        System.out.println("Patient Clicked");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("PatientManagementPane");
        BorderMainPane.setCenter(view);
    }

    @FXML
    void OnClickReports(ActionEvent event) throws IOException {
        System.out.println("Reports Clicked");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("Reports");
        BorderMainPane.setCenter(view);
    }

    @FXML
    void OnClickSettings(ActionEvent event) {

    }

    @FXML
    void OnClickStaff(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawerAction();
    }

   public void RemovePane(Pane pane){

        BorderMainPane.setCenter(pane);

    }
}
