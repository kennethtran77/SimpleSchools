package simpleschools.gui;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import simpleschools.SimpleSchools;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import simpleschools.objects.Class;
import simpleschools.objects.Course;

/**
 *
 * @author Kenneth Tran
 */
public class SearchEditClass extends Page {
    
    public SearchEditClass(SimpleSchools main) {
        super(main);
        
        // Initialize root component
        this.component = new GridPane();
        GridPane root = (GridPane) component;
        
        root.setHgap(25);
        root.setVgap(25);

        root.setMaxSize(650, 750);
        root.setMinSize(650, 750);

        // Page title
        Label lblTitle = new Label("Search/Edit/Remove Class");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        
        root.add(lblTitle, 0, 0);
        
        // < --- Search Pane
        GridPane searchPane = new GridPane();
        searchPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        searchPane.setMaxSize(600, 125);
        searchPane.setMinSize(600, 125);
        searchPane.setVgap(10);
        searchPane.setPadding(new Insets(10, 10, 10, 10));

        ColumnConstraints serarchPaneConstraint1 = new ColumnConstraints();
        serarchPaneConstraint1.setPercentWidth(40);
        searchPane.getColumnConstraints().add(serarchPaneConstraint1);

        ColumnConstraints serarchPaneConstraint2 = new ColumnConstraints();
        serarchPaneConstraint2.setPercentWidth(40);
        searchPane.getColumnConstraints().add(serarchPaneConstraint2);

        root.add(searchPane, 0, 1);
        
        // Find Title
        Label lblFindTitle = new Label("Find Class");
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

        // Search Criteria Dropbox
        Label lblCriteria = new Label("Search Based On:");
        lblCriteria.setFont(Font.font("Arial", 18));
        
        ComboBox<Criteria> cbSearchCriteria = new ComboBox<>();
        cbSearchCriteria.setMinSize(200, 35);
        cbSearchCriteria.setMaxSize(200, 35);
        
        cbSearchCriteria.getItems().add(Criteria.NAME);
        cbSearchCriteria.getItems().add(Criteria.COURSE);
        cbSearchCriteria.getItems().add(Criteria.GRADE);
        
        cbSearchCriteria.setValue(Criteria.NAME);
        
        searchPane.add(lblCriteria, 1, 1);
        searchPane.add(cbSearchCriteria, 1, 2);
        
        // Search Button
        Button btnSearch = new Button("Search");
        btnSearch.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnSearch.setMinSize(100, 35);
        btnSearch.setMaxSize(100, 35);
        
        searchPane.add(btnSearch, 2, 2);
        
        // --- >
        
        // < --- Results Pane
        GridPane resultPane = new GridPane();
        resultPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        resultPane.setMaxSize(600, 460);
        resultPane.setMinSize(600, 460);
        resultPane.setVgap(10);
        resultPane.setPadding(new Insets(10, 10, 10, 10));

        ColumnConstraints resultPaneConstraint1 = new ColumnConstraints();
        resultPaneConstraint1.setPercentWidth(40);
        resultPane.getColumnConstraints().add(resultPaneConstraint1);
        
        root.add(resultPane, 0, 2);

        // Results Title
        Label lblResultsTitle = new Label("Classes(s) Matching Search");
        lblResultsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        resultPane.add(lblResultsTitle, 0, 0, 2, 1);
        
        // Results TableView
        TableView table = new TableView();
        table.setMinWidth(580);
        table.setMaxWidth(580);
        
        resultPane.add(table, 0, 1);
        
        // Homeform Code Column
        TableColumn<Class, String> homeformCodeColumn = new TableColumn<>("Class Name");
        homeformCodeColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        homeformCodeColumn.setResizable(false);
        homeformCodeColumn.setMinWidth(200);
        homeformCodeColumn.setSortable(false);
        
        table.getColumns().add(homeformCodeColumn);

        // Grade Value Column
        TableColumn<Class, Course> gradeValColumn = new TableColumn<>("Course");
        gradeValColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        gradeValColumn.setResizable(false);
        gradeValColumn.setMinWidth(175);
        gradeValColumn.setSortable(false);
        
        table.getColumns().add(gradeValColumn);

        // Class Value Column
        TableColumn<Class, Grade> classColumn = new TableColumn<>("Grade");
        classColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        classColumn.setResizable(false);
        classColumn.setMinWidth(175);
        classColumn.setSortable(false);
        
        table.getColumns().add(classColumn);
        
        // Populate table
        for (Class cl : main.classes) {
            table.getItems().add(cl);
        }

        // Search Button Event Handler
        btnSearch.setOnAction(event -> {
            List<Class> search;

            // Change editPane to resultPane
            root.getChildren().clear();
            root.add(lblTitle, 0, 0);
            root.add(searchPane, 0, 1);
            root.add(resultPane, 0, 2);
            
            if (txtFind.getText().isEmpty()) {
                search = main.classes;
            } else {
                search = main.searchClass(txtFind.getText().trim(), cbSearchCriteria.getValue());
            }
            
            table.getItems().clear();
            table.getItems().addAll(search);
        });

        // Sort Criteria Combobox
        Label lblSortCriteria = new Label("Sort Based On:");
        lblSortCriteria.setFont(Font.font("Arial", 18));

        ComboBox<Criteria> cbSortCriteria = new ComboBox<>();
        cbSortCriteria.setMinSize(200, 35);
        cbSortCriteria.setMaxSize(200, 35);

        cbSortCriteria.getItems().add(Criteria.NAME);
        cbSortCriteria.getItems().add(Criteria.COURSE);
        cbSortCriteria.getItems().add(Criteria.GRADE);
        
        cbSortCriteria.setValue(Criteria.NAME);

        resultPane.add(lblSortCriteria, 0, 2);
        resultPane.add(cbSortCriteria, 0, 3);
        
        // Sort Button
        Button btnSort = new Button("Sort");
        btnSort.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnSort.setMinSize(100, 35);
        btnSort.setMaxSize(100, 35);

        resultPane.add(btnSort, 1, 3);

        // Sort Button Event Handler
        btnSort.setOnAction(e -> {
            // Use merge sort to sort result list
            if (table.getItems().size() == main.classes.size()) { // Merge the actual ArrayList
                Collections.sort(main.classes, (a, b) -> cbSortCriteria.getValue().compare(a, b));
                
                table.getItems().clear();
                table.getItems().addAll(main.classes);
            } else
                Collections.sort(main.classes, (a, b) -> cbSortCriteria.getValue().compare(a, b));
        });
        
        // Select Row Event Handler
        table.setOnMousePressed(event -> {
            if (table.getSelectionModel().getSelectedItem() != null && event.getClickCount() == 2 && event.isPrimaryButtonDown()) {
                Class cl = (Class) table.getSelectionModel().getSelectedItem();

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
                Label lblEditTitle = new Label("Class \"" + cl.getName() + "\"");
                lblEditTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

                editPane.add(lblEditTitle, 0, 0, 2, 1);

                // Course Combobox
                Label lblCourse = new Label("Course*");
                lblCourse.setPadding(new Insets(20, 0, 0, 0));
                lblCourse.setFont(Font.font("Arial", 18));

                ComboBox<Course> cbCourse = new ComboBox<>();
                cbCourse.setMinSize(500, 35);
                cbCourse.setMaxSize(500, 35);
                
                for (Course course : main.courses) {
                    cbCourse.getItems().add(course);
                }
                
                cbCourse.setValue(cl.getCourse());

                editPane.add(lblCourse, 0, 1, 2, 1);
                editPane.add(cbCourse, 0, 2);

                // Grade Combobox
                Label lblGrade = new Label("Class Grade*");
                lblGrade.setFont(Font.font("Arial", 18));

                ComboBox<Grade> cbGrade = new ComboBox<>();
                cbGrade.setDisable(true);
                cbGrade.setMinSize(500, 35);
                cbGrade.setMaxSize(500, 35);
                
                cbGrade.setValue(cl.getGrade());

                // Update "Grades Combobox" when Course is changed
                cbCourse.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                    cbGrade.setValue(newValue.getGrade());
                });

                editPane.add(lblGrade, 0, 3, 2, 1);
                editPane.add(cbGrade, 0, 4);

                // Name Text Field
                Label lblName = new Label("Class Name*");
                lblName.setFont(Font.font("Arial", 18));

                TextField txtName = new TextField(cl.getName() + "");
                txtName.setMinSize(365, 35);
                txtName.setMaxSize(365, 35);
                txtName.setFont(Font.font("Arial", 18));

                editPane.add(lblName, 0, 5, 2, 1);
                editPane.add(txtName, 0, 6);

                // Generate Name Nutton
                Button btnGenerate = new Button("Generate");
                btnGenerate.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                btnGenerate.setMinSize(125, 35);
                btnGenerate.setMaxSize(125, 35);
                GridPane.setMargin(btnGenerate, new Insets(0, 0, 0, 55));

                btnGenerate.setOnAction(e -> {
                    int count = 1;

                    for (Class c : main.classes)
                        if (c.getCourse() == cbCourse.getValue())
                            count++;

                    String suffix = count + "";

                    txtName.setText(cbCourse.getValue().getCode() + "-" + "00".substring(suffix.length(), 2) + suffix);
                });

                editPane.add(btnGenerate, 2, 6);

                // Warning label
                Label lblWarning = new Label("*This is a required field");
                lblWarning.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                lblWarning.setPadding(new Insets(72, 0, 0, 0));

                editPane.add(lblWarning, 0, 7, 3, 1);

                // Save Changes button
                Button btnSave = new Button("Save");
                btnSave.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                btnSave.setMinSize(150, 50);
                btnSave.setMaxSize(150, 50);

                editPane.add(btnSave, 0, 8);

                // Delete button
                Button btnDelete = new Button("Delete");
                btnDelete.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                btnDelete.setMinSize(150, 50);
                btnDelete.setMaxSize(150, 50);

                editPane.add(btnDelete, 1, 8);

                // Back Changes button
                Button btnBack = new Button("Back");
                btnBack.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                btnBack.setMinSize(150, 50);
                btnBack.setMaxSize(150, 50);

                editPane.add(btnBack, 2, 8);

                // Output label
                Label lblOutput = new Label();
                lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                lblOutput.setTextFill(Color.RED);
                lblOutput.setPadding(new Insets(30, 0, 0, 0));
                lblOutput.setVisible(false);

                editPane.add(lblOutput, 0, 9, 3, 1);

                // --- > Button Event Handlers for Edit Pane
                
                // Save Changes Button Event Handler
                btnSave.setOnAction(e -> {
                    String name = txtName.getText().trim();
                    
                    // Check for empty text fields
                    if (name.isEmpty() || cbCourse.getValue() == null) {
                        lblOutput.setTextFill(Color.RED);
                        lblOutput.setText("You did not fill in all the required fields.");
                        main.showVanish(lblOutput, 2);
                        return;
                    }

                    // Prevent creating duplicate Classes
                    if (!cl.getName().equalsIgnoreCase(name)) {
                        for (Class c : main.classes) {
                            if (c.getName().equalsIgnoreCase(name)) {
                                lblOutput.setTextFill(Color.RED);
                                lblOutput.setText("There is already a Class registered with this name.");
                                main.showVanish(lblOutput, 2);
                                return;
                            }
                        }
                    }

                    // Add old to toPurge
                    main.toPurge.add(main.classesPath.toString() + "/" + cl.getName() + ".json");
                    
                    // Update Class object
                    cl.setName(name);
                    cl.setCourse(cbCourse.getValue());

                    // Remove updated from toPurge
                    main.toPurge.remove(main.classesPath.toString() + "/" + cl.getName() + ".json");
                    
                    // Update Edit Pane Title
                    lblEditTitle.setText("Class \"" + name + "\"");

                    lblOutput.setTextFill(Color.GREEN);
                    lblOutput.setText("Successfully saved Class \"" + name + "\".");
                    main.showVanish(lblOutput, 2);
                });
                
                // Delete Button Event Handler
                btnDelete.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete Class \"" + cl.getName() + "\"?", ButtonType.YES, ButtonType.NO);
                    alert.setHeaderText(null);
                    alert.initStyle(StageStyle.UTILITY);

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.YES) {
                        table.getItems().remove(cl);
                        cl.unregister(main);
                    
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