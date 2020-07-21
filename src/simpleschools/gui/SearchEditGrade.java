package simpleschools.gui;

import java.util.List;
import java.util.Optional;
import simpleschools.SimpleSchools;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;
import simpleschools.Criteria;
import simpleschools.objects.Grade;

/**
 *
 * @author Kenneth Tran
 */
public class SearchEditGrade extends Page {
    
    public SearchEditGrade(SimpleSchools main) {
        super(main);
        
        // Initialize root component
        this.component = new GridPane();
        GridPane root = (GridPane) component;
        
        root.setHgap(25);
        root.setVgap(25);

        root.setMaxSize(650, 750);
        root.setMinSize(650, 750);

        // Page title
        Label lblTitle = new Label("Search/Edit/Remove Grade");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        
        root.add(lblTitle, 0, 0);
        
        // < --- Search Pane
        GridPane searchPane = new GridPane();
        searchPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        searchPane.setMaxSize(600, 125);
        searchPane.setMinSize(600, 125);
        searchPane.setVgap(10);
        searchPane.setPadding(new Insets(10, 10, 10, 10));

        ColumnConstraints constraint = new ColumnConstraints();
        constraint.setPercentWidth(40);
        searchPane.getColumnConstraints().add(constraint);
        
        root.add(searchPane, 0, 1);
        
        // Find Title
        Label lblFindTitle = new Label("Find Grade");
        lblFindTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        searchPane.add(lblFindTitle, 0, 0);
        
        // Find Text Field
        Label lblFind = new Label("Find:");
        lblFind.setFont(Font.font("Arial", 18));
        
        TextField txtFind = new TextField();
        txtFind.setMinSize(200, 35);
        txtFind.setMaxSize(200, 35);
        txtFind.setFont(Font.font("Arial", 18));
        
        searchPane.add(lblFind, 0, 1);
        searchPane.add(txtFind, 0, 2);

        // Search Button
        Button btnSearch = new Button("Search");
        btnSearch.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnSearch.setMinSize(100, 35);
        btnSearch.setMaxSize(100, 35);
        
        searchPane.add(btnSearch, 1, 2);
        
        // --- >
        
        // < --- Results Pane
        GridPane resultPane = new GridPane();
        resultPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        resultPane.setMaxSize(600, 460);
        resultPane.setMinSize(600, 460);
        resultPane.setVgap(10);
        resultPane.setPadding(new Insets(10, 10, 10, 10));

        root.add(resultPane, 0, 2);

        // Results Title
        Label lblResultsTitle = new Label("Grade(s) Matching Search");
        lblResultsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        resultPane.add(lblResultsTitle, 0, 0);
        
        // Results TableView
        TableView table = new TableView();
        table.setMinWidth(580);
        table.setMaxWidth(580);
        
        resultPane.add(table, 0, 1);
        
        // Grade Value Column
        TableColumn<Grade, Grade> gradeValColumn = new TableColumn<>("Grade");
        gradeValColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        gradeValColumn.setResizable(false);
        gradeValColumn.setMinWidth(100);
        gradeValColumn.setSortable(false);
        
        table.getColumns().add(gradeValColumn);
        
        // Populate table
        for (Grade grade : main.grades) {
            table.getItems().add(grade);
        }

        // Search Button Event Handler
        btnSearch.setOnAction(event -> {
            List<Grade> search;

            // Change editPane to resultPane
            root.getChildren().clear();
            root.add(lblTitle, 0, 0);
            root.add(searchPane, 0, 1);
            root.add(resultPane, 0, 2);
            
            if (txtFind.getText().isEmpty()) {
                search = main.grades;
            } else {
                search = main.searchGrade(txtFind.getText().trim());
            }
            
            table.getItems().clear();
            table.getItems().addAll(search);
        });
        
        // Sort Button
        Button btnSort = new Button("Sort");
        btnSort.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnSort.setMinSize(100, 35);
        btnSort.setMaxSize(100, 35);

        resultPane.add(btnSort, 0, 2);

        // Sort Button Event Handler
        btnSort.setOnAction(e -> {
            // Use merge sort to sort result list
            if (table.getItems().size() == main.grades.size()) { // Merge the actual ArrayList
                main.sortGrades(Criteria.NAME, main.grades);
                
                table.getItems().clear();
                table.getItems().addAll(main.grades);
            } else
                main.sortGrades(Criteria.NAME, table.getItems());
        });
        
        // Select Row Event Handler
        table.setOnMousePressed(event -> {
            if (table.getSelectionModel().getSelectedItem() != null && event.getClickCount() == 2 && event.isPrimaryButtonDown()) {
                Grade grade = (Grade) table.getSelectionModel().getSelectedItem();
               
                // Edit Pane
                GridPane editPane = new GridPane();
                editPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
                editPane.setMaxSize(600, 460);
                editPane.setMinSize(600, 460);
                editPane.setVgap(10);
                editPane.setPadding(new Insets(10, 10, 10, 10));

                // Edit Pane Column Constraints
                ColumnConstraints editPaneConstraint1 = new ColumnConstraints();
                editPaneConstraint1.setPercentWidth(27.5);
                editPane.getColumnConstraints().add(editPaneConstraint1);
                
                ColumnConstraints editPaneConstraint2 = new ColumnConstraints();
                editPaneConstraint2.setPercentWidth(27.5);
                editPane.getColumnConstraints().add(editPaneConstraint2);

                // Change resultPane to editPane
                root.getChildren().clear();
                root.add(lblTitle, 0, 0);
                root.add(searchPane, 0, 1);
                root.add(editPane, 0, 2);

                // Edit Pane Title
                Label lblEditTitle = new Label("Grade \"" + grade.getValue() + "\"");
                lblEditTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

                editPane.add(lblEditTitle, 0, 0);

                // Grade Text Field
                Label lblGrade = new Label("Grade Name*");
                lblGrade.setPadding(new Insets(20, 0, 0, 0));
                lblGrade.setFont(Font.font("Arial", 18));

                TextField txtGrade = new TextField(grade.getValue() + "");
                txtGrade.setMinSize(500, 35);
                txtGrade.setMaxSize(500, 35);
                txtGrade.setFont(Font.font("Arial", 18));

                editPane.add(lblGrade, 0, 1);
                editPane.add(txtGrade, 0, 2);

                // Warning label
                Label lblWarning = new Label("*This is a required field");
                lblWarning.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                lblWarning.setPadding(new Insets(224, 0, 0, 0));

                editPane.add(lblWarning, 0, 3, 3, 1);

                // Save Changes button
                Button btnSave = new Button("Save");
                btnSave.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                btnSave.setMinSize(150, 50);
                btnSave.setMaxSize(150, 50);

                editPane.add(btnSave, 0, 4);

                // Delete button
                Button btnDelete = new Button("Delete");
                btnDelete.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                btnDelete.setMinSize(150, 50);
                btnDelete.setMaxSize(150, 50);

                editPane.add(btnDelete, 1, 4);

                // Back Changes button
                Button btnBack = new Button("Back");
                btnBack.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                btnBack.setMinSize(150, 50);
                btnBack.setMaxSize(150, 50);

                editPane.add(btnBack, 2, 4);

                // Output label
                Label lblOutput = new Label();
                lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                lblOutput.setTextFill(Color.RED);
                lblOutput.setPadding(new Insets(30, 0, 0, 0));
                lblOutput.setVisible(false);

                editPane.add(lblOutput, 0, 5, 3, 1);

                // --- > Button Event Handlers for Edit Pane
                
                // Save Changes Button Event Handler
                btnSave.setOnAction(e -> {
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
                    if (val != grade.getValue()) {
                        for (Grade g : main.grades) {
                            if (g.getValue() == val) {
                                lblOutput.setTextFill(Color.RED);
                                lblOutput.setText("There is already a Grade registered with this value.");
                                main.showVanish(lblOutput, 2);
                                return;
                            }
                        }
                    }

                    // Add old to toPurge
                    main.toPurge.add(main.gradesPath.toString() + "/" + grade.getValue() + ".json");
                    
                    // Update Grade object
                    grade.setValue(val);

                    // Remove updated from toPurge
                    main.toPurge.remove(main.gradesPath.toString() + "/" + grade.getValue() + ".json");
                    
                    // Update Edit Pane Title
                    lblEditTitle.setText("Grade \"" + val + "\"");

                    lblOutput.setTextFill(Color.GREEN);
                    lblOutput.setText("Successfully saved Grade \"" + val + "\".");
                    main.showVanish(lblOutput, 2);
                });
                
                // Delete Button Event Handler
                btnDelete.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete Grade \"" + grade.getValue() + "\"?", ButtonType.YES, ButtonType.NO);
                    alert.setHeaderText(null);
                    alert.initStyle(StageStyle.UTILITY);

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.YES) {
                        table.getItems().remove(grade);
                        grade.unregister(main);
                    
                        // Change resultPane to editPane
                        root.getChildren().clear();
                        root.add(lblTitle, 0, 0);
                        root.add(searchPane, 0, 1);
                        root.add(resultPane, 0, 2);
                    }
                });
                
                // Back Button Event Handler
                btnBack.setOnAction(e -> {
                    // Change editPane to resultPane
                    root.getChildren().clear();
                    root.add(lblTitle, 0, 0);
                    root.add(searchPane, 0, 1);
                    root.add(resultPane, 0, 2);
                    
                    table.refresh();
                });
           }
        });
    }
    
}