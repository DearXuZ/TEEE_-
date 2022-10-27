package com.teee.service.HomeWork.Questions;

import com.teee.domain.BooleanReturn;
import com.teee.domain.works.BankQuestion;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Xu ZhengTao
 */
public interface QuestionBankService {
    BooleanReturn createQuestionBank(String bankName, Integer bankType, String questions, Long owner);
    BooleanReturn deleteQuestionBank(Integer bankId);
    BooleanReturn editQuestionsBank(BankQuestion bankQuestion);
    BankQuestion getQuestionBankById(Integer baknId);
    List<BankQuestion> getQuestionBankByOnwer(Long tid);
    BooleanReturn addBankTags(Integer bankId, ArrayList<String> tags);
}
