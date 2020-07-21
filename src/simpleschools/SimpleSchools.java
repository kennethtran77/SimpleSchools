package simpleschools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import simpleschools.gui.Page;
import simpleschools.gui.Dashboard;
import simpleschools.gui.RegisterClass;
import simpleschools.gui.RegisterCourse;
import simpleschools.gui.RegisterGrade;
import simpleschools.gui.RegisterHomeform;
import simpleschools.gui.RegisterStudent;
import simpleschools.gui.SearchEditClass;
import simpleschools.gui.SearchEditCourse;
import simpleschools.gui.SearchEditGrade;
import simpleschools.gui.SearchEditHomeform;
import simpleschools.gui.SearchEditStudent;
import simpleschools.objects.Class;
import simpleschools.objects.Course;
import simpleschools.objects.Entry;
import simpleschools.objects.Grade;
import simpleschools.objects.Homeform;
import simpleschools.objects.Student;

/**
 * Main class
 *
 * @author Kenneth Tran
 */
public class SimpleSchools extends Application {

    private VBox menu;
    private BorderPane root;
    
    // ArrayList data structures
    public ArrayList<Student> students = new ArrayList<>();
    public ArrayList<Grade> grades = new ArrayList<>();
    public ArrayList<Class> classes = new ArrayList<>();
    public ArrayList<Course> courses = new ArrayList<>();
    public ArrayList<Homeform> homeforms = new ArrayList<>();

    // Store ids to prevent generating duplicates
    public HashSet<Integer> ids = new HashSet<>();

    public LinkedList<String> toPurge = new LinkedList<>();

    // Records paths
    public File recordsPath = new File(System.getProperty("user.dir") + "/records");
    
    public File gradesPath = new File(recordsPath.getPath() + "/grades");
    public File coursesPath = new File(recordsPath.getPath() + "/courses");
    public File classesPath = new File(recordsPath.getPath() + "/classes");
    public File homeformsPath = new File(recordsPath.getPath() + "/homeforms");
    public File studentsPath = new File(recordsPath.getPath() + "/students");
    
