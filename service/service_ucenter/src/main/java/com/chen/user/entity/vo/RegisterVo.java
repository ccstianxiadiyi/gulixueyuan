package com.chen.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="注册对象",description = "注册对象")
public class RegisterVo {
    @ApiModelProperty(value="昵称")
    public String nickName;
    @ApiModelProperty(value="手机号")
    public String mobile;
    @ApiModelProperty(value="密码")
    public String password;
    @ApiModelProperty(value="验证码")
    public String code;


}
