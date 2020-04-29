package com.qyk.controller.center;

import com.qyk.pojo.bo.center.CenterUserBO;
import com.qyk.service.center.CenterUserService;
import com.qyk.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "center - 用户中心", tags = {"用户中心展示的相关接口"})
@RestController
@RequestMapping("userInfo ")
public class CenterUserController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @GetMapping("update")
    public JSONResult updateUserInfo(
            @ApiParam(name = "userId", value = "用户ID", required = true) String userId,
            @RequestBody CenterUserBO centerUserBO) {

        return JSONResult.ok();
    }
}
