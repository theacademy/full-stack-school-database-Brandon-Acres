package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.TeacherMapper;
import mthree.com.fullstackschool.model.Teacher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TeacherDaoImpl implements TeacherDao {

    private final JdbcTemplate jdbcTemplate;

    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Teacher createNewTeacher(Teacher teacher) {
        //YOUR CODE STARTS HERE
        final String INSERT_TEACHER = "INSERT INTO teacher(tFName, tLName, dept) VALUES (?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(INSERT_TEACHER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, teacher.getTeacherFName());
            statement.setString(2, teacher.getTeacherLName());
            statement.setString(3, teacher.getDept());
            return statement;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            teacher.setTeacherId(key.intValue());
        }

        return teacher;
        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Teacher> getAllTeachers() {
        //YOUR CODE STARTS HERE
        final String SELECT_ALL_TEACHERS = "SELECT * FROM teacher";

        return jdbcTemplate.query(SELECT_ALL_TEACHERS, new TeacherMapper());

        //YOUR CODE ENDS HERE
    }

    @Override
    public Teacher findTeacherById(int id) {
        //YOUR CODE STARTS HERE
        final String SELECT_TEACHER_BY_ID = "SELECT * FROM teacher WHERE tid = ?";

        // throws data access exception - which the service will handle.
        // Not declaring it in method title as we aren't to change the interfaces.
        return jdbcTemplate.queryForObject(SELECT_TEACHER_BY_ID, new TeacherMapper(), id);

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateTeacher(Teacher t) {
        //YOUR CODE STARTS HERE
        // don't change id therefore, don't change FK references
        final String UPDATE_TEACHER = "UPDATE teacher SET tFName = ?, tLName = ?, dept = ? WHERE "
                + "tid = ?";

        jdbcTemplate.update(UPDATE_TEACHER,
                t.getTeacherFName(),
                t.getTeacherLName(),
                t.getDept(),
                t.getTeacherId());

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteTeacher(int id) {
        //YOUR CODE STARTS HERE

        // first delete foreign key dependency course_student entry referencing course
        // that has the teacher we are removing.
        final String REMOVE_COURSE_STUDENT_BY_TEACHER = "DELETE FROM course_student WHERE "
                + "course_id IN "
                + "(SELECT cid FROM course WHERE teacherId = ?)";

        jdbcTemplate.update(REMOVE_COURSE_STUDENT_BY_TEACHER, id);

        // then remove courses referencing teacher
        final String REMOVE_COURSE_BY_TEACHER = "DELETE FROM course WHERE teacherId = ?";
        jdbcTemplate.update(REMOVE_COURSE_BY_TEACHER, id);

        // then remove teacher
        final String REMOVE_TEACHER_BY_ID = "DELETE FROM teacher WHERE tid = ?";
        jdbcTemplate.update(REMOVE_TEACHER_BY_ID, id);

        //YOUR CODE ENDS HERE
    }
}
