package com.chenym.pig.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptRelation extends Model<SysDeptRelation> {
    private static final long serialVersionUID = 1L;

    /**
     * 祖先节点
     */
    private Integer ancestor;
    /**
     * 后代节点
     */
    private Integer descendant;
}
