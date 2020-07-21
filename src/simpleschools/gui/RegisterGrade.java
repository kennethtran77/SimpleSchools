package simpleschools.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
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

/**
 *
 * @author Kenneth Tran
 */
public class RegisterGrade extends Page {
    
    public RegisterGrade(SimpleSchools main) {
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
        Label lblTitle = new Label("Register Grade");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        
        root.add(lblTitle, 0, 0);
        root.add(pane, 0, 1);

        // Grade Text Field
        Label lblGrade = new Label("Grade*");
        lblGrade.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        TextField txtGrade = new TextField();
        txtGrade.setMinSize(500, 35);
        txtGrade.setMaxSize(500, 35);
        txtGrade.setFont(Font.font("Arial", 18));
        
        pane.add(lblGrade, 0, 0);
        pane.add(txtGrade, 0, 1);

        // Warning label
        Label lblWarning = new Label("*This is a required field");
        lblWarning.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblWarning.setPadding(new Insets(425, 0, 0, 0));
        
        pane.add(lblWarning, 0, 2);

        // Register button
        Button btnRegister = new Button("Register");
        btnRegister.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnRegister.setMinSize(150, 50);
        btnRegister.setMaxSize(150, 50);
        
        pane.add(btnRegister, 0, 3);
        
        // Output label
        Label lblOutput = new Label();
        lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblOutput.setTextFill(Color.RED);
        lblOutput.setPadding(new Insets(30, 0, 0, 0));
        lblOutput.setVisible(false);
        
        pane.add(lblOutput, 0, 4, 2, 1);
        
        // Register Button Event Handler
        btnRegister.setOnAction(e -> {
            // Check for empty text fields
            if (txtGrade.getText().isEmpty()) {
                lblOutput.setTextFill(Color.RED);
                lblOutput.setText("You did not fill in all the required fields.");
                main.showVanish(lblOutput, 2);
                return;
            }
            
            int val;
            
            // Parse integer, detect error
            try {
                val = Integer.parseInt(txtGrade.getText());
            } catch (NumberFormatException ex) {
                lblOutput.setTextFill(Color.RED);
                lblOutput.setText("You did not enter a valid integer.");
                main.showVanish(lblOutput, 2);
                return;
            }
            
            if (val < 0) {
                lblOutput.setTextFill(Color.RED);
                lblOutput.setText("You can only enter a positive integer.");
                main.showVanish(lblOutput, 2);
                return;
            }

            // Prevent creating duplicate Grades
            for (Grade grade : main.grades) {
                if (grade.getValue() == val) {
                    lblOutput.setTextFill(Color.RED);
                    lblOutput.setText("There is already a grade registered with this value.");
                    main.showVanish(lblOutput, 2);
                    return;
                }
            }
            
            // Create Grade object and add it to the data structure
            Grade grade = new Grade(val);
            main.grades.add(grade);
            
            lblOutput.setTextFill(Color.GREEN);
            lblOutput.setText("Successfully registered grade \"" + val + "\".");
            main.showVanish(lblOutput, 2);
        });
    }
    
}