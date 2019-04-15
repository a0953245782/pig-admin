package com.chenym.pig.utils;

import com.chenym.pig.common.constant.CommonConstants;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class R<T> {

    @Getter
    @Setter
    private int code = CommonConstants.SUCCESS;

    @Getter
    @Setter
    private String msg = "success";

    @Getter
    @Setter
    private T data;

    public R() {
        super();
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public R(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }

    public R(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = CommonConstants.FAIL;
    }
}
