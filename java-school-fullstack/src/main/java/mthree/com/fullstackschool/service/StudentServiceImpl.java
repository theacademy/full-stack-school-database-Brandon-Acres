package mthree.com.fullstackschool.service;

import mthree.com.fullstackschool.dao.StudentDao;
import mthree.com.fullstackschool.dao.CourseDao;
import mthree.com.fullstackschool.model.Course;
import mthree.com.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentServiceInterface {

    //YOUR CODE STARTS HERE
    private final StudentDao studentDao;

    private final CourseServiceInterface courseService;

    private final String STUDENT_NOT_FOUND = "Student Not Found";

    public StudentServiceImpl(StudentDao studentDao) {
        this(studentDao, new NoOpCourseService());

    }

    static private class NoOpCourseService implements CourseServiceInterface {
        @Override
        public List<Course> getAllCourses() {
            return List.of();
        }

        @Override
        public Course getCourseById(int id) {
            return null;
        }

        @Override
        public Course addNewCourse(Course course) {
            return null;
        }

        @Override
        public Course updateCourseData(int id, Course course) {
            return null;
        }

        @Override
        public void deleteCourseById(int id) {

        }
    }


    @Autowired
    public StudentServiceImpl(StudentDao studentDao, CourseServiceInterface courseService) {
        this.studentDao = studentDao;
        this.courseService = courseService;
    }

    //YOUR CODE ENDS HERE

    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE
        return studentDao.getAllStudents();
        //YOUR CODE ENDS HERE
    }

    public Student getStudentById(int id) {
        //YOUR CODE STARTS HERE
        try {
            return studentDao.findStudentById(id);
        } catch (DataAccessException e) {
            Student newStudent = new Student();
            newStudent.setStudentFirstName(STUDENT_NOT_FOUND);
            newStudent.setStudentLastName(STUDENT_NOT_FOUND);
            return newStudent;
        }

        //YOUR CODE ENDS HERE
    }

    public Student addNewStudent(Student student) {
        //YOUR CODE STARTS HERE

        // if neither first name nor last name are blank, return pass through
        if (!student.getStudentFirstName().isBlank() && !student.getStudentLastName().isBlank()) {
            return studentDao.createNewStudent(student);
        }

        // otherwise one of the two are blank
        if (student.getStudentFirstName().isBlank()) {
            student.setStudentFirstName("First Name blank, student NOT added");
        }
        if (student.getStudentLastName().isBlank()) {
            student.setStudentLastName("Last Name blank, student NOT added");
        }

        return student;

        //YOUR CODE ENDS HERE
    }

    public Student updateStudentData(int id, Student student) {
        //YOUR CODE STARTS HERE
        // if IDs don't match, don't update, return error student
        if (id != student.getStudentId()) {
            final String IDS_DO_NOT_MATCH = "IDs do not match, student not updated";
            student.setStudentFirstName(IDS_DO_NOT_MATCH);
            student.setStudentLastName(IDS_DO_NOT_MATCH);
            return student;
        }

        // otherwise ids match - pass through to studentDao
        studentDao.updateStudent(student);
        return student;
        //YOUR CODE ENDS HERE
    }

    public void deleteStudentById(int id) {
        //YOUR CODE STARTS HERE
        // pass through
        studentDao.deleteStudent(id);
        //YOUR CODE ENDS HERE
    }

    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE
        Student studentFromDao = getStudentById(studentId);

        if (studentFromDao.getStudentFirstName().equals(STUDENT_NOT_FOUND)) {
            System.out.println("Student not found");
        } else if (courseService.getCourseById(courseId).getCourseName().equals("Course Not Found")) {
            System.out.println("Course not found");
        } else {
            // delete student from course
            studentDao.deleteStudentFromCourse(studentId, courseId);
            System.out.printf("Student: %d deleted from course: %d", studentId, courseId);
        }
        //YOUR CODE ENDS HERE
    }

    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE
        Student studentFromDao = getStudentById(studentId);

        if (studentFromDao.getStudentFirstName().equals(STUDENT_NOT_FOUND)) {
            System.out.println("Student not found");
        } else if (courseService.getCourseById(courseId).getCourseName().equals("Course Not Found")) {
            System.out.println("Course not found");
        }

        // otherwise delete and catch exception
        try {
            studentDao.addStudentToCourse(studentId, courseId);
            System.out.printf("Student: %d added to course: %d", studentId, courseId);
        } catch (DataAccessException e) {
            System.out.printf("Student: %d already enrolled in course: %d", studentId, courseId);
        }

        //YOUR CODE ENDS HERE
    }
}
