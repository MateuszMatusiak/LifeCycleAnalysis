package Main;

import javafx.fxml.Initializable;

public class Controller implements Initializable {

    @FXML
    private Button lifeTable;
    @FXML
    private Button lognormal;
    @FXML
    private TextField dataFile;
    @FXML
    private TextField numberOfDays;
    @FXML
    private TextField interval;
    @FXML
    private Label errorLabel;

    FileReader f = new FileReader();

    public static ArrayList<Data> data;
    public static int publicNumberOfDays;
    public static int publicInterval;

    public void makeLognormal() {
        String filename = dataFile.getText();
        try {
            data = f.readFile(filename);
            hideErrorLabel();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Lognormal.class.getResource("lognormal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Lognormal");
            stage.setScene(scene);
            stage.show();
        } catch (FileNotFoundException e) {
            setErrorLabel("Couldn't open file");
        } catch (IOException e) {
            System.out.println("Something went wrong");
            setErrorLabel("Something went wrong with new window, try again");
            e.printStackTrace();
        } catch (Exception e) {
            setErrorLabel("Bad parameters");
        }
    }

    public void makeLifeTable() {
        String filename = dataFile.getText();
        try {
            data = f.readFile(filename);
            hideErrorLabel();
            publicNumberOfDays = Integer.parseInt(numberOfDays.getText());
            publicInterval = Integer.parseInt(interval.getText());
            if (publicNumberOfDays <= 0 || publicInterval <= 0)
                throw new InputMismatchException();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Lognormal.class.getResource("lifetable.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("LifeTable");
            stage.setScene(scene);
            stage.show();
        } catch (FileNotFoundException e) {
            setErrorLabel("Couldn't open file");
        } catch (IOException e) {
            System.out.println("Something went wrong");
            setErrorLabel("Something went wrong with new window, try again");
            e.printStackTrace();
        } catch (Exception e) {
            setErrorLabel("Bad parameters");
            e.printStackTrace();
        }
    }

    public void setErrorLabel(String message) {
        errorLabel.setText(message);
    }

    public void hideErrorLabel() {
        errorLabel.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lognormal.setOnMouseClicked(event -> makeLognormal());
        lifeTable.setOnMouseClicked(event -> makeLifeTable());
    }
}