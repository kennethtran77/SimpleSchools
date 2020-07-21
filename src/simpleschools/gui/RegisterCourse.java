package simpleschools.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import simpleschools.SimpleSchools;
import simpleschools.objects.Grade;
import simpleschools.objects.Course;

/**
 *
 * @author Kenneth Tran
 */
public class RegisterCourse extends Page {
    
    public RegisterCourse(SimpleSchools main) {
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

        // Page title
        Label lblTitle = new Label("Register Course");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        
        root.add(lblTitle, 0, 0);
        root.add(pane, 0, 1);

        // Name Text Field
        Label lblName = new Label("Course Name*");
        lblName.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextField txtName = new TextField();
        txtName.setMinSize(500, 35);
        txtName.setMaxSize(500, 35);
        txtName.setFont(Font.font("Arial", 18));
        
        pane.add(lblName, 0, 0);
        pane.add(txtName, 0, 1);

        // Code Text Field
        Label lblCode = new Label("Course Code*");
        lblCode.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextField txtCode = new TextField();
        txtCode.setMinSize(500, 35);
        txtCode.setMaxSize(500, 35);
        txtCode.setFont(Font.font("Arial", 18));
        
        pane.add(lblCode, 0, 2);
        pane.add(txtCode, 0, 3);

        // Grade Combobox
        Label lblGrade = new Label("Course Grade*");
        lblGrade.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        ComboBox<Grade> cbGrade = new ComboBox<>();
        cbGrade.setMinSize(300, 35);
        cbGrade.setMaxSize(300, 35);
        
        for (Grade grade : main.grades) {
            cbGrade.getItems().add(grade);
        }
        
        pane.add(lblGrade, 0, 4);
        pane.add(cbGrade, 0, 5);

        // Description Text Area
        Label lblDescription = new Label("Course Description");
        lblDescription.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextArea txtDescription = new TextArea();
        txtDescription.setMinSize(500, 125);
        txtDescription.setMaxSize(500, 125);
        txtDescription.setFont(Font.font("Arial", 18));
        
        pane.add(lblDescription, 0, 6);
        pane.add(txtDescription, 0, 7);

        // Warning label
        Label lblWarning = new Label("*This is a required field");
        lblWarning.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblWarning.setPadding(new Insets(92, 0, 0, 0));
        
        pane.add(lblWarning, 0, 8);

        // Register button
        Button btnRegister = new Button("Register");
        btnRegister.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnRegister.setMinSize(150, 50);
        btnRegister.setMaxSize(150, 50);
        
        pane.add(btnRegister, 0, 9);

        // Output label
        Label lblOutput = new Label();
        lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblOutput.setTextFill(Color.RED);
        lblOutput.setPadding(new Insets(30, 0, 0, 0));
        lblOutput.setVisible(false);
        
        pane.add(lblOutput, 0, 10, 2, 1);
        
        // Register Button Event Handler
        btnRegister.setOnAction(e -> {
            String name = txtName.getText().trim(), code = txtCode.getText().trim(), description = txtDescription.getText().trim();
            
            // Check for empty text fields
            if (name.isEmpty() || code.isEmpty() || cbGrade.getValue() == null) {
                lblOutput.setTextFill(Color.RED);
                lblOutput.setText("You did not fill in all the required fields.");
                main.showVanish(lblOutput, 2);
                return;
            }
            
            // Prevent creating duplicate Courses
            for (Course course : main.courses) {
                if (course.getCode().equalsIgnoreCase(code)) {
                    lblOutput.setTextFill(Color.RED);
                    lblOutput.setText("There is already a Course registered with this course code.");
                    main.showVanish(lblOutput, 2);
                    return;
                }
            }

            Grade grade = cbGrade.getValue();
            
            // Create Course object and add it to the data structure
            Course course = new Course(name, code, description, grade);
            main.courses.add(course);
            
            lblOutput.setTextFill(Color.GREEN);
            lblOutput.setText("Successfully registered Course \"" + code + "\".");
            main.showVanish(lblOutput, 2);
        });
    }
    
}