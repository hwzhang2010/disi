package com.hywx.userservice.bo;

import com.hywx.userservice.dao.Organization;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-03-12 17:40
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationTree extends Tree<Organization>{

    private Integer orderNum;
}

