package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.StudentMapper;
import mthree.com.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Student createNewStudent(Student student) {
        //YOUR CODE STARTS HERE
        final String INSERT_STUDENT = "INSERT INTO student(fName, lName) VALUES (?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(INSERT_STUDENT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, student.getStudentFirstName());
            statement.setString(2, student.getStudentLastName());
            return statement;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            student.setStudentId(key.intValue());
        }

        return student;

        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE
        final String SELECT_ALL_STUDENTS = "SELECT * FROM student";

        return jdbcTemplate.query(SELECT_ALL_STUDENTS, new StudentMapper());
        //YOUR CODE ENDS HERE
    }

    @Override
    public Student findStudentById(int id) {
        //YOUR CODE STARTS HERE
        final String SELECT_STUDENT_BY_ID = "SELECT * FROM student WHERE sid = ?";

        // throws data access exception - which the service will handle.
        // Not declaring it in method title as we aren't to change the interfaces.
        return jdbcTemplate.queryForObject(SELECT_STUDENT_BY_ID, new StudentMapper(), id);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateStudent(Student student) {
        //YOUR CODE STARTS HERE
        // don't need to edit entry in the course_student table
        // because student contains no data relating to the courses it is in.
        final String UPDATE_STUDENT = "UPDATE student SET fName = ?, lName = ? WHERE sid = ?";

        jdbcTemplate.update(UPDATE_STUDENT,
                student.getStudentFirstName(),
                student.getStudentLastName(),
                student.getStudentId());

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudent(int id) {
        //YOUR CODE STARTS HERE
        final String DELETE_STUDENT_FROM_COURSE_STUDENT = "DELETE FROM course_student WHERE student_id = ?";

        jdbcTemplate.update(DELETE_STUDENT_FROM_COURSE_STUDENT, id);

        final String DELETE_STUDENT = "DELETE FROM student WHERE sid = ?";
        jdbcTemplate.update(DELETE_STUDENT, id);

        //YOUR CODE ENDS HERE
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE
        final String ADD_COURSE_STUDENT = "INSERT INTO course_student(student_id, course_id) VALUES (?,?)";

        jdbcTemplate.update(ADD_COURSE_STUDENT, studentId, courseId);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE
        // just remove the row with matching student and course IDs
        final String REMOVE_COURSE_STUDENT = "DELETE FROM course_student WHERE student_id = ? AND course_id = ?";

        jdbcTemplate.update(REMOVE_COURSE_STUDENT, studentId, courseId);
        //YOUR CODE ENDS HERE
    }
}
