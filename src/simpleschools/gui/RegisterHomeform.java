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
import simpleschools.objects.Grade;
import simpleschools.objects.Class;
import simpleschools.objects.Homeform;

/**
 *
 * @author Kenneth Tran
 */
public class RegisterHomeform extends Page {
    
    public RegisterHomeform(SimpleSchools main) {
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
        Label lblTitle = new Label("Register Homeform");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        
        root.add(lblTitle, 0, 0);
        root.add(pane, 0, 1);

        // Code Text Field
        Label lblCode = new Label("Homeform Code*");
        lblCode.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextField txtCode = new TextField();
        txtCode.setMinSize(500, 35);
        txtCode.setMaxSize(500, 35);
        txtCode.setFont(Font.font("Arial", 18));
        
        pane.add(lblCode, 0, 0);
        pane.add(txtCode, 0, 1);

        // Grade Combobox
        Label lblGrade = new Label("Homeform Grade*");
        lblGrade.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        ComboBox<Grade> cbGrade = new ComboBox<>();
        cbGrade.setDisable(true);
        cbGrade.setMinSize(300, 35);
        cbGrade.setMaxSize(300, 35);
        
        pane.add(lblGrade, 0, 2);
        pane.add(cbGrade, 0, 3);

        // Class Combobox
        Label lblClass = new Label("Homeform Class*");
        lblClass.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        ComboBox<Class> cbClass = new ComboBox<>();
        cbClass.setMinSize(300, 35);
        cbClass.setMaxSize(300, 35);

        for (Class cl : main.classes) {
            cbClass.getItems().add(cl);
        }
                
        // Update "Grades Combobox" when Class is changed
        cbClass.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            cbGrade.setValue(newValue.getCourse().getGrade());
        });
        
        pane.add(lblClass, 0, 4);
        pane.add(cbClass, 0, 5);

        // Warning label
        Label lblWarning = new Label("*This is a required field");
        lblWarning.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblWarning.setPadding(new Insets(263, 0, 0, 0));
        
        pane.add(lblWarning, 0, 6);

        // Register button
        Button btnRegister = new Button("Register");
        btnRegister.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnRegister.setMinSize(150, 50);
        btnRegister.setMaxSize(150, 50);
        
        pane.add(btnRegister, 0, 7);

        // Output label
        Label lblOutput = new Label();
        lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblOutput.setTextFill(Color.RED);
        lblOutput.setPadding(new Insets(30, 0, 0, 0));
        lblOutput.setVisible(false);
        
        pane.add(lblOutput, 0, 8, 2, 1);
        
        // Register Button Event Handler
        btnRegister.setOnAction(e -> {
            String code = txtCode.getText().trim();
            Class cl = cbClass.getValue();
            
            // Check for empty text fields
            if (code.isEmpty() || cl == null) {
                lblOutput.setTextFill(Color.RED);
                lblOutput.setText("You did not fill in all the required fields.");
                main.showVanish(lblOutput, 2);
                return;
            }
            
            // Prevent creating duplicate Homeforms
            for (Homeform homeform : main.homeforms) {
                if (homeform.getCode().equalsIgnoreCase(code)) {
                    lblOutput.setTextFill(Color.RED);
                    lblOutput.setText("There is already a Homeform registered with this code.");
                    main.showVanish(lblOutput, 2);
                    return;
                } else if (homeform.getClassroom().getName().equalsIgnoreCase(cbClass.getValue().toString())) {
                    lblOutput.setTextFill(Color.RED);
                    lblOutput.setText("There is already a Homeform registered with this Class.");
                    main.showVanish(lblOutput, 2);
                    return;
                }
            }

            // Create Homeform object and add it to the data structure
            Homeform homeform = new Homeform(code, cl);
            main.homeforms.add(homeform);
            
            lblOutput.setTextFill(Color.GREEN);
            lblOutput.setText("Successfully registered Homeform \"" + code + "\".");
            main.showVanish(lblOutput, 2);
        });
    }
    
}