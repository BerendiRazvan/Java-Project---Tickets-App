package festival.client;


import festival.model.Employee;
import festival.services.FestivalException;
import festival.services.IFestivalServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class LoginController {

    private IFestivalServices server;
    private HomeController festivalController;
    private Parent mainFestivalParent;

    @FXML
    Label errorsLabel;

    @FXML
    TextField emailTextField;

    @FXML
    PasswordField passwordTextField;

    public void setServer(IFestivalServices server){
        this.server = server;
    }

    public void setFestivalController(HomeController festivalController){
        this.festivalController = festivalController;
    }

    public void setMainFestivalParent(Parent mainFestivalParent){
        this.mainFestivalParent = mainFestivalParent;
    }


    @FXML
    public void initialize() {

        clearFields();

        emailTextField.textProperty().addListener(o -> errorsLabel.setText(""));
        passwordTextField.textProperty().addListener(o -> errorsLabel.setText(""));
    }

    private void clearFields(){
        emailTextField.setText("");
        passwordTextField.setText("");

        clearErrors();
    }

    private void clearErrors(){
        errorsLabel.setText("");
    }

    @FXML
    protected void onActionLogInBtn(ActionEvent actionEvent) {
        Boolean ok = true;

        if (passwordTextField.getText().matches("")) {
            errorsLabel.setText("Please entre a password!");
            ok = false;
        }

        if (emailTextField.getText().matches("")) {
            errorsLabel.setText("Please entre an email!");
            ok = false;
        }

        if (ok == true) {
            try {
                Employee employee = new Employee("", "", emailTextField.getText(), passwordTextField.getText());
                server.login(employee, festivalController);

                clearFields();


                System.out.println("After the controller was loaded by FXMLLoader, I can load the data");
                festivalController.setServer(server);
                Stage stage = new Stage();
                stage.setScene(new Scene(mainFestivalParent));

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        try {
                            server.logout(employee, festivalController);
                        } catch (FestivalException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    }
                });

                stage.setTitle("MPP App - Logged in as: " + employee.getMail());
                stage.show();


                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

            }catch (Exception e) {
                errorsLabel.setText(e.getMessage());
            }

        }
    }
}
