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
    private StudentXMLRepository repo;
    private StudentValidator validator;
    private Service service;

    @BeforeAll
    static void createXMLFile() {
        File file = new File("IO/test_add_student.xml");
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
    void setup() {
        this.validator = new StudentValidator();
        this.repo = new StudentXMLRepository(validator, "IO/test_add_student.xml");
        this.service = new Service(this.repo, null, null);
    }

    @AfterAll
    static void removeXML() {
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
        assertThrows(ValidationException.class, () -> this.validator.validate(student));
    }

    @Test
    public void testService() {
        int result = service.saveStudent("1", "Andrei", 935);
        assertEquals(1, result);
        result = service.saveStudent("1", "Sam", 937);
        assertEquals(0, result);

    }



}
