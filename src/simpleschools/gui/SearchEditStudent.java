package simpleschools.gui;

import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleDoubleProperty;
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
import simpleschools.objects.Class;
import simpleschools.objects.Entry;
import simpleschools.objects.Grade;
import simpleschools.objects.Homeform;
import simpleschools.objects.Student;

/**
 *
 * @author Kenneth Tran
 */
public class SearchEditStudent extends Page {
    
    private GridPane root;
    
    private GridPane resultPane;
    private GridPane editStudentPane;
    private GridPane classesPane;
    private GridPane entriesPane;
    
    private TableView tableStudents;
    private TableView tableClasses;
    private TableView tableEntries;
    
    private TextField txtStudentAverage;
    private TextField txtStudentAverageClass;
    
    public SearchEditStudent(SimpleSchools main) {
        super(main);
        
        // Initialize root component
        this.component = new GridPane();
        root = (GridPane) component;
        
        root.setHgap(25);
        root.setVgap(25);

        root.setMaxSize(650, 750);
        root.setMinSize(650, 750);

        // Page title
        Label lblTitle = new Label("Search/Edit/Remove Student");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        
        root.add(lblTitle, 0, 0);
        
        // < --- Search Pane
        GridPane searchPane = new GridPane();
        searchPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        searchPane.setMaxSize(600, 125);
        searchPane.setMinSize(600, 125);
        searchPane.setVgap(10);
        searchPane.setPadding(new Insets(10, 10, 10, 10));

        // Search Pane Column Constraints
        for (int i = 0; i < 2; i++) {
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(40);
            searchPane.getColumnConstraints().add(constraint);
        }

        root.add(searchPane, 0, 1);
        
        // Find Title
        Label lblFindTitle = new Label("Find Student");
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
        
        cbSearchCriteria.getItems().add(Criteria.STUDENT_ID);
        cbSearchCriteria.getItems().add(Criteria.NAME);
        cbSearchCriteria.getItems().add(Criteria.GRADE);
        cbSearchCriteria.getItems().add(Criteria.HOMEFORM);
        
        cbSearchCriteria.setValue(Criteria.STUDENT_ID);
        
        searchPane.add(lblCriteria, 1, 1);
        searchPane.add(cbSearchCriteria, 1, 2);
        
        // Search Button
        Button btnSearch = new Button("Search");
        btnSearch.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnSearch.setMinSize(100, 35);
        btnSearch.setMaxSize(100, 35);
        
        searchPane.add(btnSearch, 2, 2);
        
        // -------------------------- >
        
        // < --- Results Pane
        resultPane = new GridPane();
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
        Label lblResultsTitle = new Label("Student(s) Matching Search");
        lblResultsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        resultPane.add(lblResultsTitle, 0, 0, 2, 1);
        
        // Results TableView
        tableStudents = new TableView();
        tableStudents.setMinWidth(580);
        tableStudents.setMaxWidth(580);
        
        resultPane.add(tableStudents, 0, 1);
        
        // Student ID Column
        TableColumn<Student, String> homeformCodeColumn = new TableColumn<>("Student ID");
        homeformCodeColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        homeformCodeColumn.setResizable(false);
        homeformCodeColumn.setMinWidth(140);
        homeformCodeColumn.setSortable(false);
        
        tableStudents.getColumns().add(homeformCodeColumn);

        // Name Column
        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameColumn.setResizable(false);
        nameColumn.setMinWidth(140);
        nameColumn.setSortable(false);
        
        tableStudents.getColumns().add(nameColumn);

        // Grade Value Column
        TableColumn<Student, Grade> gradeValColumn = new TableColumn<>("Grade");
        gradeValColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeValColumn.setResizable(false);
        gradeValColumn.setMinWidth(140);
        gradeValColumn.setSortable(false);
        
        tableStudents.getColumns().add(gradeValColumn);

        // Class Value Column
        TableColumn<Student, Homeform> homeformColumn = new TableColumn<>("Homeform");
        homeformColumn.setCellValueFactory(new PropertyValueFactory<>("homeform"));
        homeformColumn.setResizable(false);
        homeformColumn.setMinWidth(140);
        homeformColumn.setSortable(false);
        
        tableStudents.getColumns().add(homeformColumn);
        
        // Populate students table
        main.students.forEach((student) -> {
            tableStudents.getItems().add(student);
        });

        // Search Button Event Handler
        btnSearch.setOnAction(searchEvent -> {
            List<Student> search;

            // Change editPane to resultPane
            root.getChildren().clear();
            root.add(lblTitle, 0, 0);
            root.add(searchPane, 0, 1);
            root.add(resultPane, 0, 2);
            
            if (txtFind.getText().isEmpty()) {
                search = getMain().students;
            } else {
                search = getMain().searchStudent(txtFind.getText().trim(), cbSearchCriteria.getValue());
            }
            
            tableStudents.getItems().clear();
            tableStudents.getItems().addAll(search);
        });

        // Sort Criteria Combobox
        Label lblSortCriteria = new Label("Sort Based On:");
        lblSortCriteria.setFont(Font.font("Arial", 18));

        ComboBox<Criteria> cbSortCriteria = new ComboBox<>();
        cbSortCriteria.setMinSize(200, 35);
        cbSortCriteria.setMaxSize(200, 35);

        cbSortCriteria.getItems().add(Criteria.STUDENT_ID);
        cbSortCriteria.getItems().add(Criteria.NAME);
        cbSortCriteria.getItems().add(Criteria.GRADE);
        cbSortCriteria.getItems().add(Criteria.HOMEFORM);
        
        cbSortCriteria.setValue(Criteria.STUDENT_ID);

        resultPane.add(lblSortCriteria, 0, 2);
        resultPane.add(cbSortCriteria, 0, 3);
        
        // Sort Button
        Button btnSort = new Button("Sort");
        btnSort.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnSort.setMinSize(100, 35);
        btnSort.setMaxSize(100, 35);

        resultPane.add(btnSort, 1, 3);

        // Sort Button Event Handler
        btnSort.setOnAction(sortEvent -> {
            // Use merge sort to sort result list
            if (tableStudents.getItems().size() == getMain().students.size()) { // Merge the actual ArrayList
                getMain().sortStudents(cbSortCriteria.getValue(), getMain().students);
                
                tableStudents.getItems().clear();
                tableStudents.getItems().addAll(getMain().students);
            } else // Merge the search results
                getMain().sortStudents(cbSortCriteria.getValue(), tableStudents.getItems());
        });
        
        // Select Student Row Event Handler
        tableStudents.setOnMousePressed(tableStudentsEvent -> {
            if (tableStudents.getSelectionModel().getSelectedItem() != null && tableStudentsEvent.getClickCount() == 2 && tableStudentsEvent.isPrimaryButtonDown()) {
                Student student = (Student) tableStudents.getSelectionModel().getSelectedItem();

                openStudentProfilePane(student);
           }
        });
        
        // ------------------- >
    }
    