    @Override
    public void start(Stage primaryStage) {
        readData();

        // Menu component
        menu = new VBox(5);
        menu.setId("menu");

        // Root pane
        root = new BorderPane();
        root.setLeft(menu);
        root.setCenter(new Dashboard(this).getComponent());
        root.setId("page");

        // < --- Dashboard Button
        Button btnDashboard = new Button("Dashboard");
        btnDashboard.setMinSize(200, 100);
        btnDashboard.setMaxSize(200, 100);
        btnDashboard.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        btnDashboard.setPadding(new Insets(20, 40, 20, 40));
        btnDashboard.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        VBox.setMargin(btnDashboard, new Insets(30, 0, 50, 0));

        btnDashboard.setOnAction(e -> {
            for (Node n : menu.getChildren()) {
                if (n instanceof Button) {
                    n.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), false);
                }
            }

            btnDashboard.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
            
            // Change page
            root.setCenter(new Dashboard(this).getComponent());
        });
        // ---------------------------------------------------------------- >

        // Operations label
        Label lblOperations = new Label("Operations");
        lblOperations.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblOperations.setMinHeight(50);
        lblOperations.setMaxSize(Double.MAX_VALUE, 50);
        
        menu.getChildren().addAll(btnDashboard, lblOperations);

        // Add menu buttons
        addMenuButton("Register Student", () -> new RegisterStudent(this), false);
        addMenuButton("Search/Edit Student", () -> new SearchEditStudent(this), true);
        addMenuButton("Register Course", () -> new RegisterCourse(this), false);
        addMenuButton("Search/Edit Course", () -> new SearchEditCourse(this), true);
        addMenuButton("Register Homeform", () -> new RegisterHomeform(this), false);
        addMenuButton("Search/Edit Homeform", () -> new SearchEditHomeform(this), true);
        addMenuButton("Register Grade", () -> new RegisterGrade(this), false);
        addMenuButton("Search/Edit Grade", () -> new SearchEditGrade(this), true);
        addMenuButton("Register Class", () -> new RegisterClass(this), false);
        addMenuButton("Search/Edit Class", () -> new SearchEditClass(this), true);

        // Output label
        Label lblOutput = new Label();
        
        // < ----------------------- Custom GSON Serialization

        Gson gsonClassWriter = getClassGsonWriter();
        Gson gsonHomeformWriter = getHomeformGsonWriter();
        Gson gsonStudentWriter = getStudentGsonWriter();
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // ---------------------------------------------------------------- >
        
        // Save button
        Button btnSave = new Button("Save to Database");
        VBox.setMargin(btnSave, new Insets(40, 0, 0, 0));
        btnSave.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnSave.setMaxSize(Double.MAX_VALUE, 25);

        // Save Button Event Handler
        btnSave.setOnAction(e -> {
            // Make folders (if not already exists)
            recordsPath.mkdir();
            gradesPath.mkdir();
            coursesPath.mkdir();
            classesPath.mkdir();
            homeformsPath.mkdir();
            studentsPath.mkdir();
            
            try {
                // Display output label
                if (!menu.getChildren().contains(lblOutput))
                    menu.getChildren().add(lblOutput);
                
                lblOutput.setText("Saving...");
                showVanish(lblOutput, 2);

                // Purge files that are no longer used
                while (!toPurge.isEmpty()) {
                    String path = toPurge.poll();
                    System.out.println("Purging: " + path);
                    new File(path).delete();
                }

                // Save Grades
                for (Grade grade : grades) {
                    FileWriter writer = new FileWriter(gradesPath.toString() + "/" + grade.getValue() + ".json");
                    gson.toJson(grade, writer);
                    writer.close();
                }

                // Save Courses
                for (Course course : courses) {
                    FileWriter writer = new FileWriter(coursesPath.toString() + "/" + course.getCode() + ".json");
                    gson.toJson(course, writer);
                    writer.close();
                }

                // Save Classes
                for (Class cl : classes) {
                    FileWriter writer = new FileWriter(classesPath.toString() + "/" + cl.getName() + ".json");
                    gsonClassWriter.toJson(cl, writer);
                    writer.close();
                }

                // Save Homeforms
                for (Homeform hf : homeforms) {
                    FileWriter writer = new FileWriter(homeformsPath.toString() + "/" + hf.getCode() + ".json");
                    gsonHomeformWriter.toJson(hf, writer);
                    writer.close();
                }

                // Save Students
                for (Student student : students) {
                    FileWriter writer = new FileWriter(studentsPath.toString() + "/" + student.getStudentId() + ".json");
                    gsonStudentWriter.toJson(student, writer);
                    writer.close();
                }

                // Display output label
                lblOutput.setText("Saved");
            } catch (IOException ex) {
                lblOutput.setText("Error while saving!");
                System.out.println(ex);
            }
        });

        // Exit button
        Button btnExit = new Button("Exit");
        btnExit.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnExit.setMaxSize(Double.MAX_VALUE, 25);

        btnExit.setOnAction(e -> {
            Platform.exit();
        });

        // Add buttons
        menu.getChildren().addAll(btnSave, btnExit);

        // < --- Window Setup
        
        // Stage
        Scene scene = new Scene(root, 1075, 790);
        scene.getStylesheets().add("simpleschools/css/styles.css");

        // Stage
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1075);
        primaryStage.setMinHeight(790);
        primaryStage.setTitle("SimpleSchools");
        primaryStage.show();
        
        // --- >
    }
    
    private interface PageFactory {
        public Page createPage();
    }
    
    /**
     * Add a button to the menu to change pages
     * @param name
     * @param page 
     */
    private void addMenuButton(String name, PageFactory factory, boolean gap) {
        Button menuButton = new Button(name);
        menuButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        menuButton.setMaxSize(Double.MAX_VALUE, 25);
        
        if (gap) VBox.setMargin(menuButton, new Insets(0, 0, 20, 0));
        
        menuButton.setOnAction(e -> {
            for (Node n : menu.getChildren()) {
                if (n instanceof Button) {
                    n.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), false);
                }
            }

            menuButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
            
            // Change page
            root.setCenter(factory.createPage().getComponent());
        });
        
        menu.getChildren().add(menuButton);
    }
    
    private Gson getClassGsonWriter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        
        // Course Json Serializer
        JsonSerializer<Course> courseSerializer = (Course src, Type typeOfSrc, JsonSerializationContext context) -> {
            JsonObject jsonCourse = new JsonObject();
            jsonCourse.addProperty("courseCode", src.getCode());
            return jsonCourse;
        };

        // Student List Json Serializer
        JsonSerializer<List<Student>> studentListSerializer = (List<Student> src, Type typeOfSrc, JsonSerializationContext context) -> {
            JsonArray jsonStudentList = new JsonArray();
            src.forEach(student -> jsonStudentList.add(student.getStudentId()));
            return jsonStudentList;
        };
        
        gsonBuilder.registerTypeAdapter(Course.class, courseSerializer);
        gsonBuilder.registerTypeAdapter(new TypeToken<List<Student>>() {}.getType(), studentListSerializer);
        Gson classGsonWriter = gsonBuilder.setPrettyPrinting().create();
        
        return classGsonWriter;
    }
    
    private Gson getHomeformGsonWriter() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Class Json Serializer
        JsonSerializer<Class> classSerializer = (Class src, Type typeOfSrc, JsonSerializationContext context) -> {
            JsonObject jsonClass = new JsonObject();
            jsonClass.addProperty("className", src.getName());
            return jsonClass;
        };

        gsonBuilder.registerTypeAdapter(Class.class, classSerializer);
        Gson gsonHomeformWriter = gsonBuilder.setPrettyPrinting().create();
        
        return gsonHomeformWriter;
    }
    
    private Gson getStudentGsonWriter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        
        // Homeform Json Serializer
        JsonSerializer<Homeform> homeformSerializer = (Homeform src, Type typeOfSrc, JsonSerializationContext context) -> {
            JsonObject jsonHomeform = new JsonObject();
            jsonHomeform.addProperty("code", src.getCode());
            return jsonHomeform;
        };

        gsonBuilder.registerTypeAdapter(Homeform.class, homeformSerializer);
        Gson gsonStudentWriter = gsonBuilder.setPrettyPrinting().create();
        
        return gsonStudentWriter;
    }
    
    /**
     * Reads data from record files
     */
    private void readData() {
        if (!recordsPath.exists()) // Don't do anything if there is no records path directory
            return;
        
        Gson gson = new Gson();
        
        try {
            // Read Grades
            if (gradesPath.exists()) {
                for (File gradeFile : gradesPath.listFiles()) {
                    if (gradeFile.isFile()) {
                        BufferedReader br = new BufferedReader(new FileReader(gradeFile));
                        grades.add(gson.fromJson(br, Grade.class));
                        br.close();
                    }
                }
            }

            // Read Courses
            if (coursesPath.exists()) {
                for (File courseFile : coursesPath.listFiles()) {
                    if (courseFile.isFile()) {
                        BufferedReader br = new BufferedReader(new FileReader(courseFile));
                        Course course = gson.fromJson(br, Course.class);

                        // Make sure the Grade that the Json read actually exists
                        Grade grade = grades.stream().filter(g -> g.getValue() == course.getGrade().getValue()).findAny().orElse(null);

                        if (grade == null)
                            break;

                        // Update Grade
                        course.setGrade(grade);

                        courses.add(course);
                        br.close();
                    }
                }
            }

            // Read Classes
            if (classesPath.exists()) {
                for (File classFile : classesPath.listFiles()) {
                    if (classFile.isFile()) {
                        // Custom Class Deserializer
                        JsonDeserializer<Class> classDeserializer = (JsonElement json, Type typeOfSrc, JsonDeserializationContext context) -> {
                            JsonObject jsonObject = json.getAsJsonObject();

                            // Load Student Ids to Initialize Students Later
                            Queue<Integer> load = new LinkedList<>();
                            jsonObject.get("students").getAsJsonArray().forEach(e -> load.add(e.getAsInt()));

                            Class cl = new Class(courses.stream().filter(c -> c.getCode().equalsIgnoreCase(jsonObject.get("course").getAsJsonObject().get("courseCode").getAsString())).findAny().orElse(null), jsonObject.get("className").getAsString(), load);
                            return cl;
                        };
                        // Create Gson object to read file
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(Class.class, classDeserializer);
                        Gson classGsonReader = gsonBuilder.create();  

                        BufferedReader br = new BufferedReader(new FileReader(classFile));
                        
                        Class cl = classGsonReader.fromJson(br, Class.class);
                        classes.add(cl);
                        
                        br.close();
                    }
                }
            }
            
            // Read Homeforms
            if (homeformsPath.exists()) {
                for (File homeformFile : homeformsPath.listFiles()) {
                    if (homeformFile.isFile()) {
                        BufferedReader br = new BufferedReader(new FileReader(homeformFile));
                        Homeform homeform = gson.fromJson(br, Homeform.class);

                        // Make sure the Class that the Json read actually exists
                        Class cl = classes.stream().filter(c -> c.equals(homeform.getClassroom())).findAny().orElse(null);

                        if (cl == null)
                            break;

                        // Update Class
                        homeform.setClass(cl);
                        homeforms.add(homeform);
                        
                        br.close();
                    }
                }
            }

            // Read Students
            if (studentsPath.exists()) {
                for (File studentFile : studentsPath.listFiles()) {
                    if (studentFile.isFile()) {
                        // < ------- Custom Student Deserializer
                        JsonDeserializer<Student> studentDeserializer = (JsonElement json, Type typeOfSrc, JsonDeserializationContext context) -> {
                            JsonObject jsonObject = json.getAsJsonObject();

                            // Create Student Object
                            Student student = new Student(
                                    jsonObject.get("firstName").getAsString(),
                                    jsonObject.get("middleName").getAsString(),
                                    jsonObject.get("lastName").getAsString(),
                                    jsonObject.get("email").getAsString(),
                                    jsonObject.get("phoneNumber").getAsString(),
                                    homeforms.stream().filter(hf -> hf.getCode().equalsIgnoreCase(jsonObject.get("homeform").getAsJsonObject().get("code").getAsString())).findAny().orElse(null),
                                    jsonObject.get("studentId").getAsInt());

                            // Read Entries HashMap
                            Type entriesType = new TypeToken<Map<String, List<Entry>>>() {}.getType();
                            LinkedTreeMap<String, List<Entry>> entries = gson.fromJson(jsonObject.get("entries").getAsJsonObject(), entriesType);

                            // Insert Entry data for each Class
                            entries.forEach((k, v) -> student.setEntries(classes.stream().filter(cl -> cl.getName().equalsIgnoreCase(k)).findAny().orElse(null), v));

                            return student;
                        };
                        // Create Gson object to read file
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(Student.class, studentDeserializer);
                        Gson gsonStudentReader = gsonBuilder.create();

                        // <--------------------

                        BufferedReader br = new BufferedReader(new FileReader(studentFile));
                        
                        Student student = gsonStudentReader.fromJson(br, Student.class);
                        students.add(student);
                        
                        br.close();
                    }
                }

                classes.forEach(c -> c.loadStudents(this));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    /**
     * Returns a list of classes that a student is enrolled in
     * @param student
     * @return list of classes
     */
    public List<Class> getStudentClasses(Student student) {
        return this.classes.stream().filter(c -> c.containsStudent(student)).collect(Collectors.toList());
    }
    
    
    /**
     * Sort a list of Students
     *
     * @param criteria
     * @param students
     */
    public void sortStudents(Criteria criteria, List<Student> students) {
        Collections.sort(students, (a, b) -> criteria.compare(a, b));
    }

    /**
     * Sort a list of Classes
     *
     * @param criteria
     * @param classes
     */
    public void sortClasses(Criteria criteria, List<Class> classes) {
        Collections.sort(classes, (a, b) -> criteria.compare(a, b));
    }

    /**
     * Sort a list of Courses
     *
     * @param criteria
     * @param courses
     */
    public void sortCourses(Criteria criteria, List<Course> courses) {
        Collections.sort(courses, (a, b) -> criteria.compare(a, b));
    }

    /**
     * Sort a list of Grades
     *
     * @param criteria
     * @param grades
     */
    public void sortGrades(Criteria criteria, List<Grade> grades) {
        Collections.sort(grades, (a, b) -> criteria.compare(a, b));
    }

    /**
     * Sort a list of Homeforms
     *
     * @param criteria
     * @param homeforms
     */
    public void sortHomeform(Criteria criteria, List<Homeform> homeforms) {
        Collections.sort(homeforms, (a, b) -> criteria.compare(a, b));
    }

    /**
     * Search for a given student(s) given a key.
     *
     * @param key
     * @param criteria
     * @return an ArrayList of all students that match this search query
     */
    public List<Student> searchStudent(String key, Criteria criteria) {
        key = key.toLowerCase();

        List<Student> result = new ArrayList<>();

        // Loop through students and add matching students to result ArrayList
        for (Student student : students) {
            switch (criteria) {
                case NAME:
                    if (student.getLastName().toLowerCase().contains(key) || student.getFirstName().toLowerCase().contains(key) || student.getMiddleName().toLowerCase().contains(key)) {
                        result.add(student);
                    }
                    break;
                case STUDENT_ID:
                    if (String.valueOf(student.getStudentId()).toLowerCase().contains(key)) {
                        result.add(student);
                    }
                    break;
                case GRADE:
                    if (String.valueOf(student.getGrade().getValue()).contains(key)) {
                        result.add(student);
                    }
                    break;
                case HOMEFORM:
                    if (student.getHomeform().getCode().toLowerCase().contains(key)) {
                        result.add(student);
                    }
                    break;
                case CLASS:
                    for (Class cl : this.getStudentClasses(student)) {
                        if (cl.getName().toLowerCase().contains(key)) {
                            result.add(student);
                        }
                    }
                    break;
            }
        }

        return result;
    }

    /**
     * Search for a given class(es) given a key.
     *
     * @param key
     * @param criteria
     * @return an ArrayList of all classes that match this search query
     */
    public List<Class> searchClass(String key, Criteria criteria) {
        key = key.toLowerCase();

        List<Class> result = new ArrayList<>();

        // Loop through classes and add matching classes to result ArrayList
        for (Class c : classes) {
            switch (criteria) {
                case NAME:
                    if (c.getName().toLowerCase().contains(key)) {
                        result.add(c);
                    }
                    break;
                case COURSE:
                    if (c.getCourse().getName().toLowerCase().contains(key)) {
                        result.add(c);
                    }
                    break;
                case GRADE:
                    if (String.valueOf(c.getGrade().getValue()).contains(key)) {
                        result.add(c);
                    }
                    break;
            }
        }

        return result;
    }

    /**
     * Search for a given course(s) given a key.
     *
     * @param key
     * @param criteria
     * @return an ArrayList of all courses that match this search query
     */
    public List<Course> searchCourse(String key, Criteria criteria) {
        key = key.toLowerCase();

        List<Course> result = new ArrayList<>();

        // Loop through courses and add matching courses to result ArrayList
        for (Course c : courses) {
            switch (criteria) {
                case NAME:
                    if (c.getName().toLowerCase().contains(key)) {
                        result.add(c);
                    }
                    break;
                case COURSE_CODE:
                    if (c.getCode().toLowerCase().contains(key)) {
                        result.add(c);
                    }
                    break;
                case GRADE:
                    if (String.valueOf(c.getGrade().getValue()).contains(key)) {
                        result.add(c);
                    }
                    break;
            }
        }

        return result;
    }

    /**
     * Search for a given homeform(s) given a key.
     *
     * @param key
     * @param criteria
     * @return an ArrayList of all homeforms that match this search query
     */
    public List<Homeform> searchHomeform(String key, Criteria criteria) {
        key = key.toLowerCase();

        List<Homeform> result = new ArrayList<>();

        for (Homeform hf : homeforms) {
            switch (criteria) {
                case NAME:
                    if (hf.getCode().toLowerCase().contains(key)) {
                        result.add(hf);
                    }
                    break;
                case CLASS:
                    if (hf.getClassroom().getName().toLowerCase().contains(key)) {
                        result.add(hf);
                    }
                    break;
                case GRADE:
                    if (String.valueOf(hf.getClassroom().getCourse().getGrade().getValue()).contains(key)) {
                        result.add(hf);
                    }
                    break;
            }
        }

        return result;
    }

    /**
     * Search for a given grade(s) given a key.
     *
     * @param key
     * @return an ArrayList of all grades that match this search query
     */
    public List<Grade> searchGrade(String key) {
        key = key.toLowerCase();

        List<Grade> result = new ArrayList<>();

        for (Grade grade : grades) {
            if (String.valueOf(grade.getValue()).contains(key)) {
                result.add(grade);
            }
        }

        return result;
    }

    private final HashSet<Node> vanished = new HashSet<>();
    
    /**
     * Makes a component vanish after given seconds
     * @param node
     * @param seconds 
     */
    public void showVanish(Node node, int seconds) {
        if (!vanished.contains(node)) {
            vanished.add(node);
            node.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            
            pause.setOnFinished(e -> {
                node.setVisible(false);
                vanished.remove(node);
            });
            pause.play();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
