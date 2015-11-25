/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiaanfx;

import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoCursorNotFoundException;
import com.mongodb.util.JSON;
import java.awt.event.ActionEvent;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import utils.persianCalendar;

import static java.util.Arrays.asList;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import org.bson.Document;
/**
 *
 * @author mostafa
 */

public class Kiaanfx extends Application {
    
    private TableView<Person> table = new TableView<>();
//    private static final ObservableList<buys> buy_data= FXCollections.observableArrayList(
//            new buys("1", "1394/08/27", "2", "ali", "gh", "125000")
//    );
    private static final ObservableList<Person> data = FXCollections.observableArrayList(
            new Person("1", "1394/10/15", "1", "مصطفی", "قاسمی"));
    public static void main(String[] args) {
        launch(args);
    }
    
    private static void agg(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase db = mongoClient.getDatabase("kiaan" );
        AggregateIterable<Document> iterable = db.getCollection("banks").aggregate(asList(
                new Document("$unwind", "$branches")));
        
        
        
        iterable.forEach(new Block<Document>() {
         @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
            }
        });
    }
    
    
    
    private static void getBuy(){
        try{
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB db = mongoClient.getDB( "kiaan" );
            DBCollection coll = db.getCollection("buy");
            //aggregate
            DBObject unwind = new BasicDBObject("$unwind","$items");
            //$group            
            DBObject group_id = new BasicDBObject("_id", "$_id");
            group_id.put("num", "$num");
            group_id.put("person_id", "$person_id");
            group_id.put("discount", "$discount");
            group_id.put("increase", "$increase");
            //$group -> $multiply
            BasicDBList args  = new BasicDBList();
            args.add("$items.value");
            args.add("$items.price");
            DBObject multiply = new BasicDBObject("$multiply", args);
            //$group -> $sum
//            DBObject group_sum = new BasicDBObject("$sum", multiply);
            DBObject group_field = new BasicDBObject();
            group_field.put("_id", group_id);
            group_field.put("total", new BasicDBObject("$sum", multiply));
            DBObject group = new BasicDBObject("$group",group_field);
            //$project
            DBObject project_field = new BasicDBObject("_id","$_id._id");
            project_field.put("person_id", "$_id.person_id");
            project_field.put("num", "$_id.num");
            BasicDBList arr = new BasicDBList();
            arr.add("$total");arr.add("$_id.discount");arr.add("$_id.increase");
            DBObject field_add = new BasicDBObject("$add",arr);
            project_field.put("sum", field_add);
            DBObject project = new BasicDBObject("$project",project_field);
            DBObject sort = new BasicDBObject("$sort", new BasicDBObject("_id",1));
            List<DBObject> pipeline = Arrays.asList(unwind, group, project, sort);
            
//            AggregationOutput output = coll.aggregate(pipeline);
//            for (DBObject result : output.results()) {
//                System.out.println(result);
//            }
            
            AggregationOptions aggregationOptions = AggregationOptions.builder()
                .batchSize(100)
                .outputMode(AggregationOptions.OutputMode.CURSOR)
                .allowDiskUse(true)
                .build();
            
            BasicDBObject dbo = new BasicDBObject();            
            BasicDBList dbl = new BasicDBList();
            Cursor cursor = coll.aggregate(pipeline, aggregationOptions);
            //            
            DBCollection person_col = db.getCollection("persons");
            
//            BasicDBObject query = new BasicDBObject("items.personId",1);             
            BasicDBObject fields = new BasicDBObject("items.$",1)                    
                    .append("_id",false);
            
//            BasicDBList l_per = (BasicDBList) person_col.findOne(query, fields).get("items");
//            BasicDBObject[] lightArr = l_per.toArray(new BasicDBObject[0]);            
//            System.out.println(lightArr[0].get("_id"));
//            System.out.println(lightArr[0].get("first_name"));  
            
            
        
            
            
//            BasicDBList result = new BasicDBList();
            while (cursor.hasNext()) {                 
                dbo = (BasicDBObject) cursor.next();
//                System.out.println(dbo.toString());  
                DBObject query = new BasicDBObject("items._id", (ObjectId) dbo.get("person_id"));
                BasicDBList lst_person = (BasicDBList) person_col.findOne(query, fields).get("items");
                BasicDBObject[] lightArr = lst_person.toArray(new BasicDBObject[0]);                            
//                System.out.println(lightArr[0].get("first_name"));
                
                Date date = ((ObjectId) lightArr[0].get("_id")).getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                persianCalendar persianCalendar = new persianCalendar(calendar);            
                
                dbo.put("date", persianCalendar.getNumericDateFormatWithTime());
                dbo.put("personId", lightArr[0].get("personId").toString());
                dbo.put("first_name", lightArr[0].get("first_name").toString());
                dbo.put("last_name", lightArr[0].get("last_name").toString());
                
                data.add(new Person(
                        dbo.get("num").toString(),
                        dbo.get("date").toString(), 
                        dbo.get("personId").toString(), 
                        dbo.get("first_name").toString(), 
                        dbo.get("last_name").toString()                        
                    )
                );
//                buy_data.add(new buys(dbo.get("num").toString(),
//                        dbo.get("date").toString(), 
//                        dbo.get("personId").toString(),
//                        dbo.get("first_name").toString(),
//                        dbo.get("last_name").toString(),
//                        dbo.get("sum").toString()
//                ));                                
                dbo.remove("person_id");
//                result.add(dbo);                
//                System.out.println(dbo.get("first_name"));                  
            }                                             
            System.out.println(dbo.toString());
            
                        
        } catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    
    public static void fill(){        
        try{
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB db = mongoClient.getDB( "test" );
            DBCollection coll = db.getCollection("table");
            DBCursor cursor = coll.find();
            DBObject obj;
            while(cursor.hasNext()) {                  
                obj = cursor.next();
//                data.add(new Person((String) obj.get("first_name"), 
//                        (String) obj.get("last_name"), (String) obj.get("email")));
                System.out.println(obj.get("_id"));
//                System.out.println(JSON.serialize(obj));
            }
        } catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    
    @Override
    public void start(Stage stage) throws UnknownHostException {        
        //
//        fill();
//        getBuy();
//        agg();
        //
        Scene scene = new Scene(new Group());
        scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        stage.setTitle("Table View Sample");
        //set Stage boundaries to visible bounds of the main screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        
//        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
//        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2); 
//        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 4); 
        
        MenuBar menuBar = new MenuBar();
 
        // --- Menu File
        Menu menuFile = new Menu("File");
 
        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
 
        // --- Menu View
        Menu menuView = new Menu("View");
 
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        
        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));
        
        Button btn = new Button();
        btn.setText("get cell value");
        btn.setOnAction((event) -> {
            // set table focus
            table.requestFocus();
            // select row
            table.getSelectionModel().select(1);
            // get cell's value(text)
            Person person = table.getSelectionModel().getSelectedItem();
            System.out.println(person.getFirstName());
            
            
            //clear selection
//            table.getSelectionModel().clearSelection();
        });
        
        
        table.setEditable(true);
         //
        TableColumn numCol = new TableColumn("ش فاکتور");
        numCol.setMinWidth(50);
        numCol.setCellValueFactory(
                new PropertyValueFactory<>("num"));
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
//        firstNameCol.setCellValueFactory(
//                new PropertyValueFactory<>("firstName"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
 
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(200);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
// 
        
// 
        table.setItems(data);
        table.getColumns().addAll(numCol, dateCol, personIdCol, firstNameCol, lastNameCol);
        //
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(menuBar, label, btn, table);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
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