    /**
     * View a Student Profile
     * @param student
     * @param tableStudents 
     */
    private void openStudentProfilePane(Student student) {
        // Edit Student Pane
        editStudentPane = new GridPane();
        editStudentPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        editStudentPane.setMaxSize(600, 460);
        editStudentPane.setMinSize(600, 460);
        editStudentPane.setVgap(10);
        editStudentPane.setPadding(new Insets(10, 10, 10, 10));

        // Edit Student Pane Column Constraints
        for (int i = 0; i < 3; i++) {
            ColumnConstraints editPaneConstraint = new ColumnConstraints();
            editPaneConstraint.setPercentWidth(23);
            editStudentPane.getColumnConstraints().add(editPaneConstraint);
        }

        // Change resultPane to editStudentPane
        root.getChildren().remove(2);
        root.add(editStudentPane, 0, 2);

        // Edit Pane Title
        Label lblEditTitle = new Label("Student \"" + student.getFullName() + "\"");
        lblEditTitle.setPadding(new Insets(0, 0, 20, 0));
        lblEditTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        editStudentPane.add(lblEditTitle, 0, 0, 3, 1);

        // First Name Text Field
        Label lblFirstName = new Label("First Name*");
        lblFirstName.setFont(Font.font("Arial", 18));

        TextField txtFirstName = new TextField(student.getFirstName());
        txtFirstName.setMinSize(260, 35);
        txtFirstName.setMaxSize(260, 35);
        txtFirstName.setFont(Font.font("Arial", 18));

        editStudentPane.add(lblFirstName, 0, 1);
        editStudentPane.add(txtFirstName, 0, 2);

        // Middle Name Text Field
        Label lblMiddleName = new Label("Middle Name*");
        lblMiddleName.setFont(Font.font("Arial", 18));

        TextField txtMiddleName = new TextField(student.getMiddleName());
        txtMiddleName.setMinSize(260, 35);
        txtMiddleName.setMaxSize(260, 35);
        txtMiddleName.setFont(Font.font("Arial", 18));

        editStudentPane.add(lblMiddleName, 0, 3);
        editStudentPane.add(txtMiddleName, 0, 4);

        // Last Name Text Field
        Label lblLastName = new Label("Last Name*");
        lblLastName.setFont(Font.font("Arial", 18));

        TextField txtLastName = new TextField(student.getLastName());
        txtLastName.setMinSize(260, 35);
        txtLastName.setMaxSize(260, 35);
        txtLastName.setFont(Font.font("Arial", 18));

        editStudentPane.add(lblLastName, 0, 5);
        editStudentPane.add(txtLastName, 0, 6);

        // Student Id Text Field
        Label lblStudentId = new Label("Student ID*");
        lblStudentId.setFont(Font.font("Arial", 18));

        TextField txtStudentId = new TextField(student.getStudentId() + "");
        txtStudentId.setDisable(true);
        txtStudentId.setMinSize(260, 35);
        txtStudentId.setMaxSize(260, 35);
        txtStudentId.setFont(Font.font("Arial", 18));

        editStudentPane.add(lblStudentId, 0, 7);
        editStudentPane.add(txtStudentId, 0, 8);

        // Grade Combobox
        Label lblGrade = new Label("Grade*");
        lblGrade.setPadding(new Insets(0, 0, 0, 150));
        lblGrade.setFont(Font.font("Arial", 18));

        ComboBox<Grade> cbGrade = new ComboBox<>();
        GridPane.setMargin(cbGrade, new Insets(0, 0, 0, 150));
        cbGrade.setDisable(true);
        cbGrade.setMinSize(260, 35);
        cbGrade.setMaxSize(260, 35);

        cbGrade.setValue(student.getGrade());

        editStudentPane.add(lblGrade, 1, 1, 3, 1);
        editStudentPane.add(cbGrade, 1, 2);

        // Homeform Combobox
        Label lblHomeform = new Label("Homeform*");
        lblHomeform.setPadding(new Insets(0, 0, 0, 150));
        lblHomeform.setFont(Font.font("Arial", 18));

        ComboBox<Homeform> cbHomeform = new ComboBox<>();
        GridPane.setMargin(cbHomeform, new Insets(0, 0, 0, 150));
        cbHomeform.setMinSize(260, 35);
        cbHomeform.setMaxSize(260, 35);

        getMain().homeforms.forEach((homeform) -> cbHomeform.getItems().add(homeform));

        cbHomeform.setValue(student.getHomeform());

        // Update "Grade Combobox" when Homeform is changed
        cbHomeform.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            cbGrade.setValue(newValue.getGrade());
        });

        editStudentPane.add(lblHomeform, 1, 3, 3, 1);
        editStudentPane.add(cbHomeform, 1, 4);

        // Email Text Field
        Label lblEmail = new Label("Email*");
        lblEmail.setPadding(new Insets(0, 0, 0, 150));
        lblEmail.setFont(Font.font("Arial", 18));

        TextField txtEmail = new TextField(student.getEmail());
        GridPane.setMargin(txtEmail, new Insets(0, 0, 0, 150));
        txtEmail.setMinSize(260, 35);
        txtEmail.setMaxSize(260, 35);
        txtEmail.setFont(Font.font("Arial", 18));

        editStudentPane.add(lblEmail, 1, 5, 3, 1);
        editStudentPane.add(txtEmail, 1, 6);

        // Phone Number Text Field
        Label lblPhoneNumber = new Label("Phone Number");
        lblPhoneNumber.setPadding(new Insets(0, 0, 0, 150));
        lblPhoneNumber.setFont(Font.font("Arial", 18));

        TextField txtPhoneNumber = new TextField(student.getPhoneNumber());
        GridPane.setMargin(txtPhoneNumber, new Insets(0, 0, 0, 150));
        txtPhoneNumber.setMinSize(260, 35);
        txtPhoneNumber.setMaxSize(260, 35);
        txtPhoneNumber.setFont(Font.font("Arial", 18));

        editStudentPane.add(lblPhoneNumber, 1, 7, 3, 1);
        editStudentPane.add(txtPhoneNumber, 1, 8);

        // Warning label
        Label lblWarning = new Label("*This is a required field");
        lblWarning.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblWarning.setPadding(new Insets(-4, 0, 0, 0));

        editStudentPane.add(lblWarning, 0, 9, 3, 1);

        // Save Changes button
        Button btnSaveStudent = new Button("Save");
        btnSaveStudent.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnSaveStudent.setMinSize(125, 50);
        btnSaveStudent.setMaxSize(125, 50);

        editStudentPane.add(btnSaveStudent, 0, 10);

        // Delete Student button
        Button btnDeleteStudent = new Button("Delete");
        btnDeleteStudent.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnDeleteStudent.setMinSize(125, 50);
        btnDeleteStudent.setMaxSize(125, 50);

        editStudentPane.add(btnDeleteStudent, 1, 10);

        // Back Changes button
        Button btnBack = new Button("Back");
        btnBack.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnBack.setMinSize(125, 50);
        btnBack.setMaxSize(125, 50);

        editStudentPane.add(btnBack, 2, 10);

        // Classes button
        Button btnClasses = new Button("Classes");
        btnClasses.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnClasses.setMinSize(125, 50);
        btnClasses.setMaxSize(125, 50);

        editStudentPane.add(btnClasses, 3, 10);

        // Output label
        Label lblOutput = new Label();
        lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblOutput.setTextFill(Color.RED);
        lblOutput.setPadding(new Insets(30, 0, 0, 0));
        lblOutput.setVisible(false);

        editStudentPane.add(lblOutput, 0, 11, 5, 1);

        // Save Student Button Event Handler
        btnSaveStudent.setOnAction(saveStudentEvent -> {
            String firstName = txtFirstName.getText().trim(), middleName = txtMiddleName.getText().trim(), lastName = txtLastName.getText().trim(), email = txtEmail.getText().trim(), phoneNumber = txtPhoneNumber.getText().trim();

            // Check for empty text fields
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || cbGrade.getValue() == null || cbHomeform.getValue() == null) {      
                lblOutput.setTextFill(Color.RED);
                lblOutput.setText("You did not fill in all the required fields.");
                getMain().showVanish(lblOutput, 2);
                return;
            }

            // Add old to toPurge
            getMain().toPurge.add(getMain().studentsPath.toString() + "/" + student.getStudentId() + ".json");

            // Update Student object
            student.setFirstName(firstName);
            student.setMiddleName(middleName);
            student.setLastName(lastName);
            student.setEmail(email);
            student.setPhoneNumber(phoneNumber);

            Grade oldGrade = student.getGrade();

            // Add student to their new Homeform Class
            if (!cbHomeform.getValue().equals(student.getHomeform())) {
                student.setHomeform(cbHomeform.getValue());

                if (!cbHomeform.getValue().getClassroom().containsStudent(student))
                    student.getHomeform().getClassroom().addStudent(student);
            }

            // Remove student from classes that are not of the same grade as the student's new grade
            if (!student.getGrade().equals(oldGrade)) {
                getMain().getStudentClasses(student).stream().filter(cl -> !cl.getGrade().equals(student.getGrade())).forEach(cl -> cl.removeStudent(student));
            }

            // Remove updated from toPurge
            getMain().toPurge.remove(getMain().studentsPath.toString() + "/" + student.getStudentId() + ".json");

            // Update Edit Pane Title
            lblEditTitle.setText("Student \"" + student.getFullName() + "\"");

            lblOutput.setTextFill(Color.GREEN);
            lblOutput.setText("Successfully saved Student \"" + student.getFullName() + "\".");
            getMain().showVanish(lblOutput, 2);
        });

        // Delete Student Button Event Handler
        btnDeleteStudent.setOnAction(deleteStudentEvent -> {
            Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete Student \"" + student.getStudentId() + "\"?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText(null);
            alert.initStyle(StageStyle.UTILITY);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {
                // Delete student
                tableStudents.getItems().remove(student);
                student.unregister(getMain());

                // Change resultPane to editPane
                root.getChildren().remove(2);
                root.add(resultPane, 0, 2);
            }
        });

        // Back Button Event Handler
        btnBack.setOnAction(backEvent -> {
            // Change editPane to resultPane
            root.getChildren().remove(2);
            root.add(resultPane, 0, 2);
        });

        // Classes Button Event Handler
        btnClasses.setOnAction(classesEvent -> {
            // View this Student's Classes
            openStudentClassesListPane(student);
        });
    }
    
    /**
     * View Student Classes
     * @param student
     * @param editStudentPane 
     */
    private void openStudentClassesListPane(Student student) {
        // Classes Pane
        classesPane = new GridPane();
        classesPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        classesPane.setMaxSize(600, 460);
        classesPane.setMinSize(600, 460);
        classesPane.setVgap(10);
        classesPane.setPadding(new Insets(10, 10, 10, 10));

        // Classes Pane Column Constraints
        for (int i = 0; i < 3; i++) {
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(27.5);
            classesPane.getColumnConstraints().add(constraint);
        }

        // Change editPane to classesPane
        root.getChildren().remove(2);
        root.add(classesPane, 0, 2);

        // Classes Pane Title
        Label lblClassesTitle = new Label("Student \"" + student.getFullName() + "\" Classes");
        lblClassesTitle.setPadding(new Insets(0, 0, 20, 0));
        lblClassesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        classesPane.add(lblClassesTitle, 0, 0, 3, 1);

        // Classes TableView
        tableClasses = new TableView();
        tableClasses.setMinWidth(580);
        tableClasses.setMaxWidth(580);

        classesPane.add(tableClasses, 0, 1);

        // Course Name Column
        TableColumn<Class, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseNameColumn.setResizable(false);
        courseNameColumn.setMinWidth(240);
        courseNameColumn.setSortable(false);

        tableClasses.getColumns().add(courseNameColumn);

        // Class Column
        TableColumn<Class, String> classColumn = new TableColumn<>("Class");
        classColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        classColumn.setResizable(false);
        classColumn.setMinWidth(190);
        classColumn.setSortable(false);

        tableClasses.getColumns().add(classColumn);

        // Class Average Column
        TableColumn<Class, Double> classAverageColumn = new TableColumn<>("Average");
        classAverageColumn.setCellValueFactory(tf -> new SimpleDoubleProperty(tf.getValue().calculateAverage(student)).asObject());
        classAverageColumn.setResizable(false);
        classAverageColumn.setMinWidth(135);
        classAverageColumn.setSortable(false);

        tableClasses.getColumns().add(classAverageColumn);

        // Populate table
        getMain().getStudentClasses(student).forEach((cl) -> tableClasses.getItems().add(cl));

        // Add Class Button
        Button btnAddClass = new Button("Add Class");
        btnAddClass.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnAddClass.setMinSize(150, 35);
        btnAddClass.setMaxSize(150, 35);

        classesPane.add(btnAddClass, 0, 2);

        // Student Average Text Field
        txtStudentAverage = new TextField(student.calculateAverage() + "");
        GridPane.setMargin(txtStudentAverage, new Insets(0, 0, 0, 120));
        txtStudentAverage.setFont(Font.font("Arial", 18));
        txtStudentAverage.setMinSize(135, 35);
        txtStudentAverage.setMaxSize(135, 35);
        txtStudentAverage.setDisable(true);

        classesPane.add(txtStudentAverage, 2, 2);

        // Add Class Button Event Handler
        btnAddClass.setOnAction(addClassEvent -> {
            // Add Class Pane Pane
            GridPane addClassPane = new GridPane();
            addClassPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
            addClassPane.setMaxSize(600, 460);
            addClassPane.setMinSize(600, 460);
            addClassPane.setVgap(10);
            addClassPane.setPadding(new Insets(10, 10, 10, 10));

            // Add Class Pane Column Constraint
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(27.5);
            addClassPane.getColumnConstraints().add(constraint);

            // Change classesPane to addClassPane
            root.getChildren().remove(2);
            root.add(addClassPane, 0, 2);

            // Add Class Pane Title
            Label lblAddClassTitle = new Label("Add Class for Student \"" + student.getFullName() + "\"");
            lblAddClassTitle.setPadding(new Insets(0, 0, 20, 0));
            lblAddClassTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

            addClassPane.add(lblAddClassTitle, 0, 0, 3, 1);

            // Class Combobox
            Label lblClass = new Label("Class*");
            lblClass.setFont(Font.font("Arial", 18));

            ComboBox<Class> cbClass = new ComboBox<>();
            cbClass.setMinSize(500, 35);
            cbClass.setMaxSize(500, 35);

            // Fill Class Combobox
            getMain().classes.stream().filter(c -> c.getGrade().equals(student.getGrade())).forEach(c -> cbClass.getItems().add(c));

            addClassPane.add(lblClass, 0, 1);
            addClassPane.add(cbClass, 0, 2);

            // Warning label
            Label lblWarning = new Label("*This is a required field");
            lblWarning.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            lblWarning.setPadding(new Insets(242, 0, 0, 0));

            addClassPane.add(lblWarning, 0, 3, 3, 1);

            // Enroll Button
            Button btnEnroll = new Button("Enroll");
            btnEnroll.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            btnEnroll.setMinSize(150, 35);
            btnEnroll.setMaxSize(150, 35);

            addClassPane.add(btnEnroll, 0, 4);

            // View Classes Button
            Button btnViewClasses = new Button("View Classes");
            btnViewClasses.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            btnViewClasses.setMinSize(150, 35);
            btnViewClasses.setMaxSize(150, 35);

            addClassPane.add(btnViewClasses, 1, 4);

            // Output label
            Label lblOutput = new Label();
            lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            lblOutput.setTextFill(Color.RED);
            lblOutput.setPadding(new Insets(30, 0, 0, 0));
            lblOutput.setVisible(false);

            addClassPane.add(lblOutput, 0, 5, 3, 1);

            // Enroll Button Event Handler
            btnEnroll.setOnAction(enrollEvent -> {
                // Check for empty fields
                if (cbClass.getValue() == null) {
                    lblOutput.setTextFill(Color.RED);
                    lblOutput.setText("You did not fill in all the required fields.");
                    getMain().showVanish(lblOutput, 2);
                    return;
                }

                // Prevent enrolling in a class if already enrolled
                if (cbClass.getValue().containsStudent(student)) {
                    lblOutput.setTextFill(Color.RED);
                    lblOutput.setText("\"" + student.getFullName() + "\" is already enrolled in this Class.");
                    getMain().showVanish(lblOutput, 2);
                    return;
                }

                // Enroll student
                cbClass.getValue().addStudent(student);
                tableClasses.getItems().add(cbClass.getValue());
                
                lblOutput.setTextFill(Color.GREEN);
                lblOutput.setText("Added \"" + student.getFullName() + "\" to this Class.");
                getMain().showVanish(lblOutput, 2);
            });

            // View Classes Button Event Handler
            btnViewClasses.setOnAction(viewClassesEvent -> {
                // Change addClassPane to classesPane
                root.getChildren().remove(2);
                root.add(classesPane, 0, 2);

                // Update Student Average
                txtStudentAverage.setText(student.calculateAverage() + "");
            });
        });

        // View Student Button
        Button btnViewStudent = new Button("View Student");
        btnViewStudent.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnViewStudent.setMinSize(150, 35);
        btnViewStudent.setMaxSize(150, 35);

        classesPane.add(btnViewStudent, 1, 2);

        // View Student Button Event Handler
        btnViewStudent.setOnAction(viewStudentEvent -> {
            // Change classesPane to editStudentPane
            root.getChildren().remove(2);
            root.add(editStudentPane, 0, 2);
        });

        // Select Class Row Event Handler
        tableClasses.setOnMousePressed(tableClassesEvent -> {
            if (tableClasses.getSelectionModel().getSelectedItem() != null && tableClassesEvent.getClickCount() == 2 && tableClassesEvent.isPrimaryButtonDown()) {
                Class cl = (Class) tableClasses.getSelectionModel().getSelectedItem();

                // View this Student's list of Entries for this Class
                openEntriesListPane(cl, student);
            }
        });
    }
    
    /**
     * Updates the average column on the TableView of list of the Student's classes
     */
    private void updateStudentClassAverageColumn() {
        ((TableColumn) tableClasses.getColumns().get(2)).setVisible(false);
        ((TableColumn) tableClasses.getColumns().get(2)).setVisible(true);
    }
    
    /**
     * View Entries List for a Student's Class
     * @param cl
     * @param student
     */
    private void openEntriesListPane(Class cl, Student student) {
        // Entries Pane
        entriesPane = new GridPane();
        entriesPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        entriesPane.setMaxSize(600, 460);
        entriesPane.setMinSize(600, 460);
        entriesPane.setVgap(10);
        entriesPane.setPadding(new Insets(10, 10, 10, 10));

        // Classes Pane Column Constraints
        for (int i = 0; i < 3; i++) {
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(27.5);
            entriesPane.getColumnConstraints().add(constraint);
        }

        // Change classesPane to entriesPane
        root.getChildren().remove(2);
        root.add(entriesPane, 0, 2);

        // Entries Title
        Label lblEntriesTitle = new Label("Student \"" + student.getFullName() + "\" \"" + cl.getName() + "\" Report");
        lblEntriesTitle.setPadding(new Insets(0, 0, 20, 0));
        lblEntriesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        entriesPane.add(lblEntriesTitle, 0, 0, 4, 1);

        // Entries TableView
        tableEntries = new TableView();
        tableEntries.setMinHeight(339);
        tableEntries.setMinWidth(580);
        tableEntries.setMaxWidth(580);

        entriesPane.add(tableEntries, 0, 1);

        // Entry Name Column
        TableColumn<Entry, String> entryNameColumn = new TableColumn<>("Entry");
        entryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        entryNameColumn.setResizable(false);
        entryNameColumn.setMinWidth(150);
        entryNameColumn.setSortable(false);

        tableEntries.getColumns().add(entryNameColumn);

        // Entry Date Column
        TableColumn<Entry, String> entryDateColumn = new TableColumn<>("Date");
        entryDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        entryDateColumn.setResizable(false);
        entryDateColumn.setMinWidth(100);
        entryDateColumn.setSortable(false);

        tableEntries.getColumns().add(entryDateColumn);

        // Entry Unit Column
        TableColumn<Entry, String> entryUnitColumn = new TableColumn<>("Unit");
        entryUnitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        entryUnitColumn.setResizable(false);
        entryUnitColumn.setMinWidth(100);
        entryUnitColumn.setSortable(false);

        tableEntries.getColumns().add(entryUnitColumn);

        // Entry Weight Column
        TableColumn<Entry, Double> entryWeightColumn = new TableColumn<>("Weight");
        entryWeightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        entryWeightColumn.setResizable(false);
        entryWeightColumn.setMinWidth(100);
        entryWeightColumn.setSortable(false);

        tableEntries.getColumns().add(entryWeightColumn);

        // Entry Mark Column
        TableColumn<Entry, Double> entryMarkColumn = new TableColumn<>("Mark");
        entryMarkColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));
        entryMarkColumn.setResizable(false);
        entryMarkColumn.setMinWidth(100);
        entryMarkColumn.setSortable(false);

        tableEntries.getColumns().add(entryMarkColumn);

        // Populate Entries table
        student.getEntries(cl).forEach(entry -> tableEntries.getItems().add(entry));

        // Add Entry Button
        Button btnAddEntry = new Button("Add Entry");
        btnAddEntry.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnAddEntry.setMinSize(150, 35);
        btnAddEntry.setMaxSize(150, 35);

        entriesPane.add(btnAddEntry, 0, 2);

        // Student's Average for Class
        txtStudentAverageClass = new TextField(student.calculateAverage(cl) + "");
        GridPane.setMargin(txtStudentAverageClass, new Insets(0, 0, 0, 0));
        txtStudentAverageClass.setFont(Font.font("Arial", 18));
        txtStudentAverageClass.setMinSize(60, 35);
        txtStudentAverageClass.setMaxSize(60, 35);
        txtStudentAverageClass.setDisable(true);

        entriesPane.add(txtStudentAverageClass, 3, 2);

        // Add Entry Button Event Handler
        btnAddEntry.setOnAction(addEntryEvent -> {
            // Add Entry Pane
            GridPane addEntryPane = new GridPane();
            addEntryPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
            addEntryPane.setMaxSize(600, 460);
            addEntryPane.setMinSize(600, 460);
            addEntryPane.setVgap(10);
            addEntryPane.setPadding(new Insets(10, 10, 10, 10));

            // Edit Pane Column Constraints
            for (int i = 0; i < 3; i++) {
                ColumnConstraints constraint = new ColumnConstraints();
                constraint.setPercentWidth(27.5);
                addEntryPane.getColumnConstraints().add(constraint);
            }

            // Change resultPane to editPane
            root.getChildren().remove(2);
            root.add(addEntryPane, 0, 2);

            // Add Entry Pane Pane Title
            Label lblEntryTitle = new Label("Add Entry for Student \"" + student.getFullName() + "\" \"" + cl.getName() + "\"");
            lblEntryTitle.setPadding(new Insets(0, 0, 20, 0));
            lblEntryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

            addEntryPane.add(lblEntryTitle, 0, 0, 5, 1);

            // Entry Name Text Field
            Label lblEntryName = new Label("Entry Name*");
            lblEntryName.setFont(Font.font("Arial", 18));

            TextField txtEntryName = new TextField();
            txtEntryName.setMinSize(260, 35);
            txtEntryName.setMaxSize(260, 35);
            txtEntryName.setFont(Font.font("Arial", 18));

            addEntryPane.add(lblEntryName, 0, 1);
            addEntryPane.add(txtEntryName, 0, 2);

            // Entry Date Text Field
            Label lblEntryDate = new Label("Date*");
            lblEntryDate.setFont(Font.font("Arial", 18));

            TextField txtEntryDate = new TextField();
            txtEntryDate.setMinSize(260, 35);
            txtEntryDate.setMaxSize(260, 35);
            txtEntryDate.setFont(Font.font("Arial", 18));

            addEntryPane.add(lblEntryDate, 0, 3);
            addEntryPane.add(txtEntryDate, 0, 4);

            // Entry Unit Text Field
            Label lblEntryUnit = new Label("Unit*");
            lblEntryUnit.setFont(Font.font("Arial", 18));

            TextField txtEntryUnit = new TextField();
            txtEntryUnit.setMinSize(260, 35);
            txtEntryUnit.setMaxSize(260, 35);
            txtEntryUnit.setFont(Font.font("Arial", 18));

            addEntryPane.add(lblEntryUnit, 0, 5);
            addEntryPane.add(txtEntryUnit, 0, 6);

            // Entry Weight Text Field
            Label lblEntryWeight = new Label("Weight*");
            lblEntryWeight.setPadding(new Insets(0, 0, 0, 135));
            lblEntryWeight.setFont(Font.font("Arial", 18));

            TextField txtEntryWeight = new TextField();
            GridPane.setMargin(txtEntryWeight, new Insets(0, 0, 0, 135));
            txtEntryWeight.setMinSize(260, 35);
            txtEntryWeight.setMaxSize(260, 35);
            txtEntryWeight.setFont(Font.font("Arial", 18));

            addEntryPane.add(lblEntryWeight, 1, 1, 3, 1);
            addEntryPane.add(txtEntryWeight, 1, 2);

            // Entry Mark Text Field
            Label lblEntryMark = new Label("Mark");
            lblEntryMark.setPadding(new Insets(0, 0, 0, 135));
            lblEntryMark.setFont(Font.font("Arial", 18));

            TextField txtEntryMark = new TextField();
            GridPane.setMargin(txtEntryMark, new Insets(0, 0, 0, 135));
            txtEntryMark.setMinSize(260, 35);
            txtEntryMark.setMaxSize(260, 35);
            txtEntryMark.setFont(Font.font("Arial", 18));

            addEntryPane.add(lblEntryMark, 1, 3, 3, 1);
            addEntryPane.add(txtEntryMark, 1, 4);

            // Warning label
            Label lblWarningAddEntry = new Label("*This is a required field");
            lblWarningAddEntry.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            lblWarningAddEntry.setPadding(new Insets(90, 0, 0, 0));

            addEntryPane.add(lblWarningAddEntry, 0, 7, 3, 1);

            // Create Button
            Button btnCreate = new Button("Create");
            btnCreate.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            btnCreate.setMinSize(150, 35);
            btnCreate.setMaxSize(150, 35);

            addEntryPane.add(btnCreate, 0, 8);

            // View Entries Button
            Button btnViewEntries = new Button("View Entries");
            btnViewEntries.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            btnViewEntries.setMinSize(150, 35);
            btnViewEntries.setMaxSize(150, 35);

            addEntryPane.add(btnViewEntries, 1, 8);

            // Output label
            Label lblOutput = new Label();
            lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            lblOutput.setTextFill(Color.RED);
            lblOutput.setPadding(new Insets(30, 0, 0, 0));
            lblOutput.setVisible(false);

            addEntryPane.add(lblOutput, 0, 9, 3, 1);

            // Create Button Event Handler
            btnCreate.setOnAction(event -> {
                String entryName = txtEntryName.getText().trim(), entryDate = txtEntryDate.getText().trim();

                if (entryName.isEmpty() || entryDate.isEmpty() || txtEntryUnit.getText().isEmpty() || txtEntryWeight.getText().isEmpty()) {
                    lblOutput.setTextFill(Color.RED);
                    lblOutput.setText("You did not fill in all the required fields.");
                    getMain().showVanish(lblOutput, 2);
                    return;
                }

                double unit, weight, mark;

                // Parse doubles, detect error
                try {
                    unit = Double.parseDouble(txtEntryUnit.getText());
                    weight = Double.parseDouble(txtEntryWeight.getText());
                    mark = txtEntryMark.getText().isEmpty() ? -1 : Double.parseDouble(txtEntryMark.getText());
                } catch (NumberFormatException ex) {
                    lblOutput.setTextFill(Color.RED);
                    lblOutput.setText("You did not enter a valid double.");
                    getMain().showVanish(lblOutput, 2);
                    return;
                }

                Entry entry = new Entry(entryName, entryDate, unit, weight, mark < -1 ? -1 : mark);
                student.getEntries(cl).add(entry);
                tableEntries.getItems().add(entry);

                lblOutput.setTextFill(Color.GREEN);
                lblOutput.setText("Successfully created Entry \"" + entryName + "\".");
                getMain().showVanish(lblOutput, 2);
            });

            // View Entries Button Event Handler
            btnViewEntries.setOnAction(event -> {
                // Change addEntryPane to entriesPane
                root.getChildren().remove(2);
                root.add(entriesPane, 0, 2);

                // Update Student Class Average
                updateStudentClassAverageColumn();
                txtStudentAverageClass.setText(student.calculateAverage(cl) + "");
            });
        });

        // Unenroll Button
        Button btnUnenroll = new Button("Unenroll");
        //GridPane.setMargin(btnUnenroll, new Insets(30, 0, 0, 0));
        btnUnenroll.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnUnenroll.setMinSize(150, 35);
        btnUnenroll.setMaxSize(150, 35);

        entriesPane.add(btnUnenroll, 1, 2);

        // Error Label
        Label lblError = new Label("You cannot unenroll a Student from their Homeform Class.");
        lblError.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblError.setTextFill(Color.RED);
        lblError.setPadding(new Insets(30, 0, 0, 0));
        lblError.setVisible(false);

        entriesPane.add(lblError, 0, 3, 4, 1);

        // Unenroll Button Event Handler
        btnUnenroll.setOnAction(unenrollEvent -> {
            // Prevent unenrolling from homeform
            if (student.getHomeform().getClassroom().equals(cl)) {
                getMain().showVanish(lblError, 2);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to unenroll Student \"" + student.getStudentId() + "\" from Class \"" + cl.getName() + "\"?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText(null);
            alert.initStyle(StageStyle.UTILITY);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {
                // Delete student
                tableClasses.getItems().remove(cl);
                cl.removeStudent(student);

                // Change entriesPane to classesPane
                root.getChildren().remove(2);
                root.add(classesPane, 0, 2);

                // Update Student Average
                txtStudentAverage.setText(student.calculateAverage() + "");
            }
        });

        // View Classes Button
        Button btnViewClasses = new Button("View Classes");
        btnViewClasses.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnViewClasses.setMinSize(150, 35);
        btnViewClasses.setMaxSize(150, 35);

        entriesPane.add(btnViewClasses, 2, 2);

        // View Classes Button Event Handler
        btnViewClasses.setOnAction(viewClassesEvent -> {
            // Change entriesPane to classesPane
            root.getChildren().remove(2);
            root.add(classesPane, 0, 2);

            // Update Student Average
            txtStudentAverage.setText(student.calculateAverage() + "");
        });

        // Select Entry Row Event Handler
        tableEntries.setOnMousePressed(tableEntriesEvent -> {
            if (tableEntries.getSelectionModel().getSelectedItem() != null && tableEntriesEvent.getClickCount() == 2 && tableEntriesEvent.isPrimaryButtonDown()) {
                Entry entry = (Entry) tableEntries.getSelectionModel().getSelectedItem();

                // View the Entry's Profile
                openEntryProfilePane(student, cl, entry);
            }
        });
    }
    
    /**
     * View Entry Profile
     * @param entry 
     * @param student
     */
    private void openEntryProfilePane(Student student, Class cl, Entry entry) {
        // Edit Entry Pane
        GridPane editEntryPane = new GridPane();
        editEntryPane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        editEntryPane.setMaxSize(600, 460);
        editEntryPane.setMinSize(600, 460);
        editEntryPane.setVgap(10);
        editEntryPane.setPadding(new Insets(10, 10, 10, 10));

        // Edit Pane Column Constraints
        for (int i = 0; i < 3; i++) {
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(27.5);
            editEntryPane.getColumnConstraints().add(constraint);
        }

        // Change resultPane to editEntryPane
        root.getChildren().remove(2);
        root.add(editEntryPane, 0, 2);

        // Add Entry Pane Pane Title
        Label lblEntryTitle = new Label("Entry \"" + entry.getName() + "\" for Student \"" + student.getFullName() + "\"");
        lblEntryTitle.setPadding(new Insets(0, 0, 20, 0));
        lblEntryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        editEntryPane.add(lblEntryTitle, 0, 0, 5, 1);

        // Entry Name Text Field
        Label lblEntryName = new Label("Entry Name*");
        lblEntryName.setFont(Font.font("Arial", 18));

        TextField txtEntryName = new TextField(entry.getName());
        txtEntryName.setMinSize(260, 35);
        txtEntryName.setMaxSize(260, 35);
        txtEntryName.setFont(Font.font("Arial", 18));

        editEntryPane.add(lblEntryName, 0, 1);
        editEntryPane.add(txtEntryName, 0, 2);

        // Entry Date Text Field
        Label lblEntryDate = new Label("Date*");
        lblEntryDate.setFont(Font.font("Arial", 18));

        TextField txtEntryDate = new TextField(entry.getDate());
        txtEntryDate.setMinSize(260, 35);
        txtEntryDate.setMaxSize(260, 35);
        txtEntryDate.setFont(Font.font("Arial", 18));

        editEntryPane.add(lblEntryDate, 0, 3);
        editEntryPane.add(txtEntryDate, 0, 4);

        // Entry Unit Text Field
        Label lblEntryUnit = new Label("Unit*");
        lblEntryUnit.setFont(Font.font("Arial", 18));

        TextField txtEntryUnit = new TextField(entry.getUnit() + "");
        txtEntryUnit.setMinSize(260, 35);
        txtEntryUnit.setMaxSize(260, 35);
        txtEntryUnit.setFont(Font.font("Arial", 18));

        editEntryPane.add(lblEntryUnit, 0, 5);
        editEntryPane.add(txtEntryUnit, 0, 6);

        // Entry Weight Text Field
        Label lblEntryWeight = new Label("Weight*");
        lblEntryWeight.setPadding(new Insets(0, 0, 0, 135));
        lblEntryWeight.setFont(Font.font("Arial", 18));

        TextField txtEntryWeight = new TextField(entry.getWeight() + "");
        GridPane.setMargin(txtEntryWeight, new Insets(0, 0, 0, 135));
        txtEntryWeight.setMinSize(260, 35);
        txtEntryWeight.setMaxSize(260, 35);
        txtEntryWeight.setFont(Font.font("Arial", 18));

        editEntryPane.add(lblEntryWeight, 1, 1, 3, 1);
        editEntryPane.add(txtEntryWeight, 1, 2);

        // Entry Mark Text Field
        Label lblEntryMark = new Label("Mark");
        lblEntryMark.setPadding(new Insets(0, 0, 0, 135));
        lblEntryMark.setFont(Font.font("Arial", 18));

        TextField txtEntryMark = new TextField(entry.getMark() + "");
        GridPane.setMargin(txtEntryMark, new Insets(0, 0, 0, 135));
        txtEntryMark.setMinSize(260, 35);
        txtEntryMark.setMaxSize(260, 35);
        txtEntryMark.setFont(Font.font("Arial", 18));

        editEntryPane.add(lblEntryMark, 1, 3, 3, 1);
        editEntryPane.add(txtEntryMark, 1, 4);

        // Warning label
        Label lblWarning = new Label("*This is a required field");
        lblWarning.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblWarning.setPadding(new Insets(90, 0, 0, 0));

        editEntryPane.add(lblWarning, 0, 7, 3, 1);

        // Save Button
        Button btnSaveEntry = new Button("Save");
        btnSaveEntry.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnSaveEntry.setMinSize(150, 35);
        btnSaveEntry.setMaxSize(150, 35);

        editEntryPane.add(btnSaveEntry, 0, 8);

        // Delete Button
        Button btnDeleteEntry = new Button("Delete");
        btnDeleteEntry.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnDeleteEntry.setMinSize(150, 35);
        btnDeleteEntry.setMaxSize(150, 35);

        editEntryPane.add(btnDeleteEntry, 1, 8);

        // View Entries Button
        Button btnViewEntries = new Button("View Entries");
        btnViewEntries.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btnViewEntries.setMinSize(150, 35);
        btnViewEntries.setMaxSize(150, 35);

        editEntryPane.add(btnViewEntries, 2, 8);

        // Output label
        Label lblOutput = new Label();
        lblOutput.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblOutput.setTextFill(Color.RED);
        lblOutput.setPadding(new Insets(30, 0, 0, 0));
        lblOutput.setVisible(false);

        editEntryPane.add(lblOutput, 0, 9, 3, 1);

        // Save Entry Button Event Handler
        btnSaveEntry.setOnAction(saveEntryEvent -> {
            String entryName = txtEntryName.getText().trim(), entryDate = txtEntryDate.getText().trim();

            // Check for empty fields
            if (entryName.isEmpty() || entryDate.isEmpty() || txtEntryUnit.getText().isEmpty() || txtEntryWeight.getText().isEmpty()) {
                lblOutput.setTextFill(Color.RED);
                lblOutput.setText("You did not fill in all the required fields.");
                getMain().showVanish(lblOutput, 2);
                return;
            }

            double unit, weight, mark;

            // Parse doubles, detect error
            try {
                unit = Double.parseDouble(txtEntryUnit.getText());
                weight = Double.parseDouble(txtEntryWeight.getText());
                mark = txtEntryMark.getText().isEmpty() ? -1 : Double.parseDouble(txtEntryMark.getText());
            } catch (NumberFormatException ex) {
                lblOutput.setTextFill(Color.RED);
                lblOutput.setText("You did not enter a valid double.");
                getMain().showVanish(lblOutput, 2);
                return;
            }

            // Update Entry object
            entry.setName(entryName);
            entry.setDate(entryDate);
            entry.setMark(mark);
            entry.setUnit(unit);
            entry.setWeight(weight);

            lblOutput.setTextFill(Color.GREEN);
            lblOutput.setText("Saved Entry \"" + entryName + "\".");
            getMain().showVanish(lblOutput, 2);
        });

        // Delete Entry Button Event Handler
        btnDeleteEntry.setOnAction(deleteEntryEvent -> {
            Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete Entry \"" + entry.getName() + "\"?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText(null);
            alert.initStyle(StageStyle.UTILITY);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {
                // Delete Entry
                student.getEntries(cl).remove(entry);
                tableEntries.getItems().remove(entry);

                // Update Student Class Average
                updateStudentClassAverageColumn();
                txtStudentAverageClass.setText(student.calculateAverage(cl) + "");

                // Change editEntryPane to entriesPane
                root.getChildren().remove(2);
                root.add(entriesPane, 0, 2);
            }
        });

        // View Entries Button Event Handler
        btnViewEntries.setOnAction(viewEntriesEvent -> {
            // Change editEntryPane to entriesPane
            root.getChildren().remove(2);
            root.add(entriesPane, 0, 2);

            // Update Student Class Average
            updateStudentClassAverageColumn();
            txtStudentAverageClass.setText(student.calculateAverage(cl) + "");
            
            tableEntries.refresh();
        });
    }
    
}