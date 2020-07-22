package simpleschools.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import simpleschools.GsonFactory;
import simpleschools.SimpleSchools;
import simpleschools.objects.Course;
import simpleschools.objects.Grade;
import simpleschools.objects.Homeform;
import simpleschools.objects.Student;

/**
 * Represents the menu
 * @author Kenneth Tran
 */
public final class Menu {
    
    public final VBox component = new VBox(5);
    private final Label lblOutput = new Label();
    
    private final SimpleSchools main;
    
    public Menu(SimpleSchools main) {
        this.main = main;
        
        // Menu component
        component.setId("menu");

        // Dashboard Button
        addDashboardButton();

        // Operations label
        Label lblOperations = new Label("Operations");
        lblOperations.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblOperations.setMinHeight(50);
        lblOperations.setMaxSize(Double.MAX_VALUE, 50);
        
        component.getChildren().add(lblOperations);

        // Add menu buttons
        addMenuButton("Register Student", () -> new RegisterStudent(main), false);
        addMenuButton("Search/Edit Student", () -> new SearchEditStudent(main), true);
        addMenuButton("Register Course", () -> new RegisterCourse(main), false);
        addMenuButton("Search/Edit Course", () -> new SearchEditCourse(main), true);
        addMenuButton("Register Homeform", () -> new RegisterHomeform(main), false);
        addMenuButton("Search/Edit Homeform", () -> new SearchEditHomeform(main), true);
        addMenuButton("Register Grade", () -> new RegisterGrade(main), false);
        addMenuButton("Search/Edit Grade", () -> new SearchEditGrade(main), true);
        addMenuButton("Register Class", () -> new RegisterClass(main), false);
        addMenuButton("Search/Edit Class", () -> new SearchEditClass(main), true);

        // Save button
        addSaveButton();

        // Exit button
        Button btnExit = new Button("Exit");
        btnExit.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnExit.setMaxSize(Double.MAX_VALUE, 25);
        btnExit.setOnAction(e -> Platform.exit());

        // Add buttons
        component.getChildren().addAll(btnExit);
    }
    
    public VBox getComponent() {
        return this.component;
    }
    
    // Add the dashboard button
    private void addDashboardButton() {
        Button btnDashboard = new Button("Dashboard");
        btnDashboard.setMinSize(200, 100);
        btnDashboard.setMaxSize(200, 100);
        btnDashboard.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnDashboard.setPadding(new Insets(20, 40, 20, 40));
        btnDashboard.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        VBox.setMargin(btnDashboard, new Insets(30, 0, 50, 0));

        component.getChildren().add(btnDashboard);
        
        btnDashboard.setOnAction(e -> {
            // Deselect previous button
            component.getChildren().stream().filter(o -> (o instanceof Button)).forEachOrdered(button -> {
                button.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), false);
            });

            btnDashboard.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
            
            // Change page
            main.root.setCenter(new Dashboard(main).getComponent());
        });
    }
    
    private interface PageFactory {
        public Page createPage();
    }
    
    // Add a button to the menu to change pages
    private void addMenuButton(String name, PageFactory factory, boolean gap) {
        Button menuButton = new Button(name);
        menuButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        menuButton.setMaxSize(Double.MAX_VALUE, 25);
        
        if (gap) VBox.setMargin(menuButton, new Insets(0, 0, 20, 0));
        
        component.getChildren().add(menuButton);
        
        menuButton.setOnAction(e -> {
            // Deselect previous button
            component.getChildren().stream().filter((o) -> (o instanceof Button)).forEach(button -> {
                button.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), false);
            });

            menuButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
            
            // Change page
            main.root.setCenter(factory.createPage().getComponent());
        });
    }
    
    // Add a button to the menu to save data
    private void addSaveButton() {
        // Custom GSON Serialization
        Gson gsonClass = GsonFactory.getGsonClass();
        Gson gsonHomeform = GsonFactory.getGsonHomeform();
        Gson gsonStudent = GsonFactory.getGsonStudent();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        Button btnSave = new Button("Save to Database");
        VBox.setMargin(btnSave, new Insets(40, 0, 0, 0));
        btnSave.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnSave.setMaxSize(Double.MAX_VALUE, 25);

        component.getChildren().add(btnSave);
        
        // Save Button Event Handler
        btnSave.setOnAction(e -> {
            // Make folders (if not already exists)
            main.recordsPath.mkdir();
            main.gradesPath.mkdir();
            main.coursesPath.mkdir();
            main.classesPath.mkdir();
            main.homeformsPath.mkdir();
            main.studentsPath.mkdir();
            
            try {
                // Display output label
                if (!component.getChildren().contains(lblOutput))
                    component.getChildren().add(lblOutput);
                
                lblOutput.setText("Saving...");
                main.showVanish(lblOutput, 2);

                // Purge files that are no longer used
                while (!main.toPurge.isEmpty()) {
                    String path = main.toPurge.poll();
                    System.out.println("Purging: " + path);
                    new File(path).delete();
                }

                // Save Grades
                for (Grade grade : main.grades) {
                    FileWriter writer = new FileWriter(main.gradesPath.toString() + "/" + grade.getValue() + ".json");
                    gson.toJson(grade, writer);
                    writer.close();
                }
                // Save Courses
                for (Course course : main.courses) {
                    FileWriter writer = new FileWriter(main.coursesPath.toString() + "/" + course.getCode() + ".json");
                    gson.toJson(course, writer);
                    writer.close();
                }
                // Save Classes
                for (simpleschools.objects.Class cl : main.classes) {
                    FileWriter writer = new FileWriter(main.classesPath.toString() + "/" + cl.getName() + ".json");
                    gsonClass.toJson(cl, writer);
                    writer.close();
                }
                // Save Homeforms
                for (Homeform hf : main.homeforms) {
                    FileWriter writer = new FileWriter(main.homeformsPath.toString() + "/" + hf.getCode() + ".json");
                    gsonHomeform.toJson(hf, writer);
                    writer.close();
                }
                // Save Students
                for (Student student : main.students) {
                    FileWriter writer = new FileWriter(main.studentsPath.toString() + "/" + student.getStudentId() + ".json");
                    gsonStudent.toJson(student, writer);
                    writer.close();
                }

                // Display output label
                lblOutput.setText("Saved");
            } catch (IOException ex) {
                lblOutput.setText("Error while saving!");
                System.out.println(ex);
            }
        });
    }
    
}