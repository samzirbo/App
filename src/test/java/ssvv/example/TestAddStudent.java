package ssvv.example;

import domain.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.StudentXMLRepository;
import service.Service;
import validation.StudentValidator;
import validation.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAddStudent {
    public static StudentXMLRepository repo;
    public static StudentValidator validator;
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

    @BeforeAll
    public static void setUp() {
        createFile("IO/test_add_student.xml");

        validator = new StudentValidator();
        repo = new StudentXMLRepository(validator, "IO/test_add_student.xml");
        service = new Service(repo, null, null);
    }

    @AfterAll
    public static void cleanUp() {
        File file = new File("IO/test_add_student.xml");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testValidation() {
        int result = service.saveStudent("2", "Maria", 935);
        assertEquals(1, result);
        Student student = new Student(null, "George", 935);
        assertThrows(ValidationException.class, () -> validator.validate(student));
    }

    @Test
    public void testService() {
        int result = service.saveStudent("1", "Andrei", 935);
        assertEquals(1, result);
        result = service.saveStudent("1", "Sam", 937);
        assertEquals(0, result);

    }



}
