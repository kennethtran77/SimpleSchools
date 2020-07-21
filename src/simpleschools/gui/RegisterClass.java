package simpleschools.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import simpleschools.SimpleSchools;
import simpleschools.objects.Class;
import simpleschools.objects.Course;
import simpleschools.objects.Grade;

/**
 *
 * @author Kenneth Tran
 */
public class RegisterClass extends Page {
    
    public RegisterClass(SimpleSchools main) {
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
        Label lblTitle = new Label("Register Class");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        
        root.add(lblTitle, 0, 0);
        root.add(pane, 0, 1);

        // Course Combobox
        Label lblCourse = new Label("Class Course*");
        lblCourse.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        ComboBox<Course> cbCourse = new ComboBox<>();
        cbCourse.setMinSize(300, 35);
        cbCourse.setMaxSize(300, 35);
        
        // Add choices to combobox
        for (Course course : main.courses) {
            cbCourse.getItems().add(course);
        }
        
        pane.add(lblCourse, 0, 1);
        pane.add(cbCourse, 0, 2);

        // Grade Combobox
        Label lblGrade = new Label("Class Grade");
        lblGrade.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        ComboBox<Grade> cbGrade = new ComboBox<>();
        cbGrade.setDisable(true);
        cbGrade.setMinSize(425, 35);
        cbGrade.setMaxSize(425, 35);

        // Update "Grades Combobox" when Course is changed
        cbCourse.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            cbGrade.setValue(newValue.getGrade());
        });

        pane.add(lblGrade, 0, 3);
        pane.add(cbGrade, 0, 4);

        // Name Text Field
        Label lblName = new Label("Class Name*");
        lblName.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextField txtName = new TextField();
        txtName.setMinSize(425, 35);
        txtName.setMaxSize(425, 35);
        txtName.setFont(Font.font("Arial", 18));
        
        pane.add(lblName, 0, 5);
        pane.add(txtName, 0, 6);

        // Generate Name Nutton
        Button btnGenerate = new Button("Generate");
        btnGenerate.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnGenerate.setMinSize(125, 35);
        btnGenerate.setMaxSize(125, 35);
        btnGenerate.setDisable(true);
        GridPane.setMargin(btnGenerate, new Insets(0, 0, 0, 20));
        
        // Enable "Generate" button once the user chooses a course
        cbCourse.setOnAction(e -> btnGenerate.setDisable(false));
        
        btnGenerate.setOnAction(e -> {
            int count = 1;
            
            for (Class cl : main.classes)
                if (cl.getCourse() == cbCourse.getValue())
                    count++;
            
            String suffix = count + "";
            
            txtName.setText(cbCourse.getValue().getCode() + "-" + "00".substring(suffix.length(), 2) + suffix);
        });
        
        pane.add(btnGenerate, 1, 6);

        // Warning label
        Label lblWarning = new Label("*This is a required field");
        lblWarning.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblWarning.setPadding(new Insets(253, 0, 0, 0));
        
        pane.add(lblWarning, 0, 7);

        // Register button
        Button btnRegister = new Button("Register");
        btnRegister.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnRegister.setMinSize(150, 50);
        btnRegister.setMaxSize(150, 50);
        
        pane.add(btnRegister, 0, 8);

        // Output label
        Label lblOutput = new Label();
        lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblOutput.setTextFill(Color.RED);
        lblOutput.setPadding(new Insets(30, 0, 0, 0));
        lblOutput.setVisible(false);
        
        pane.add(lblOutput, 0, 9, 2, 1);
        
        // Register Button Event Handler
        btnRegister.setOnAction(e -> {
            String name = txtName.getText().trim();
            
            // Check for empty text fields
            if (name.isEmpty() || cbCourse.getValue() == null) {
                lblOutput.setTextFill(Color.RED);
                lblOutput.setText("You did not fill in all the required fields.");
                main.showVanish(lblOutput, 2);
                return;
            }
            
            // Prevent creating duplicate Classes
            for (Class cl : main.classes) {
                if (cl.getName().equalsIgnoreCase(name)) {
                    lblOutput.setTextFill(Color.RED);
                    lblOutput.setText("There is already a Class registered with this name.");
                    main.showVanish(lblOutput, 2);
                    return;
                }
            }

            // Create Class object and add it to the data structure
            Class cl = new Class(cbCourse.getValue(), name);
            main.classes.add(cl);
            
            lblOutput.setTextFill(Color.GREEN);
            lblOutput.setText("Successfully registered Class \"" + name + "\".");
            main.showVanish(lblOutput, 2);
        });
    }
    
}