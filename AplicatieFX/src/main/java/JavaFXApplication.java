import Domeniu.Client;
import Domeniu.Cursa;
import Domeniu.Rezervare;
import Domeniu.Utilizator;
import Service.AppException;
import Service.IObserver;
import Service.IService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.Collection;
import java.util.List;

public class JavaFXApplication implements IObserver
{
    private IService service;

    private TableView<Cursa> tableCurse;
    private ObservableList<Cursa> observableListCurse = FXCollections.observableArrayList();

    private TableView<Rezervare> tableRezervare;
    private ObservableList<Rezervare> observableListRezervare = FXCollections.observableArrayList();

    private List<Cursa> curse;

    private List<Rezervare> rezervari;

    private ObservableList<Cursa> curseDupaDestinatie = FXCollections.observableArrayList();

    ListView<Cursa> listViewCurseDupaDestinatie = new ListView<>();
    TextField textFieldDestinatieCautare = new TextField();

    Utilizator utilizator;



    public JavaFXApplication(IService service, Utilizator utilizatorLogat)
    {
        this.service = service;
        utilizator= utilizatorLogat;
    }

    public void start(Stage stage) throws AppException
    {

        TabPane tabPane = new TabPane();

        Tab tabCurse = new Tab("Curse");

        Tab tabRezervare = new Tab("Rezervari");

        Tab tabCautare = new Tab("Cautare");

        tabCurse.setContent(new StackPane());
        tabRezervare.setContent(new StackPane());
        tabCautare.setContent(new StackPane());

        tabPane.getTabs().addAll(tabCurse, tabRezervare, tabCautare);

        Scene scene = new Scene(tabPane, 800, 600);
        // CURSE
        tableCurse = new TableView<>();
        TableColumn<Cursa, Integer> columnId = new TableColumn<>("Id");
        TableColumn<Cursa, String> columnDestinatie = new TableColumn<>("Destinatie");
        TableColumn<Cursa, String> columnData = new TableColumn<>("Data");
        TableColumn<Cursa, String> columnOra = new TableColumn<>("Ora");
        TableColumn<Cursa, String> columnLocuriDisponibile = new TableColumn<>("Locuri Disponibile");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDestinatie.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        columnData.setCellValueFactory(new PropertyValueFactory<>("data"));
        columnOra.setCellValueFactory(new PropertyValueFactory<>("ora"));
        columnLocuriDisponibile.setCellValueFactory(new PropertyValueFactory<>("locuriDisponibile"));

        tableCurse.getColumns().addAll(columnDestinatie, columnData, columnOra, columnLocuriDisponibile);
//TODO


        curse = (List<Cursa>) this.service.getAllCurse();
        this.observableListCurse = FXCollections.observableArrayList(curse);
        tableCurse.setItems(this.observableListCurse);

        tabCurse.setContent(tableCurse);

        // CAUTARE

        GridPane gridPaneCautare = new GridPane();
        Label labelDestinatie = new Label("Destinatie");
        Label labelData = new Label("Data");
        Label labelOra = new Label("Ora");
        TextField textFieldDestinatie = new TextField();
        TextField textFieldData = new TextField();
        TextField textFieldOra = new TextField();
        Button buttonCautare = new Button("Cautare");

        gridPaneCautare.add(labelDestinatie, 0, 0);
        gridPaneCautare.add(labelData, 0, 1);
        gridPaneCautare.add(labelOra, 0, 2);
        gridPaneCautare.add(textFieldDestinatie, 1, 0);
        gridPaneCautare.add(textFieldData, 1, 1);
        gridPaneCautare.add(textFieldOra, 1, 2);
        gridPaneCautare.add(buttonCautare, 1, 3);

        buttonCautare.setOnAction(event -> {
            List<String> curseCautate = null;
            try {
                curseCautate = this.service.cautareCurse(textFieldDestinatie.getText(), textFieldData.getText(), textFieldOra.getText());
            } catch (AppException e) {
                throw new RuntimeException(e);
            }

            ListView<String> listViewCurseCautate = new ListView<>();
            ObservableList<String> observableListCurseCautate = FXCollections.observableArrayList();
            for(int i=0;i<curseCautate.size();i++)
                observableListCurseCautate.add(i+1 + curseCautate.get(i));
            listViewCurseCautate.setItems(observableListCurseCautate);

            gridPaneCautare.add(listViewCurseCautate, 0, 5);
        });
        tabCautare.setContent(gridPaneCautare);


        //REZERVARE

        tableRezervare = new TableView<>();
        TableColumn<Rezervare, Integer> columnIdRezervare = new TableColumn<>("Id");
        TableColumn<Rezervare, String> columnNumeClient = new TableColumn<>("Nume Client");
        TableColumn<Rezervare, String> columnDestinatieRezervare = new TableColumn<>("Destinatie");
        TableColumn<Rezervare, Integer> columnNrLocuri = new TableColumn<>("Numar Locuri");

        columnIdRezervare.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNumeClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        columnDestinatieRezervare.setCellValueFactory(new PropertyValueFactory<>("cursa"));
        columnNrLocuri.setCellValueFactory(new PropertyValueFactory<>("nrLocuri"));

        tableRezervare.getColumns().addAll(columnNumeClient, columnDestinatieRezervare, columnNrLocuri);
//TODO
        rezervari = (List<Rezervare>) this.service.getAllRezervari();
        this.observableListRezervare = FXCollections.observableArrayList(rezervari);
        tableRezervare.setItems(this.observableListRezervare);


        //TODO logout
        Button buttonLogout = new Button("Logout");
        buttonLogout.setOnAction(event -> {
            try {
                this.service.logout(utilizator);
                stage.close();
            } catch (AppException e) {
                throw new RuntimeException(e);
            }
        });

        gridPaneCautare.add(buttonLogout, 1, 4);

        GridPane gridPaneCautareDupaDestinatie = new GridPane();
        Label labelDestinatieCautare = new Label("Destinatie");

        Button buttonCautareDupaDestinatie = new Button("Cautare");

        gridPaneCautareDupaDestinatie.add(labelDestinatieCautare, 0, 0);
        gridPaneCautareDupaDestinatie.add(textFieldDestinatieCautare, 1, 0);
        gridPaneCautareDupaDestinatie.add(buttonCautareDupaDestinatie, 1, 1);

        buttonCautareDupaDestinatie.setOnAction(event -> cautaCursaDupaDestinatie(textFieldDestinatieCautare.getText()));

        HBox hBoxCautareDupaDestinatie = new HBox();
        hBoxCautareDupaDestinatie.getChildren().addAll(gridPaneCautareDupaDestinatie, listViewCurseDupaDestinatie);

        VBox vBoxRezervare = new VBox();

        GridPane gridPaneRezervare = new GridPane();
        Label labelNumeClient = new Label("Nume Client");
        Label labelNumarLocuri = new Label("Numar Locuri");
        TextField textFieldNumeClient = new TextField();
        TextField textFieldNumarLocuri = new TextField();
        Button buttonRezervare = new Button("Rezervare");

        gridPaneRezervare.add(labelNumeClient, 0, 0);
        gridPaneRezervare.add(labelNumarLocuri, 0, 1);
        gridPaneRezervare.add(textFieldNumeClient, 1, 0);
        gridPaneRezervare.add(textFieldNumarLocuri, 1, 1);
        gridPaneRezervare.add(buttonRezervare, 1, 2);


        buttonRezervare.setOnAction(event ->
        {
            try {
                Cursa cursa = this.listViewCurseDupaDestinatie.getSelectionModel().getSelectedItem();
                Client client = service.getByName(textFieldNumeClient.getText());
                Rezervare rezervare = new Rezervare(client, cursa, Integer.parseInt(textFieldNumarLocuri.getText()));
                service.addRezervare(rezervare);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eroare");
                alert.setHeaderText("Eroare la adaugare rezervare");
                alert.setContentText("Datele introduse sunt gresite!");
                alert.showAndWait();
            }

        });




        vBoxRezervare.getChildren().addAll(hBoxCautareDupaDestinatie, gridPaneRezervare, tableRezervare);

        tabRezervare.setContent(vBoxRezervare);


        stage.setTitle("Administrare curse");
        stage.setScene(scene);
        stage.show();




    }

    private void cautaCursaDupaDestinatie(String text)
    {
        List<Cursa> curseDupaDestinatie = null;
        try {
            curseDupaDestinatie = service.cautareCurseDupaDestinatie(text);
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
        this.curseDupaDestinatie = FXCollections.observableArrayList(curseDupaDestinatie);
        this.listViewCurseDupaDestinatie.setItems(this.curseDupaDestinatie);
    }

    @Override
    public void updateCurse(Collection<Cursa> curse) throws AppException
    {
        System.out.println("Update received");
            Platform.runLater(() -> {
                this.observableListCurse = FXCollections.observableArrayList(curse);
                tableCurse.setItems(observableListCurse);
                this.cautaCursaDupaDestinatie(textFieldDestinatieCautare.getText());
            });
    }

    @Override
    public void updateRezervari(Collection<Rezervare> rezervari) throws AppException
    {
            Platform.runLater(() -> {
                this.observableListRezervare = FXCollections.observableArrayList(rezervari);
                tableRezervare.setItems(observableListRezervare);
            });
    }



}

