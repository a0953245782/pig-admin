package com.chenym.pig.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends Model<SysMenu> {

    @NotNull(message = "菜单ID不能为空")
    @TableId(value = "menu_id", type = IdType.INPUT)
    private Integer menuId;

    @NotBlank(message = "菜单名称 不能为空")
    private String name;

    private String permission;

    private String path;

    @NotNull(message = "菜单父ID不能为空")
    private Integer parentId;


    private String icon;

    private String component;

    private Integer sort;

    @NotNull(message = "菜单类型不能为空")
    private String type;

    private String keepAlive;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private String delFlag;

}
