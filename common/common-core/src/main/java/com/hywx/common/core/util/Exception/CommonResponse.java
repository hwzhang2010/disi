package com.hywx.common.core.util.Exception;

import java.util.HashMap;

/**
 * @program: gateway
 * @description:
 * @author: tangjing
 * @create: 2020-03-04 17:12
 **/
public class CommonResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    public CommonResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public CommonResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    @Override
    public CommonResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public String getMessage() {
        return String.valueOf(get("message"));
    }

    public Object getData() {
        return get("data");
    }
}
