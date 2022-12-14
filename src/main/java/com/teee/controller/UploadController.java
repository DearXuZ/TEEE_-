package com.teee.controller;

import com.alibaba.fastjson.JSONObject;
import com.teee.config.Code;
import com.teee.domain.returnClass.BooleanReturn;
import com.teee.domain.returnClass.Result;
import com.teee.domain.returnClass.UploadErr;
import com.teee.domain.returnClass.UploadResult;
import com.teee.service.HomeWork.Exams.ExamService;
import com.teee.service.User.UserService;
import com.teee.utils.JWT;
import com.teee.utils.SpringBeanUtil;
import com.teee.utils.Tencent;
import com.teee.utils.TypeChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @author Xu ZhengTao
 */
@RequestMapping("/upload")
@Controller
public class UploadController {
    
    @Autowired
    Tencent tencent;
    
    @Value("${path.picPath}")
    private String picPath;

    @Value("${path.filePath}")
    private String filePath;

    @Value("${path.facePath}")
    private String facePath;

    @Value("${path.tempPath}")
    private String tempPath;

    @Value("${server.port}")
    private String port;

    @Value("${realMaxFileSizeMB}")
    private int maxSizeMB;

    @RequestMapping("/uploadFacePic")
    @ResponseBody
    public Result uploadFacePic(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file, HttpServletRequest request){
        UploadResult uploadResult = uploadImg(file, request, facePath, "face");
        if(uploadResult.getUploaded() == 0){
            return new Result(Code.ERR, uploadResult.getError());
        }else{
            SpringBeanUtil.getBean(UserService.class).setFace(JWT.getUid(token),facePath + File.separator+uploadResult.getFileName());
            return new Result(Code.Suc,null, "????????????");
        }
    }

    @RequestMapping("/checkFace")
    @ResponseBody
    public Result checkFace(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file, HttpServletRequest request){
        UploadResult uploadResult = uploadImg(file, request, tempPath, "tmp");
        if(uploadResult.getUploaded() == 0){
            return new Result(Code.ERR, uploadResult.getError());
        }else{
            BooleanReturn faceCheck = SpringBeanUtil.getBean(ExamService.class).faceCheck(JWT.getUid(token),tempPath + File.separator + uploadResult.getFileName());
            if(faceCheck.isSuccess()){
                return new Result(Code.Suc,null,"????????????");
            }else{
                return new Result(Code.ERR,faceCheck.getData(), "???????????????: " + faceCheck.getMsg());

            }
        }
    }


    @RequestMapping("/img")
    @ResponseBody
    public UploadResult uploadPicImg(@RequestParam("upload") MultipartFile file, HttpServletRequest request){
        UploadResult uploadResult = uploadImg(file, request, picPath, "pic");
        return uploadResult;
    }

    private UploadResult uploadImg(MultipartFile file, HttpServletRequest request, String path, String dirName){
        ArrayList<String> suffixWhiteList = new ArrayList<>();
        suffixWhiteList.add(".png");
        suffixWhiteList.add(".jpg");
        suffixWhiteList.add(".jpeg");
        suffixWhiteList.add(".gif");
        suffixWhiteList.add(".ico");
        suffixWhiteList.add(".cur");
        suffixWhiteList.add(".jfif");
        suffixWhiteList.add(".pjpeg");
        suffixWhiteList.add(".pjp");
        suffixWhiteList.add(".svg");
        suffixWhiteList.add(".tif");
        suffixWhiteList.add(".webp");
        suffixWhiteList.add(".tiff");
        System.out.println("??????upload");
        if(file == null){
            return new UploadResult(0, new UploadErr("????????????????????????"));
        }
        String originalFilename = file.getOriginalFilename();
        String appendName = originalFilename.substring(originalFilename.lastIndexOf("."));
        System.out.println("append: " + appendName);
        if(!suffixWhiteList.contains(appendName)){
            return new UploadResult(0, new UploadErr("?????????????????????"));

//            return new UploadResult(0, new UploadErr("????????????????????????"));
        }
        System.out.println("origin: " + originalFilename);
        System.out.println("appendName: " + appendName);
        File newMkdir = new File(path);
        if(!newMkdir.exists()){
            newMkdir.mkdirs();
        }
        String uploadFile = System.currentTimeMillis() + appendName;
        try {
            file.transferTo(new File(path+File.separator+uploadFile));
            String url = request.getScheme() + "://" + request.getServerName() + ":" + port + "/" + dirName + "/" + uploadFile;
            System.out.println("url=" + url);
            return new UploadResult(1, uploadFile,url);
        } catch (IOException e) {
            e.printStackTrace();
            return new UploadResult(0, new UploadErr("????????????"));
        }
    }





    @RequestMapping("/file")
    @ResponseBody
    public Result uploadFile(@RequestParam("file") MultipartFile[] file, HttpServletRequest request){
        try{
            System.out.println("?????????????????? ...");
            // ????????????
            for (MultipartFile multipartFile : file) {
                int check = checkFile(multipartFile);
                if(check!=0){
                    if(check==1){
                        return new Result(Code.ERR,null ,"??????:" + multipartFile.getOriginalFilename() + "??????????????????(" + maxSizeMB + "MB)");
                    }
                }
            }

            JSONObject ret = new JSONObject();
            ArrayList<String> arrayList = new ArrayList<>();
            for (MultipartFile multipartFile : file) {
                if(multipartFile.isEmpty()){
                    ret.put("err", "?????????");
                    return new Result(Code.ERR, null, "?????????");
                }
                String fileName = multipartFile.getOriginalFilename();
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                System.out.println("??????: " + fileName + ", ??????: " + suffixName);
                File fileTempObj = new File(filePath + File.separator + "TimeStamp_" +  System.currentTimeMillis() + "_" + fileName);
                if(!fileTempObj.getParentFile().exists()){
                    fileTempObj.getParentFile().mkdirs();
                }
                try{
                    multipartFile.transferTo(fileTempObj);
                }catch (Exception e){
                    e.printStackTrace();
                    return new Result(Code.ERR, null, "??????????????????: " + fileTempObj.getName());
                }
                arrayList.add("\"" + fileTempObj.getName() + "\"");
            }
            return new Result(Code.Suc, TypeChange.arrL2str(arrayList), "???????????????");
//        return new Result(Code.Suc, fileName + "|" + ((double) fileTempObj.length() / 1024 / 1024));
        }catch (Exception e){
            return new Result(Code.ERR, e.getMessage(), "Err Cause by UploadController");
        }

    }

    @RequestMapping("/getFile")
    @ResponseBody
    public Result downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        File file = new File(filePath + File.separator + fileName);
        String substring = fileName.substring(fileName.lastIndexOf("_")+1);
        String fileOriginName = substring.substring(substring.lastIndexOf("_")+1);
        if(!file.exists()){
            return new Result(Code.ERR, null, "???????????????");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int)file.length());
        response.setHeader("Content-Disposition",URLEncoder.encode(fileOriginName, "UTF-8"));

        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(file);
            OutputStream os = response.getOutputStream();
            os.write(bytes);
//            return new Result(Code.Suc, null, "??????????????????");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(Code.ERR, null, "??????????????????");
        }
        return null;
    }




    /**
     * ????????????
     *  0.????????????
     *  1.??????????????????
     *  2.????????????????????? (TODO)
     * */
    public int checkFile(MultipartFile file){

        if ((file.getSize() / (1024*1024)) > maxSizeMB ){
            return 1;
        }
        /* TODO
        else if (){

        }
         */
        else{
            return 0;
        }
    }
}
