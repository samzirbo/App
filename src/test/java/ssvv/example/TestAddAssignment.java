package ssvv.example;

import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.*;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAddAssignment {
    public static StudentXMLRepository repoStudent;
    public static StudentValidator validatorStudent = new StudentValidator();
    public static TemaXMLRepository repoTema;
    public static TemaValidator validatorAssignment = new TemaValidator();
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

    public static void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }

    @BeforeEach
    public void setUp() {
        createFile("IO/test_add_assignment_student.xml");
        createFile("IO/test_add_assignment_assignment.xml");

        repoStudent = new StudentXMLRepository(validatorStudent, "IO/test_add_assignment_student.xml");
        repoTema = new TemaXMLRepository(validatorAssignment, "IO/test_add_assignment_assignment.xml");

        service = new Service(repoStudent, repoTema, null);
    }



    @AfterEach
    public void cleanUp() {
        deleteFile("IO/test_add_assignment_student.xml");
        deleteFile("IO/test_add_assignment_assignment.xml");
    }

    // Test case 1
    @Test
    public void testAllValidInputs() {
        setUp();
        Tema tema = new Tema("10", "descr1", 10, 8);
        assertDoesNotThrow(() -> validatorAssignment.validate(tema));

        int result = service.saveTema("10", "descr1", 10, 8);
        assertEquals(1, result);
    }

    // Test case 2
    @Test
    public void testEmptyID() {
        setUp();
        Tema tema = new Tema("", "descr1", 10, 8);
        assertThrows(ValidationException.class, () -> validatorAssignment.validate(tema));
    }


}
