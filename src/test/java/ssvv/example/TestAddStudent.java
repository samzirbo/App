package ssvv.example;

import domain.Student;
import org.junit.jupiter.api.*;
import repository.StudentXMLRepository;
import service.Service;
import validation.StudentValidator;
import validation.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAddStudent {
    public static StudentXMLRepository repo;
    public static StudentValidator validator = new StudentValidator();
    public static Service service;

    public static void createFile(String filename) {
        File file = new File(filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<inbox>\n" +
                    "\n" +
                    "</inbox>");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    @BeforeEach
    public void setUp() {
        createFile("IO/test_add_student.xml");
        
        repo = new StudentXMLRepository(validator, "IO/test_add_student.xml");
        service = new Service(repo, null, null);
    }

    @AfterEach
    public void cleanUp() {
        File file = new File("IO/test_add_student.xml");
        if (file.exists()) {
            file.delete();
        }
    }

    // Test case 1
    @Test
    public void testAllValidInputs() {
        setUp();
        Student student = new Student("id1", "maria", 935);
        assertDoesNotThrow(() -> validator.validate(student));

        int result = service.saveStudent("id1", "maria", 935);
        assertEquals(1, result);
    }

    // Test case 2
    @Test
    public void testEmptyID() {
        Student student = new Student("", "maria", 935);
        assertThrows(ValidationException.class, () -> validator.validate(student));
    }

    // Test case 3
    @Test
    public void testNullID() {
        Student student = new Student(null, "", 935);
        assertThrows(ValidationException.class, () -> validator.validate(student));
    }

    // Test case 4
    @Test
    public void testIDAlreadyExists() {
        setUp();
        Student student = new Student("id1", "maria", 935);
        assertDoesNotThrow(() -> validator.validate(student));

        int result = service.saveStudent("id1", "maria", 935);
        assertEquals(1, result);

        Student student2 = new Student("id1", "maria", 935);
        assertDoesNotThrow(() -> validator.validate(student2));

        result = service.saveStudent("id1", "maria", 935);
        assertEquals(0, result);
    }

    // Test case 5
    @Test
    public void testEmptyName() {
        Student student = new Student("id2", "", 935);
        assertThrows(ValidationException.class, () -> validator.validate(student));
    }

    // Test case 6
    @Test
    public void testNameNull() {
        Student student = new Student("id3", null, 935);
        assertThrows(ValidationException.class, () -> validator.validate(student));
    }

    // Test case 7
    @Test
    public void testGroupTooBig() {
        Student student = new Student("id4", "maria", 1000);
        assertThrows(ValidationException.class, () -> validator.validate(student));
    }

    // Test case 8
    @Test
    public void testGroupNegative() {
        Student student = new Student("id5", "maria", -935);
        assertThrows(ValidationException.class, () -> validator.validate(student));
    }

    // Test case 9
    @Test
    public void testGroupZero() {
        Student student = new Student("id5", "maria", 0);
        assertThrows(ValidationException.class, () -> validator.validate(student));
    }






}
