package com.qyk.controller.center;

import com.qyk.service.center.CenterUserService;
import com.qyk.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "center - 用户中心", tags = {"用户中心展示的相关接口"})
@RestController
@RequestMapping("center ")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    // 待更新
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("userInfo")
    public JSONResult userInfo(@ApiParam(name = "userId", value = "用户ID", required = true) String userId) {
        return JSONResult.ok(centerUserService.queryUserInfo(userId));
    }
}
