package simpleschools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import simpleschools.gui.Dashboard;
import simpleschools.gui.Menu;
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

    // Root component
    public final BorderPane root = new BorderPane();
    
    // ArrayList data structures
    public final ArrayList<Student> students = new ArrayList<>();
    public final ArrayList<Grade> grades = new ArrayList<>();
    public final ArrayList<Class> classes = new ArrayList<>();
    public final ArrayList<Course> courses = new ArrayList<>();
    public final ArrayList<Homeform> homeforms = new ArrayList<>();

    // Store ids to prevent generating duplicates
    public final HashSet<Integer> ids = new HashSet<>();

    public final LinkedList<String> toPurge = new LinkedList<>();

    // Records paths
    public final File recordsPath = new File(System.getProperty("user.dir") + "/records");
    
    public final File gradesPath = new File(recordsPath.getPath() + "/grades");
    public final File coursesPath = new File(recordsPath.getPath() + "/courses");
    public final File classesPath = new File(recordsPath.getPath() + "/classes");
    public final File homeformsPath = new File(recordsPath.getPath() + "/homeforms");
    public final File studentsPath = new File(recordsPath.getPath() + "/students");
    
    @Override
    public void start(Stage primaryStage) {
        readData();

        // Initialize menu
        Menu menu = new Menu(this);
        
        // Root pane
        root.setLeft(menu.getComponent());
        root.setCenter(new Dashboard(this).getComponent());
        root.setId("page");

        // Scene
        Scene scene = new Scene(root, 1075, 790);
        scene.getStylesheets().add("simpleschools/css/styles.css");

        // Stage
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1075);
        primaryStage.setMinHeight(790);
        primaryStage.setTitle("SimpleSchools");
        primaryStage.show();
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
        } catch (JsonIOException | JsonSyntaxException | IOException ex) {
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
                    if (student.getLastName().toLowerCase().contains(key) || student.getFirstName().toLowerCase().contains(key) || student.getMiddleName().toLowerCase().contains(key))
                        result.add(student);
                    break;
                case STUDENT_ID:
                    if (String.valueOf(student.getStudentId()).toLowerCase().contains(key))
                        result.add(student);
                    break;
                case GRADE:
                    if (String.valueOf(student.getGrade().getValue()).contains(key))
                        result.add(student);
                    break;
                case HOMEFORM:
                    if (student.getHomeform().getCode().toLowerCase().contains(key))
                        result.add(student);
                    break;
                case CLASS:
                    for (Class cl : this.getStudentClasses(student))
                        if (cl.getName().toLowerCase().contains(key))
                            result.add(student);
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
                    if (c.getName().toLowerCase().contains(key))
                        result.add(c);
                    break;
                case COURSE:
                    if (c.getCourse().getName().toLowerCase().contains(key))
                        result.add(c);
                    break;
                case GRADE:
                    if (String.valueOf(c.getGrade().getValue()).contains(key))
                        result.add(c);
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
                    if (c.getName().toLowerCase().contains(key))
                        result.add(c);
                    break;
                case COURSE_CODE:
                    if (c.getCode().toLowerCase().contains(key))
                        result.add(c);
                    break;
                case GRADE:
                    if (String.valueOf(c.getGrade().getValue()).contains(key))
                        result.add(c);
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
                    if (hf.getCode().toLowerCase().contains(key))
                        result.add(hf);
                    break;
                case CLASS:
                    if (hf.getClassroom().getName().toLowerCase().contains(key))
                        result.add(hf);
                    break;
                case GRADE:
                    if (String.valueOf(hf.getClassroom().getCourse().getGrade().getValue()).contains(key))
                        result.add(hf);
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

        for (Grade grade : grades)
            if (String.valueOf(grade.getValue()).contains(key))
                result.add(grade);

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
