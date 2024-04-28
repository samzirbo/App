package ssvv.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Pair;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaRepository;
import ssvv.example.repository.NotaXMLRepository;
import ssvv.example.repository.StudentXMLRepository;
import ssvv.example.repository.TemaXMLRepository;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;
import ssvv.example.validation.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestIncrementalIntegration {
    public static StudentXMLRepository repoStudent;
    public static StudentValidator validatorStudent = new StudentValidator();
    public static TemaXMLRepository repoTema;
    public static TemaValidator validatorAssignment = new TemaValidator();
    public static NotaXMLRepository repoNota;
    public static NotaValidator validatorNota = new NotaValidator();
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
        createFile("IO/test_incremental_integration_student.xml");
        createFile("IO/test_incremental_integration_assignment.xml");
        createFile("IO/test_incremental_integration_grade.xml");

        repoStudent = new StudentXMLRepository(validatorStudent, "IO/test_incremental_integration_student.xml");
        repoTema = new TemaXMLRepository(validatorAssignment, "IO/test_incremental_integration_assignment.xml");
        repoNota = new NotaXMLRepository(validatorNota, "IO/test_incremental_integration_grade.xml");

        service = new Service(repoStudent, repoTema, repoNota);
    }



    @AfterEach
    public void cleanUp() {
        deleteFile("IO/test_integration_student.xml");
        deleteFile("IO/test_integration_assignment.xml");
        deleteFile("IO/test_integration_grade.xml");
    }

    @Test
    public void testAddStudent() {
        setUp();
        Student student = new Student("", "maria", 935);
        assertThrows(ValidationException.class, () -> validatorStudent.validate(student));
    }

    @Test
    public void testAddAssignment() {
        setUp();
        Tema tema = new Tema("", "descr1", 10, 8);
        assertThrows(ValidationException.class, () -> validatorAssignment.validate(tema));
    }

    @Test
    public void testAddStudentAndAssignment(){
        setUp();
        Student student = new Student("", "maria", 935);
        assertThrows(ValidationException.class, () -> validatorStudent.validate(student));

        Tema tema = new Tema("", "descr1", 10, 8);
        assertThrows(ValidationException.class, () -> validatorAssignment.validate(tema));

    }

    @Test
    public void testAddStudentAndAssignmentAndGrade(){
        setUp();
        Student student = new Student("", "maria", 935);
        assertThrows(ValidationException.class, () -> validatorStudent.validate(student));

        Tema tema = new Tema("", "descr1", 10, 8);
        assertThrows(ValidationException.class, () -> validatorAssignment.validate(tema));

        Nota nota = new Nota(new Pair("1", "1"), 11, 10, "feedback");
        assertThrows(ValidationException.class, () -> validatorNota.validate(nota));

    }


}
