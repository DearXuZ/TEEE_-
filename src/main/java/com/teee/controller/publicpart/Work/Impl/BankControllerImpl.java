package com.teee.controller.publicpart.Work.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teee.config.Code;
import com.teee.controller.publicpart.Work.BankController;
import com.teee.dao.BankOwnerDao;
import com.teee.dao.BankQuestionDao;
import com.teee.dao.BankWorkDao;
import com.teee.domain.returnClass.BooleanReturn;
import com.teee.domain.returnClass.Result;
import com.teee.domain.works.BankOwner;
import com.teee.domain.works.BankQuestion;
import com.teee.domain.works.BankWork;
import com.teee.service.HomeWork.Questions.QuestionBankService;
import com.teee.service.HomeWork.Works.WorkBankService;
import com.teee.utils.JWT;
import com.teee.utils.TypeChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller

public class BankControllerImpl implements BankController {

    @Autowired
    WorkBankService workBankService;
    @Autowired
    QuestionBankService questionBankService;


    @Override
    @RequestMapping("/Bank/getWorkBankByTid")
    @ResponseBody
    /**
     * return:[
     *  {
     *      id:
     *      owner:
     *      BankName:
     *      Tags:["","",""]
     *
     *  }
     * ]
     * */
    public Result getWorkBankByTid(@RequestHeader("Authorization") String token) {
        BooleanReturn ret = workBankService.getWorkBankByOnwer(JWT.getUid(token));
        if(ret.isSuccess()){
            return new Result(Code.Suc, ret.getData(),"获取成功");
        }else{
            return new Result(Code.ERR, null, ret.getMsg());
        }
    }


    @Override
    public Result editWorkBank(BankWork bankWork) {
        return null;
    }

    @Override
    public Result deleteWorkBank(Integer wbid) {
        return null;
    }

    @Override
    public Result addWorkBank(String token, BankQuestion bankQuestion) {
        return null;
    }

    @Override
    public Result editQueBank(BankQuestion bankQuestion) {
        return null;
    }

    @Override
    public Result deleteQueBank(Integer qbid) {
        return null;
    }


    @Override
    @RequestMapping("/Bank/getQueBankByTid")
    @ResponseBody
    public Result getQueBankByTid(@RequestHeader("Authorization") String token) {
        BooleanReturn ret = questionBankService.getQuestionBankByOnwer(JWT.getUid(token));
        if(ret.isSuccess()){
            return new Result(Code.Suc, ret.getData(),"获取成功");
        }else{
            return new Result(Code.ERR, null, ret.getMsg());
        }
    }

    @Override
    @RequestMapping("/Bank/addWorkBank")
    @ResponseBody
    public Result addWorkBank(@RequestHeader("Authorization") String token, @RequestBody BankWork bankWork) {
        Long tid = JWT.getUid(token);
        BooleanReturn ret = workBankService.createWorkBank(bankWork, tid);
        if(ret.isSuccess()){
            return new Result(Code.Suc, null, ret.getMsg());
        }else{
            return new Result(Code.ERR, null, ret.getMsg());
        }
    }
}
