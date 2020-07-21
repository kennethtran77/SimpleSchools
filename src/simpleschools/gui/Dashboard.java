package simpleschools.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import simpleschools.SimpleSchools;
import simpleschools.objects.Student;

/**
 *
 * @author Kenneth
 */
public final class Dashboard extends Page {
    
    private final Text txtStudentsRegistered, txtHomeformsRegistered, txtGradeAverage, txtCoursesRegistered, txtClassesRegistered, txtGradesRegistered;
    
    public Dashboard(SimpleSchools main) {
        super(main);
        
        // Initialize root component
        this.component = new GridPane();
        GridPane root = (GridPane) component;
        
        root.setHgap(25);
        root.setVgap(25);
        
        root.setMaxSize(650, 750);
        root.setMinSize(650, 750);
        
        // Page title
        Label lblTitle = new Label("Dashboard");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        
        root.add(lblTitle, 0, 0);
        
        // --- Student Info Box
        GridPane studentInfoBox = new GridPane();
        studentInfoBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        studentInfoBox.setMaxSize(300, 300);
        studentInfoBox.setMinSize(300, 300);
        studentInfoBox.setPadding(new Insets(10, 10, 10, 10));
        
        Label lblStudentTitle = new Label("Student Information");
        lblStudentTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        // Students Registered
        txtStudentsRegistered = new Text(this.getMain().students.size() + "");
        txtStudentsRegistered.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Text txtStudentsRegisteredB = new Text(" students registered");
        txtStudentsRegisteredB.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        
        TextFlow txtFlowStudentsRegistered = new TextFlow();
        txtFlowStudentsRegistered.setPadding(new Insets(40, 0, 0, 0));
        txtFlowStudentsRegistered.getChildren().addAll(txtStudentsRegistered, txtStudentsRegisteredB);
        
        // Homeforms Registered
        txtHomeformsRegistered = new Text(this.getMain().homeforms.size() + "");
        txtHomeformsRegistered.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Text txtHomeformsRegisteredB = new Text(" homeforms registered");
        txtHomeformsRegisteredB.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        
        TextFlow txtFlowHomeformsRegistered = new TextFlow();
        txtFlowHomeformsRegistered.getChildren().addAll(txtHomeformsRegistered, txtHomeformsRegisteredB);

        // Grade Average
        double avg = 0;
        
        for (Student student : main.students)
            avg += student.calculateAverage();
        
        if (main.students.size() > 0)
            avg /= main.students.size();

        // Shorten the double
        int temp = (int)(avg * 10.0);
        avg = ((double) temp) / 10.0;
        
        txtGradeAverage = new Text(avg + "%");
        txtGradeAverage.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Text txtGradeAverageB = new Text(" grade average");
        txtGradeAverageB.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        
        TextFlow txtFlowGradeAverage = new TextFlow();
        txtFlowGradeAverage.getChildren().addAll(txtGradeAverage, txtGradeAverageB);
        
        studentInfoBox.add(lblStudentTitle, 0, 0);
        studentInfoBox.add(txtFlowStudentsRegistered, 0, 1);
        studentInfoBox.add(txtFlowHomeformsRegistered, 0, 2);
        studentInfoBox.add(txtFlowGradeAverage, 0, 3);
        
        // Add student info box to root
        root.add(studentInfoBox, 0, 1);
        
        // --- >
        
        // --- Course Info Box
        GridPane courseInfoBox = new GridPane();
        courseInfoBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        courseInfoBox.setMaxSize(300, 300);
        courseInfoBox.setMinSize(300, 300);
        courseInfoBox.setPadding(new Insets(10, 10, 10, 10));
        
        Label lblCourseInfoTitle = new Label("Course Information");
        lblCourseInfoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        courseInfoBox.add(lblCourseInfoTitle, 0, 0);

        // Courses Registered
        txtCoursesRegistered = new Text(this.getMain().courses.size() + "");
        txtCoursesRegistered.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Text txtCoursesRegisteredB = new Text(" courses registered");
        txtCoursesRegisteredB.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        
        TextFlow txtFlowCoursesRegistered = new TextFlow();
        txtFlowCoursesRegistered.setPadding(new Insets(40, 0, 0, 0));
        txtFlowCoursesRegistered.getChildren().addAll(txtCoursesRegistered, txtCoursesRegisteredB);

        courseInfoBox.add(txtFlowCoursesRegistered, 0, 1);
        
        // Add course info box to root
        root.add(courseInfoBox, 1, 1); 

        // --- >
        
        // --- Class Info Box
        GridPane classInfoBox = new GridPane();
        classInfoBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        classInfoBox.setMaxSize(300, 300);
        classInfoBox.setMinSize(300, 300);
        classInfoBox.setPadding(new Insets(10, 10, 10, 10));
        
        Label lblClassInfoTitle = new Label("Class Information");
        lblClassInfoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        classInfoBox.add(lblClassInfoTitle, 0, 0);
        
        // Classes Registered
        txtClassesRegistered = new Text(this.getMain().classes.size() + "");
        txtClassesRegistered.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Text txtClassesRegisteredB = new Text(" classes registered");
        txtClassesRegisteredB.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        
        TextFlow txtFlowClassesRegistered = new TextFlow();
        txtFlowClassesRegistered.setPadding(new Insets(40, 0, 0, 0));
        txtFlowClassesRegistered.getChildren().addAll(txtClassesRegistered, txtClassesRegisteredB);

        classInfoBox.add(txtFlowClassesRegistered, 0, 1);
        
        // Add class info box to root
        root.add(classInfoBox, 0, 2);
        
        // --- >
        
        // --- Grade Info Box
        GridPane gradeInfoBox = new GridPane();
        gradeInfoBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        gradeInfoBox.setMaxSize(300, 300);
        gradeInfoBox.setMinSize(300, 300);
        gradeInfoBox.setPadding(new Insets(10, 10, 10, 10));
        
        Label lblGradeInfoTitle = new Label("Grade Information");
        lblGradeInfoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        
        gradeInfoBox.add(lblGradeInfoTitle, 0, 0);
        
        // Classes Registered
        txtGradesRegistered = new Text(this.getMain().grades.size() + "");
        txtGradesRegistered.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Text txtGradesRegisteredB = new Text(" grades registered");
        txtGradesRegisteredB.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        
        TextFlow txtFlowGradesRegistered = new TextFlow();
        txtFlowGradesRegistered.setPadding(new Insets(40, 0, 0, 0));
        txtFlowGradesRegistered.getChildren().addAll(txtGradesRegistered, txtGradesRegisteredB);

        gradeInfoBox.add(txtFlowGradesRegistered, 0, 1);
        
        // Add grade info box to root
        root.add(gradeInfoBox, 1, 2);
        
        // --- >
    }
}