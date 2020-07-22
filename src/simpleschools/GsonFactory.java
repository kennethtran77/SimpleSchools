package simpleschools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import simpleschools.objects.Course;
import simpleschools.objects.Homeform;
import simpleschools.objects.Student;

/**
 *
 * @author Kenneth Tran
 */
public final class GsonFactory {
 
    private GsonFactory() { }
    
    /**
     * Get a custom Gson object for writing Classes
     * @return Gson object
     */
    public static Gson getGsonClass() {
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
        
        // Register Course and Student serializer
        gsonBuilder.registerTypeAdapter(Course.class, courseSerializer);
        gsonBuilder.registerTypeAdapter(new TypeToken<List<Student>>() {}.getType(), studentListSerializer);
        
        // Create gson object with gsonBuilder
        Gson classGson = gsonBuilder.setPrettyPrinting().create();
        
        return classGson;
    }
    
    /**
     * Get a custom Gson object for writing Homeforms
     * @return Gson object
     */
    public static Gson getGsonHomeform() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Class Json Serializer
        JsonSerializer<simpleschools.objects.Class> classSerializer = (simpleschools.objects.Class src, Type typeOfSrc, JsonSerializationContext context) -> {
            JsonObject jsonClass = new JsonObject();
            jsonClass.addProperty("className", src.getName());
            return jsonClass;
        };

        // Register Class serializer
        gsonBuilder.registerTypeAdapter(simpleschools.objects.Class.class, classSerializer);
        
        // Create gson object with gsonBuilder
        Gson gsonHomeform = gsonBuilder.setPrettyPrinting().create();
        
        return gsonHomeform;
    }
    
    /**
     * Get a custom Gson object for writing Students
     * @return Gson object
     */
    public static Gson getGsonStudent() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        
        // Homeform Json Serializer
        JsonSerializer<Homeform> homeformSerializer = (Homeform src, Type typeOfSrc, JsonSerializationContext context) -> {
            JsonObject jsonHomeform = new JsonObject();
            jsonHomeform.addProperty("code", src.getCode());
            return jsonHomeform;
        };

        // Register Homeform serializer
        gsonBuilder.registerTypeAdapter(Homeform.class, homeformSerializer);
        
        // Create gson object with gsonBuilder
        Gson gsonStudent = gsonBuilder.setPrettyPrinting().create();
        
        return gsonStudent;
    }
    
}