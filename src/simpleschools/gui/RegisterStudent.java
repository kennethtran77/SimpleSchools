package simpleschools.gui;

import java.util.Random;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import simpleschools.SimpleSchools;
import simpleschools.objects.Grade;
import simpleschools.objects.Homeform;
import simpleschools.objects.Student;

/**
 *
 * @author Kenneth Tran
 */
public class RegisterStudent extends Page {
    
    public RegisterStudent(SimpleSchools main) {
        super(main);

        // Initialize root component
        this.component = new GridPane();
        GridPane root = (GridPane) component;
        
        root.setHgap(25);
        root.setVgap(25);

        root.setMaxSize(650, 750);
        root.setMinSize(650, 750);
        
        // Component to hold the page information
        GridPane pane = new GridPane();
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        pane.setMaxSize(600, 610);
        pane.setMinSize(600, 610);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));

        ColumnConstraints constraint = new ColumnConstraints();
        constraint.setPercentWidth(50);
        pane.getColumnConstraints().add(constraint);
        
        // Page title
        Label lblTitle = new Label("Register Student");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        
        root.add(lblTitle, 0, 0);
        root.add(pane, 0, 1);
        
        // First Name Text Field
        Label lblFirstName = new Label("Student First Name*");
        lblFirstName.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextField txtFirstName = new TextField();
        txtFirstName.setMinSize(500, 35);
        txtFirstName.setMaxSize(500, 35);
        txtFirstName.setFont(Font.font("Arial", 18));
        
        pane.add(lblFirstName, 0, 0);
        pane.add(txtFirstName, 0, 1);
        
        // Middle Name Text Field
        Label lblMiddleName = new Label("Student Middle Name");
        lblMiddleName.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextField txtMiddleName = new TextField();
        txtMiddleName.setMinSize(500, 35);
        txtMiddleName.setMaxSize(500, 35);
        txtMiddleName.setFont(Font.font("Arial", 18));
        
        pane.add(lblMiddleName, 0, 2);
        pane.add(txtMiddleName, 0, 3);
        
        // Last Name Text Field
        Label lblLastName = new Label("Student Last Name*");
        lblLastName.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextField txtLastName = new TextField();
        txtLastName.setMinSize(500, 35);
        txtLastName.setMaxSize(500, 35);
        txtLastName.setFont(Font.font("Arial", 18));
        
        pane.add(lblLastName, 0, 4);
        pane.add(txtLastName, 0, 5);
        
        // Email Text Field
        Label lblEmail = new Label("Student Email*");
        lblEmail.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextField txtEmail = new TextField();
        txtEmail.setMinSize(500, 35);
        txtEmail.setMaxSize(500, 35);
        txtEmail.setFont(Font.font("Arial", 18));
        
        pane.add(lblEmail, 0, 6);
        pane.add(txtEmail, 0, 7);
        
        // Phone Number Text Field
        Label lblPhoneNumber = new Label("Student Phone Number");
        lblPhoneNumber.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextField txtPhoneNumber = new TextField();
        txtPhoneNumber.setMinSize(500, 35);
        txtPhoneNumber.setMaxSize(500, 35);
        txtPhoneNumber.setFont(Font.font("Arial", 18));
        
        pane.add(lblPhoneNumber, 0, 8);
        pane.add(txtPhoneNumber, 0, 9);
        
        // Grade Dropbox
        Label lblGrade = new Label("Student Grade*");
        lblGrade.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        ComboBox<Grade> cbGrade = new ComboBox<>();
        cbGrade.setDisable(true);
        cbGrade.setMinSize(200, 35);
        cbGrade.setMaxSize(200, 35);
        
        pane.add(lblGrade, 0, 10);
        pane.add(cbGrade, 0, 11);
        
        // Homeform Dropbox
        Label lblHomeform = new Label("Student Homeform*");
        lblHomeform.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        ComboBox<Homeform> cbHomeform = new ComboBox<>();
        cbHomeform.setMinSize(200, 35);
        cbHomeform.setMaxSize(200, 35);
        
        for (Homeform homeform : main.homeforms) {
            cbHomeform.getItems().add(homeform);
        }

        // Update "Grade Combobox" when Homeform is changed
        cbHomeform.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            cbGrade.setValue(newValue.getGrade());
        });

        pane.add(lblHomeform, 1, 10);
        pane.add(cbHomeform, 1, 11);
        
        // Warning label
        Label lblWarning = new Label("*This is a required field");
        lblWarning.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblWarning.setPadding(new Insets(20, 0, 0, 0));
        
        pane.add(lblWarning, 0, 12);
        
        // Register button
        Button btnRegister = new Button("Register");
        btnRegister.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnRegister.setMinSize(150, 50);
        btnRegister.setMaxSize(150, 50);
        
        pane.add(btnRegister, 0, 13);

        // EditStudentOutput label
        Label lblOutput = new Label();
        lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblOutput.setTextFill(Color.RED);
        lblOutput.setPadding(new Insets(30, 0, 0, 0));
        lblOutput.setVisible(false);
        
        pane.add(lblOutput, 0, 14, 2, 1);
        
        // Register Button Event Handler
        btnRegister.setOnAction(e -> {
            // Check for empty text fields
            if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || txtEmail.getText().isEmpty() || cbHomeform.getValue() == null) {      
                lblOutput.setTextFill(Color.RED);
                lblOutput.setText("You did not fill in all the required fields.");
                main.showVanish(lblOutput, 2);
                return;
            }
            
            Homeform homeform = cbHomeform.getValue();
            
            Random random = new Random();
            
            // Generate a random ID
            int id = random.nextInt(900000) + 100000;
            
            // Prevent duplicates
            while (main.ids.contains(id))
                id = random.nextInt(900000) + 100000;
            
            // Store ID for future reference
            main.ids.add(id);
            
            // Create new student object and add it to the data structure
            Student student = new Student(txtFirstName.getText().trim(), txtMiddleName.getText().trim(), txtLastName.getText().trim(), txtEmail.getText().trim(), txtPhoneNumber.getText().trim(), homeform, id);
            homeform.getClassroom().addStudent(student);
            main.students.add(student);
            
            lblOutput.setTextFill(Color.GREEN);
            lblOutput.setText("Successfully registered Student \"" + txtFirstName.getText().trim() + " " + txtLastName.getText().trim() + "\".");
            main.showVanish(lblOutput, 2);
        });
    }
    
}
