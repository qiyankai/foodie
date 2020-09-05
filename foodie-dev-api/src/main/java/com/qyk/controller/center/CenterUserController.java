package com.qyk.controller.center;

import com.qyk.controller.BaseController;
import com.qyk.pojo.Users;
import com.qyk.pojo.bo.center.CenterUserBO;
import com.qyk.pojo.vo.UserVo;
import com.qyk.resource.FileUpload;
import com.qyk.service.center.CenterUserService;
import com.qyk.utils.CookieUtils;
import com.qyk.utils.DateUtil;
import com.qyk.utils.JSONResult;
import com.qyk.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "center - 用户中心", tags = {"用户中心展示的相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public JSONResult updateUserInfo(
            @ApiParam(name = "userId", value = "用户ID", required = true) @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return JSONResult.errorMap(errors);
        }
        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);

        UserVo userVo = convertUserVo(userResult);
//        setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVo), true);

        return JSONResult.ok();
    }

    @ApiOperation(value = "上传用户头像", notes = "上传用户头像", httpMethod = "POST")
    @PostMapping("uploadFace")
    public JSONResult updateUserInfo(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) {

        // 头像保存地址
//        String imageUserFileLocationPath = IMAGE_USER_FILE_LOCATION;
        // 使用配置文件配置上传路径
        String imageUserFileLocationPath = fileUpload.getImageUserFileLocation();
        // 在文件路径上增加一个路径，通过userId来区分不同用户路径
        String uploadFilePrefix = File.separator + userId;
        FileOutputStream fileOutputStream = null;
        if (file != null) {
            // 存放规则，face-{userId}.jpg
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isNotBlank(originalFilename)) {
                String[] fileNameArr = originalFilename.split("\\.");
                // 这是文件后缀名
                String suffix = fileNameArr[fileNameArr.length - 1];

                // 防攻击，防脚本
                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpeg") &&
                        !suffix.equalsIgnoreCase("gif")
                ) {
                    return JSONResult.errorMsg("图片格式不正确！");
                }

                String fileName = "face-" + userId + new Date().getTime() + "." + suffix;
                // 最终上传位置，本地上传地址+用户文件夹+文件名
                String finalFacePath = imageUserFileLocationPath + uploadFilePrefix + File.separator + fileName;

                // 用于提供给WEB的Url，直接使用这个变量
                uploadFilePrefix += ("/" + fileName);

                File outFile = new File(finalFacePath);
                if (outFile.getParentFile() != null) {
                    //创建夫路径文件夹
                    outFile.getParentFile().mkdirs();
                }

                //输出文件
                try {
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } else {
            return JSONResult.errorMsg("请勿上传空文件！");
        }
        // 从配置文件获取图片服务器地址，再拼接图片Url
        String imageServerUrl = fileUpload.getImageServerUrl();
        // url加时间戳，防止浏览器由于缓存导致无法即时生效
        String finelUserFaceWebUrl = imageServerUrl + uploadFilePrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        // 执行更新操作
        Users userResult = centerUserService.updateUserFace(userId, finelUserFaceWebUrl);

//        setNullProperty(userResult);
        UserVo userVo = convertUserVo(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVo), true);


        return JSONResult.ok();
    }


    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setRealname(null);
        userResult.setEmail(null);
        userResult.setUpdatedTime(null);
        userResult.setCreatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            errorMap.put(field, defaultMessage);
        }
        return errorMap;
    }

}
