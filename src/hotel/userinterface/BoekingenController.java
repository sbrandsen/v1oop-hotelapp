package hotel.userinterface;

import hotel.model.Hotel;
import hotel.model.KamerType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.Objects;

public class BoekingenController {

    @FXML private TextField TB_Naam;
    @FXML private TextField TB_Adres;
    @FXML private DatePicker DP_Aankomstdatum;
    @FXML private DatePicker DP_Vertrekdatum;
    @FXML private ComboBox Combo_Kamertype;

    private Hotel hotel;

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        ObservableList<String> oListKamerType = FXCollections.observableArrayList(hotel.getTypeNamen());

        Combo_Kamertype.setItems(oListKamerType);
        CB_Reset(null);
    }

    public void CB_Reset(ActionEvent actionEvent) {
        TB_Naam.setText("");
        TB_Adres.setText("");

        DP_Aankomstdatum.setValue(java.time.LocalDate.now());
        DP_Vertrekdatum.setValue(java.time.LocalDate.now().plusDays(1));

        Combo_Kamertype.getSelectionModel().select(0);
    }

    public void CB_Boek(ActionEvent actionEvent) throws Exception {
//        de naam is ingevuld
//        het adres is ingevuld
//        de aankomst- en vertrekdatum niet in het verleden liggen
//        de aankomstdatum voor de vertrekdatum ligt
//        er een kamertype is geselecteerd

        if(!PassedChecks()){
            return;
        }

        String kt_naam = (String) Combo_Kamertype.getValue();
        KamerType kt = null;
        for(KamerType kti : hotel.getKamerTypen() ){
            if(Objects.equals(kti.getTypeNaam(), kt_naam)){
                kt = kti;
            }
        }

        try{
            hotel.voegBoekingToe(DP_Aankomstdatum.getValue(), DP_Vertrekdatum.getValue(), TB_Naam.getText(), TB_Adres.getText(), kt);
            ShowAlert("Boeking toegevoegd!", "Succes!");
            CB_Reset(null);
        } catch (Exception e) {
            ShowAlert("Kon boeking niet toevoegen door foutieve data.");
        }

    }

    private boolean PassedChecks(){
        if(Objects.equals(TB_Naam.getText(), "")){
            ShowAlert("Naam kan niet leeg zijn");
            return false;
        }
        if(Objects.equals(TB_Adres.getText(), "")){
            ShowAlert("Adres kan niet leeg zijn");
            return false;
        }
        if(Objects.equals(Combo_Kamertype.getValue(), "")){
            ShowAlert("Kamertype kan niet leeg zijn");
            return false;
        }

        if(Objects.equals(DP_Aankomstdatum.getValue().toString(), "")){
            ShowAlert("Aankomstdatum kan niet leeg zijn");
            return false;
        } else {
            LocalDate date = DP_Aankomstdatum.getValue();
            if(date.isAfter(java.time.LocalDate.now())){
                ShowAlert("Aankomstdatum kan niet na vandaag zijn");
                return false;
            }
        }

        if(Objects.equals(DP_Vertrekdatum.getValue().toString(), "")){
            ShowAlert("Vertrekdatum kan niet leeg zijn");
            return false;
        } else {
            LocalDate date = DP_Vertrekdatum.getValue();
            if(date.isBefore(java.time.LocalDate.now())){
                ShowAlert("Vertrekdatum kan niet voor vandaag zijn");
                return false;
            }
        }

        return true;
    }

    private void ShowAlert(String box){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Verkeerde informatie");
        alert.setHeaderText("Verkeerde informatie");
        alert.setContentText(box);

        alert.showAndWait();
    }

    private void ShowAlert(String box, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(box);

        alert.showAndWait();
    }
}
