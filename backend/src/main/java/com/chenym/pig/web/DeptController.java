package com.chenym.pig.web;

import com.chenym.pig.common.log.annotation.SysLog;
import com.chenym.pig.model.SysDept;
import com.chenym.pig.service.SysDeptService;
import com.chenym.pig.utils.R;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dept")
public class DeptController {

    private final SysDeptService sysDeptService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return SysDept
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id) {
        return new R<>(sysDeptService.getById(id));
    }

    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/tree")
    public R listDeptTrees() {
        return new R<>(sysDeptService.listDeptTrees());
    }

    /**
     * 添加
     *
     * @param sysDept 实体
     * @return success/false
     */
    @SysLog("添加部门")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_dept_add')")
    public R save(@Valid @RequestBody SysDept sysDept) {
        return new R<>(sysDeptService.saveDept(sysDept));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @SysLog("删除部门")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_dept_del')")
    public R removeById(@PathVariable Integer id) {
        return new R<>(sysDeptService.removeDeptById(id));
    }

    /**
     * 编辑
     *
     * @param sysDept 实体
     * @return success/false
     */
    @SysLog("编辑部门")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_dept_edit')")
    public R update(@Valid @RequestBody SysDept sysDept) {
        sysDept.setUpdateTime(LocalDateTime.now());
        return new R<>(sysDeptService.updateDeptById(sysDept));
    }
}
