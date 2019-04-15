package com.chenym.pig.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends ATreeNode {
    private String title;
}
