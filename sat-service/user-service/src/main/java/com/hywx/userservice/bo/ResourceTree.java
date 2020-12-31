package com.hywx.userservice.bo;

import com.hywx.userservice.dao.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-03-11 16:12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceTree extends Tree<Resource>{

    private String path;
    private String component;
    private String code;
    private String icon;
    private Integer type;
    private Integer orderNum;
}

