package hotel.userinterface;

import hotel.model.Boeking;
import hotel.model.Hotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class HotelOverzichtController {
    @FXML private Label hotelnaamLabel;
    @FXML private ListView boekingenListView;
    @FXML private DatePicker overzichtDatePicker;

    private Hotel hotel = Hotel.getHotel();

    public void initialize() {
        hotelnaamLabel.setText("Boekingen hotel " + hotel.getNaam());
        overzichtDatePicker.setValue(LocalDate.now());
        toonBoekingen();
    }

    public void toonVorigeDag(ActionEvent actionEvent) {
        LocalDate dagEerder = overzichtDatePicker.getValue().minusDays(1);
        overzichtDatePicker.setValue(dagEerder);
    }

    public void toonVolgendeDag(ActionEvent actionEvent) {
        LocalDate dagLater = overzichtDatePicker.getValue().plusDays(1);
        overzichtDatePicker.setValue(dagLater);
    }

    public void nieuweBoeking(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Boekingen.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            BoekingenController controller = fxmlLoader.<BoekingenController>getController();
            controller.setHotel(hotel);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void toonBoekingen() {
        hotel = Hotel.getHotel();
        ObservableList<String> boekingen = FXCollections.observableArrayList();

        List<Boeking> boekingenList = hotel.getBoekingen();

        LocalDate date = overzichtDatePicker.getValue();
        for(Boeking b : boekingenList){
            if(date.isAfter(b.getAankomstDatum()) && date.isBefore(b.getVertrekDatum())){
                boekingen.add(b.toString());
            }
        }

        // Vraag de boekingen op bij het Hotel-object.
        // Voeg voor elke boeking in nette tekst (string) toe aan de boekingen-lijst.

        boekingenListView.setItems(boekingen);
    }
}