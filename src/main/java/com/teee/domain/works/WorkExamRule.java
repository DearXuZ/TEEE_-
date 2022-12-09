package com.teee.domain.works;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Xu ZhengTao
 * rule:
 * [ FACECHECK ]
 */
@Data
@TableName("work_exam_rule")
public class WorkExamRule {
    @TableId
    Integer wid;

    String rulePre;

    String ruleEnter;

    String ruleText;

    @TableLogic//逻辑删除
    private Integer deleted;

    public WorkExamRule(Integer wid, String rulePre, String ruleEnter, String ruleText) {
        this.wid = wid;
        this.rulePre = rulePre;
        this.ruleEnter = ruleEnter;
        this.ruleText = ruleText;
        this.deleted = 0;
    }
}
