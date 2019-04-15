package com.chenym.pig.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysRole extends Model<SysRole> {

    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    @NotBlank(message = "角色名称 不能为空")
    private String roleName;

    @NotBlank(message = "角色标识 不能为空")
    private String roleCode;

    @NotBlank(message = "角色描述 不能为空")
    private String roleDesc;


    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /**
     * 删除标识（0-正常,1-删除）
     */
    @TableLogic
    private String delFlag;

    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }

}
