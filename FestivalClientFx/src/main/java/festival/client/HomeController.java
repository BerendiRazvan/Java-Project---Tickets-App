package festival.client;


import festival.model.Location;
import festival.model.Show;
import festival.model.Ticket;
import festival.services.FestivalException;
import festival.services.IFestivalObserver;
import festival.services.IFestivalServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.time.LocalDate;
import java.time.LocalDateTime;

public class HomeController implements IFestivalObserver {

    private IFestivalServices server;


    ObservableList<String> modelArtistsList = FXCollections.observableArrayList();
    ObservableList<Show> modelShowsList = FXCollections.observableArrayList();

    @FXML
    Label errorsLabel;

    @FXML
    TextField nameTextField;

    @FXML
    TextField ticketsTextField;

    @FXML
    ListView<String> artistsList;

    @FXML
    TableView<Show> showsTableView;

    @FXML
    public TableColumn<Show, Location> tableColumnLocation;
    @FXML
    public TableColumn<Show, LocalDateTime> tableColumnDateTime;
    @FXML
    private TableColumn<Show, Integer> tableColumnTicketsAvailable;
    @FXML
    private TableColumn<Show, Integer> tableColumnTicketsSold;

    @FXML
    Label selectedShow;

    @FXML
    DatePicker selectShowDay;


    public HomeController() {
        System.out.println("Constructor TravelController");
    }

    public HomeController(IFestivalServices server) {
        this.server = server;
        System.out.println("constructor TravelController cu server param");
        initData();
    }

    public void setServer(IFestivalServices server) {
        this.server = server;
        initData();
    }

    private void initData() {
        try {
            modelArtistsList.setAll(server.getAllArtists());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (!artistsList.getItems().isEmpty())
            artistsList.getSelectionModel().select(0);
    }


    @FXML
    public void initialize() {
        artistsList.setItems(modelArtistsList);
        artistsList.getSelectionModel().selectedItemProperty().addListener(a -> displayShows());
        showsTableView.getSelectionModel().selectedItemProperty().addListener(s -> setTextSelectedShow());
        showsTableView.setRowFactory(r -> new TableRow<Show>() {
            @Override
            public void updateItem(Show item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getTicketsAvailable() == 0) {
                    setStyle("-fx-background-color: #ff7959;");
                } else {
                    setStyle("");
                }
            }
        });

        clearFields();

        nameTextField.textProperty().addListener(o -> errorsLabel.setText(""));
        ticketsTextField.textProperty().addListener(o -> errorsLabel.setText(""));

        tableColumnLocation.setCellValueFactory(new PropertyValueFactory<Show, Location>("location"));
        tableColumnDateTime.setCellValueFactory(new PropertyValueFactory<Show, LocalDateTime>("dataAndTime"));
        tableColumnTicketsAvailable.setCellValueFactory(new PropertyValueFactory<Show, Integer>("ticketsAvailable"));
        tableColumnTicketsSold.setCellValueFactory(new PropertyValueFactory<Show, Integer>("ticketsSold"));
        showsTableView.setItems(modelShowsList);

        selectShowDay.valueProperty().addListener(d -> filterTableData());


    }

    private void setTextSelectedShow() {
        Show show = showsTableView.getSelectionModel().getSelectedItem();
        if (show != null)
            selectedShow.setText(show.toString());
    }

    private void clearFields() {
        nameTextField.setText("");
        ticketsTextField.setText("");
        Show show = showsTableView.getSelectionModel().getSelectedItem();
        if (show != null)
            selectedShow.setText(show.toString());
        else
            selectedShow.setText("Select a show");

        clearErrors();
    }

    private void clearErrors() {
        errorsLabel.setText("");
    }

    private void filterTableData() {
        if (selectShowDay.getValue() != null) {
            String artistName = artistsList.getSelectionModel().getSelectedItem();
            LocalDate date = selectShowDay.getValue();
            LocalDateTime dateTime = date.atStartOfDay();
            try {
                modelShowsList.setAll(server.getArtistShowsInADay(artistName, dateTime));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    protected void onActionBuyTicketsBtn(ActionEvent event) {
        Boolean ok = true;

        if (ticketsTextField.getText().matches("")) {
            errorsLabel.setText("Please entre a valid number of tickets!");
            ok = false;
        }

        int noOfTickets = 0;
        try {
            noOfTickets = Integer.parseInt(ticketsTextField.getText());
        } catch (NumberFormatException e) {
            errorsLabel.setText("Please entre a valid number of tickets!");
            ok = false;
        }

        if (noOfTickets < 1) {
            errorsLabel.setText("Please entre a valid number of tickets!");
            ok = false;
        }

        if (nameTextField.getText().matches("")) {
            errorsLabel.setText("Please entre a name!");
            ok = false;
        }

        if (selectedShow.getText().equals("Select a show")) {
            errorsLabel.setText("Please select a show!");
            ok = false;
        }

        if (ok) {
            try {
                Show show = showsTableView.getSelectionModel().getSelectedItem();
                if (show != null)
                    server.sellTicket(nameTextField.getText(), noOfTickets, show);

                displayShows();
                clearFields();
            } catch (Exception e) {
                errorsLabel.setText(e.getMessage());
            }
        }
    }


    private void displayShows() {

        selectedShow.setText("Select a show");

        selectShowDay.setValue(null);
        String artistName = artistsList.getSelectionModel().getSelectedItem();
        try {
            modelShowsList.setAll(server.getArtistShows(artistName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void soldTicket(Ticket ticket) throws FestivalException {
        Platform.runLater(()->{
            if (selectShowDay.getValue() != null) {
                String artistName = artistsList.getSelectionModel().getSelectedItem();
                LocalDate date = selectShowDay.getValue();
                LocalDateTime dateTime = date.atStartOfDay();
                try {
                    modelShowsList.setAll(server.getArtistShowsInADay(artistName, dateTime));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            else {
                String artistName = artistsList.getSelectionModel().getSelectedItem();
                try {
                    modelShowsList.setAll(server.getArtistShows(artistName));
                } catch (FestivalException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

    }


}
