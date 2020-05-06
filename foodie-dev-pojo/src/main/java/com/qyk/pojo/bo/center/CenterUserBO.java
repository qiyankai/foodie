package com.qyk.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 封装用户注册入参
 */
@ApiModel(value = "用户对象BO", description = "封装客户端入参")
public class CenterUserBO {
    // 待更新

    @ApiModelProperty(value = "用户名", name = "username", example = "qyk", required = true)
    private String username;

    @ApiModelProperty(value = "密码", name = "password", example = "123456", required = true)
    private String password;

    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123456", required = true)
    private String confirmPassword;

    @NotBlank(message = "请填写昵称")
    @Length(max = 12,message = "用户昵称不能超过12位")
    @ApiModelProperty(value = "用户昵称", name = "nickname", example = "qyk", required = true)
    private String nickname;

    @Length(max = 12,message = "用户真实姓名不能超过12位")
    @ApiModelProperty(value = "真实姓名", name = "realname", example = "qyk", required = true)
    private String realname;

    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$")
    @ApiModelProperty(value = "手机号", name = "mobile", example = "13999999999", required = true)
    private String mobile;

    @Email
    @ApiModelProperty(value = "邮箱地址", name = "email", example = "qyk@qq.com", required = true)
    private String email;

    @Min(value = 0,message = "性别不正确")
    @Max(value = 2,message = "性别不正确")
    @ApiModelProperty(value = "性别", name = "sex", example = "0，女；1，男；2，保密", required = true)
    private String sex;

    @ApiModelProperty(value = "生日", name = "birthday", example = "1996-01-01", required = true)
    private String birthday;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
