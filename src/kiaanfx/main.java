package kiaanfx;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import myControls.mainMenu;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import myControls.mainTableview;
import myControls.myBorderPane;

/**
 * Sample application that shows examples of the different layout panes
 * provided by the JavaFX layout API.
 * The resulting UI is for demonstration purposes only and is not interactive.
 */
public class main extends Application {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    @Override
    public void start(Stage stage) {

        mainMenu mainMenu = new mainMenu();
        mainTableview maintbl = new mainTableview();
        
        myBorderPane bdrMain = new myBorderPane();
        BorderPane bdrTop = new BorderPane();
        //
        bdrTop.setRight(addKiaan());
        bdrTop.setCenter(addCompany());
        bdrTop.setLeft(addKiaan());
        bdrTop.setBottom(addTime());
        bdrMain.setTop(bdrTop);        
        bdrMain.setCenter(tblperson());
        
        //
        Scene scene = new Scene(bdrMain);        
        scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        stage.setScene(scene);
        stage.setTitle("نرم افزار فروشگاهی کیان");
        //        
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            switch (key.getText()){
                case "ط" :
                    System.out.println("تایپ شد حرف  : " + key.getText());
                    break;
                default:                    
                    
            }
//            if(key.getCode()== KeyCode.TAB) {
//                System.out.println("You pressed Tab");
//            } else if (key.getCode() == KeyCode.A && key.isControlDown()) {
//                System.out.println("Ctrl+A");
//            } else if (key.getCode() == KeyCode.LEFT) {
//                System.out.println("left");
//            } else {
//                
//                System.out.println(key.getText());
//            }
        });
        
        //set Stage boundaries to visible bounds of the main screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.show();
    }
    
    
    private BorderPane addTime() {

        BorderPane bdrTitle = new BorderPane();        
        bdrTitle.setStyle("-fx-background-color: #778899;");

        Label lblTime = new Label("12 : 00");
        bdrTitle.setRight(lblTime);
        
        return bdrTitle;
    }
    
    private HBox addHbox() {

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #336699;");        
        
//        hbox.getChildren().add(addVBox());
        
        return hbox;
    }
        
    private VBox addCompany() {
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes

        Image image = new Image("graphics/company.png");
        ImageView iv1 = new ImageView();        
        iv1.setImage(image);

        Text title = new Text("صنایع دستی قاسمی");
        title.setFont(Font.font("B Yekan", FontWeight.BOLD, 14));
        
        vbox.getChildren().addAll(iv1, title);
        
        return vbox;
    }
    
    private VBox addKiaan() {
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes

        Image image = new Image("graphics/kiaan.png");
        ImageView iv1 = new ImageView();        
        iv1.setImage(image);

        Text title = new Text("نرم افزار کیان(نسخه 1.0.0)");
        title.setFont(Font.font("B Mitra", FontWeight.BOLD, 14));
        
        vbox.getChildren().addAll(iv1, title);
        
        return vbox;
    }
    
    private mainTableview tblperson(){
             
        mainTableview tbl = new mainTableview();
        ObservableList data;
        List list = new ArrayList();
        list.add(new Person("1", "1394/10/15", "1", "مصطفی", "قاسمی"));
        data = FXCollections.observableList(list);
        tbl.setItems(data);
        
        TableColumn numCol = new TableColumn("ش فاکتور");
        numCol.setMinWidth(50);
        numCol.setCellValueFactory(new PropertyValueFactory("num"));
        //
        TableColumn dateCol = new TableColumn("تاریخ ثبت");
        dateCol.setMinWidth(150);
        dateCol.setCellValueFactory(new PropertyValueFactory("date"));
        //
        TableColumn personIdCol = new TableColumn("کد شخص");
        personIdCol.setMinWidth(50);
        personIdCol.setCellValueFactory(new PropertyValueFactory("personId"));
        //
        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(150);        
        firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
        //
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(200);
        lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
        
        tbl.getColumns().addAll(numCol,dateCol,personIdCol,firstNameCol,lastNameCol);
        
        return tbl;
    }
    
    public static class Person {
        
        private final SimpleStringProperty num;
        private final SimpleStringProperty date;
        private final SimpleStringProperty personId;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
 
        private Person(String num, String date, String personId, String fName, String lName) {
            this.num = new SimpleStringProperty(num);
            this.date = new SimpleStringProperty(date);
            this.personId = new SimpleStringProperty(personId);
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
        }
 
        public String getNum() {
            return num.get();
        }
 
        public void setNum(String _num) {
            num.set(_num);
        }
        
        public String getDate() {
            return date.get();
        }
 
        public void setDate(String _date) {
            date.set(_date);
        }
        
        public String getPersonId() {
            return personId.get();
        }
 
        public void setPersonId(String _personId) {
            personId.set(_personId);
        }
        
        public String getFirstName() {
            return firstName.get();
        }
 
        public void setFirstName(String fName) {
            firstName.set(fName);
        }
 
        public String getLastName() {
            return lastName.get();
        }
 
        public void setLastName(String fName) {
            lastName.set(fName);
        }
 
    }
    
}