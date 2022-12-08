package com.teee.service.HomeWork.Exams;

import com.alibaba.fastjson.JSONObject;
import com.teee.domain.returnClass.BooleanReturn;
import com.teee.domain.works.WorkExamRule;

import java.util.ArrayList;

public interface ExamService {

    BooleanReturn setRuleForExam(int wid, WorkExamRule rule);

    /**
     * Submit:{
     *     uid:
     *     wid:
     *     subFace:
     *
     *     +
     * }
     **/
    BooleanReturn checkRule(JSONObject submit, ArrayList<String> rules);

}
