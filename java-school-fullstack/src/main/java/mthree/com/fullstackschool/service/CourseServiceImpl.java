package mthree.com.fullstackschool.service;

import mthree.com.fullstackschool.dao.CourseDao;
import mthree.com.fullstackschool.dao.CourseDaoImpl;
import mthree.com.fullstackschool.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseServiceInterface {

    //YOUR CODE STARTS HERE
    private final CourseDao courseDao;

    private final String COURSE_NOT_FOUND = "Course Not Found";

    @Autowired
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }


    //YOUR CODE ENDS HERE

    public List<Course> getAllCourses() {
        //YOUR CODE STARTS HERE

        return courseDao.getAllCourses();

        //YOUR CODE ENDS HERE
    }

    public Course getCourseById(int id) {
        //YOUR CODE STARTS HERE

        try {
            return courseDao.findCourseById(id);
        } catch (DataAccessException e) {
            Course newCourse = new Course();
            newCourse.setCourseName(COURSE_NOT_FOUND);
            newCourse.setCourseDesc(COURSE_NOT_FOUND);
            return newCourse;
        }

        //YOUR CODE ENDS HERE
    }

    public Course addNewCourse(Course course) {
        //YOUR CODE STARTS HERE

        // if neither are blank, add
        if (!course.getCourseName().isBlank() && !course.getCourseDesc().isBlank()) {
            return courseDao.createNewCourse(course);
        }

        // otherwise set corresponding blank entries to error values
        if (course.getCourseName().isBlank()) {
            course.setCourseName("Name blank, course NOT added");
        }
        if (course.getCourseDesc().isBlank()) {
            course.setCourseDesc("Description blank, course NOT added");
        }
        return course;

        //YOUR CODE ENDS HERE
    }

    public Course updateCourseData(int id, Course course) {
        //YOUR CODE STARTS HERE

        // if id not equal to course ID, then set name and description to error message
        if (id != course.getCourseId()) {
            course.setCourseName("IDs do not match, course not updated");
            course.setCourseDesc("IDs do not match, course not updated");
            return course;
        }

        // otherwise ids match, update the course
        courseDao.updateCourse(course);
        return course;
        //YOUR CODE ENDS HERE
    }

    public void deleteCourseById(int id) {
        //YOUR CODE STARTS HERE
        // delete course
        courseDao.deleteCourse(id);

        // output log to console
        System.out.printf("Course ID: %d deleted", id);
        //YOUR CODE ENDS HERE
    }
}
