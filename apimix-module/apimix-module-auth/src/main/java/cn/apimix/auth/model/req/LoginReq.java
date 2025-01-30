package cn.apimix.auth.model.req;

import cn.apimix.auth.enunms.AuthTypeEnum;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 基础登录参数
 *
 * @Author: Hor
 * @Date: 2025/1/29 11:16
 * @Version: 1.0
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "authType", visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = AccountLoginReq.class, name = "ACCOUNT"),
        @JsonSubTypes.Type(value = EmailLoginReq.class, name = "EMAIL"),
        @JsonSubTypes.Type(value = WxGzhLoginReq.class, name = "WX"),
        @JsonSubTypes.Type(value = SocialLoginReq.class, name = "SOCIAL")})
public class LoginReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 客户端 ID
     */
    private String clientId;

    /**
     * 认证类型
     */
    @NotNull(message = "认证类型非法")
    private AuthTypeEnum authType;

}
