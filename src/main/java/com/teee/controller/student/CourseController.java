package com.teee.controller.student;

import com.teee.domain.returnClass.Result;

/**
 * @author Xu ZhengTao
 */
public interface CourseController {
    Result addCourse(String token, int cid);
    Result getMyCourses(String token);

}
