package com.chenym.pig.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserRole extends Model<SysUserRole> {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 角色ID
     */
    private Integer roleId;
}
