package mthree.com.fullstackschool.service;

import mthree.com.fullstackschool.dao.TeacherDao;
import mthree.com.fullstackschool.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherServiceInterface {

    //YOUR CODE STARTS HERE
    private final TeacherDao teacherDao;

    private final String TEACHER_NOT_FOUND = "Teacher Not Found";

    @Autowired
    public TeacherServiceImpl(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    //YOUR CODE ENDS HERE

    public List<Teacher> getAllTeachers() {
        //YOUR CODE STARTS HERE
        return teacherDao.getAllTeachers();
        //YOUR CODE ENDS HERE
    }

    public Teacher getTeacherById(int id) {
        //YOUR CODE STARTS HERE

        try {
            return teacherDao.findTeacherById(id);
        } catch (DataAccessException e) {
            Teacher errorTeacher = new Teacher();
            errorTeacher.setTeacherFName(TEACHER_NOT_FOUND);
            errorTeacher.setTeacherLName(TEACHER_NOT_FOUND);
            return errorTeacher;
        }
        //YOUR CODE ENDS HERE
    }

    public Teacher addNewTeacher(Teacher teacher) {
        //YOUR CODE STARTS HERE

        // if neither first nor last name are blank, then pass through to dao add
        if (!teacher.getTeacherFName().isBlank() && !teacher.getTeacherLName().isBlank()) {
            return teacherDao.createNewTeacher(teacher);
        }

        // otherwise update teacher according to blank fname, lname
        if (teacher.getTeacherFName().isBlank()) {
            teacher.setTeacherFName("First Name blank, teacher NOT added");
        }
        if (teacher.getTeacherLName().isBlank()) {
            teacher.setTeacherLName("Last Name blank, teacher NOT added");
        }

        return teacher;

        //YOUR CODE ENDS HERE
    }

    public Teacher updateTeacherData(int id, Teacher teacher) {
        //YOUR CODE STARTS HERE

        // if IDs don't match, return teacher with error fName and lName
        if (id != teacher.getTeacherId()) {
            final String IDS_DO_NOT_MATCH = "IDs do not match, teacher not updated";
            teacher.setTeacherFName(IDS_DO_NOT_MATCH);
            teacher.setTeacherLName(IDS_DO_NOT_MATCH);
            return teacher;
        }

        // otherwise IDs match, update dao
        teacherDao.updateTeacher(teacher);
        return teacher;
        //YOUR CODE ENDS HERE
    }

    public void deleteTeacherById(int id) {
        //YOUR CODE STARTS HERE
        teacherDao.deleteTeacher(id);

        //YOUR CODE ENDS HERE
    }
}
