package com.xdu.message;

import lombok.*;

import java.io.Serializable;

/**
 * @author tyeerth
 * @date 2023/10/27 - 上午10:06
 * @description 作为RpcMessage里面的data内容
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String requestId;
    private String interfaceName;

    private String methodName;
    /**
     * method parameters
     */
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String version;
    private String group;

    public String getRpcServiceName() {
        return this.getInterfaceName() + this.getGroup() + this.getVersion();
    }
}
