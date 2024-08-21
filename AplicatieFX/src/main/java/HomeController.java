//import Domeniu.Client;
//import Domeniu.Cursa;
//import Domeniu.Rezervare;
//import Service.AppException;
//import Service.IService;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
//import javafx.application.Platform;
//
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class HomeController
//{
//    private IService service;
//
//    @FXML
//    private TabPane tabPane;
//
//    @FXML
//    private TableView<Cursa> tableCurse;
//
//    @FXML
//    private TextField textFieldDestinatie;
//
//    @FXML
//    private TextField textFieldData;
//
//    @FXML
//    private TextField textFieldOra;
//
//    @FXML
//    private ListView<Cursa> listViewCurseDupaDestinatie;
//
//    @FXML
//    private TextField textFieldNumeClient;
//
//    @FXML
//    private TextField textFieldNumarLocuri;
//
//    @FXML
//    private TableView<Rezervare> tableRezervare;
//
//    @FXML
//    Button cautareClienti;
//
//    @FXML
//    Button rezervare;
//
//    @FXML
//    Button logout;
//
//    @FXML
//    Button cautareCursaDupaDestinatie;
//
//    @FXML
//    Button cautareCursaDupaToate;
//
//
//    public HomeController(IService service) {
//        this.service = service;
//    }
//
//    @FXML
//    public void initialize() {
//        try {
//            loadCurse();
//            loadRezervari();
//        } catch (AppException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void loadCurse() throws AppException
//    {
//        Platform.runLater(() -> {
//            List<Cursa> curse = null;
//            try {
//
//                curse = (List<Cursa>) service.getAllCurse();
//            } catch (AppException e) {
//                throw new RuntimeException(e);
//            }
//            tableCurse.getItems().addAll(curse);
//        });
//    }
//
//    private void loadRezervari() throws AppException {
//        Platform.runLater(() -> {
//            List<Rezervare> rezervari = null;
//            try {
//                rezervari = (List<Rezervare>) service.getAllRezervari();
//            } catch (AppException e) {
//                throw new RuntimeException(e);
//            }
//            tableRezervare.getItems().addAll(rezervari);
//        });
//    }
//
//    @FXML
//    private void handleSearchButtonAction() {
//        try {
//            String destinatie = textFieldDestinatie.getText();
//            String data = textFieldData.getText();
//            String ora = textFieldOra.getText();
//        }
//        catch (Exception e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Eroare");
//            alert.setHeaderText("Eroare la cautare curse");
//            alert.setContentText("Datele introduse sunt gresite!");
//            alert.showAndWait();
//        }
//    }
//
//    @FXML
//    private void handleReservationButtonAction() {
//        try {
//            Cursa cursa = listViewCurseDupaDestinatie.getSelectionModel().getSelectedItem();
//            Client client = service.getByName(textFieldNumeClient.getText());
//            Rezervare rezervare = new Rezervare(client, cursa, Integer.parseInt(textFieldNumarLocuri.getText()));
//            service.addRezervare(rezervare);
//        } catch (Exception e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Eroare");
//            alert.setHeaderText("Eroare la adaugare rezervare");
//            alert.setContentText("Datele introduse sunt gresite!");
//            alert.showAndWait();
//        }
//    }
//
//    @FXML
//    private void handleCautareCurseDupaDestinatie()
//        {
//            try {
//                String destinatie = textFieldDestinatie.getText();
//                List<Cursa> curse = (List<Cursa>) service.getAllCurse();
//                List<Cursa> curseDupaDestinatie = curse.stream().filter(cursa -> cursa.getDestinatie().equals(destinatie)).collect(Collectors.toList());
//                listViewCurseDupaDestinatie.getItems().addAll(curseDupaDestinatie);
//            } catch (Exception e) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Eroare");
//                alert.setHeaderText("Eroare la cautare curse");
//                alert.setContentText("Datele introduse sunt gresite!");
//                alert.showAndWait();
//            }
//
//
//        }
//
//    @FXML
//    private void handleLogoutButtonAction() {
//        try {
//            //logout
//        } catch (Exception e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Eroare");
//            alert.setHeaderText("Eroare la delogare");
//            alert.setContentText("Datele introduse sunt gresite!");
//            alert.showAndWait();
//        }
//    }
//
//    @FXML
//    private void handleUpdateButtonAction() {
//        try {
//
//        } catch (Exception e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Eroare");
//            alert.setHeaderText("Eroare la actualizare");
//            alert.setContentText("Datele introduse sunt gresite!");
//            alert.showAndWait();
//        }
//    }
//}
