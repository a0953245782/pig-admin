package com.chenym.pig.web;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenym.pig.common.log.annotation.SysLog;
import com.chenym.pig.model.SysDict;
import com.chenym.pig.service.SysDictService;
import com.chenym.pig.utils.R;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dict")
public class DictController {

    private final SysDictService sysDictService;

    /**
     * 分页查询字典信息
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public R<IPage> getDictPage(Page page, SysDict sysDict) {
        return new R<>(sysDictService.page(page, Wrappers.query(sysDict)));
    }

    /**
     * 添加字典
     *
     * @param sysDict 字典信息
     * @return success、false
     */
    @SysLog("添加字典")
    @PostMapping
    @CacheEvict(value = "dict_details", key = "#sysDict.valuee")
    @PreAuthorize("@pms.hasPermission('sys_dict_add')")
    public R save(@Valid @RequestBody SysDict sysDict) {
        return new R<>(sysDictService.save(sysDict));
    }

    /**
     * 删除字典，并且清除字典缓存
     *
     * @param id   ID
     * @return R
     */
    @SysLog("删除字典")
    @DeleteMapping("/{id}/{valuee}")
    @CacheEvict(value = "dict_details", key = "#valuee")
    @PreAuthorize("@pms.hasPermission('sys_dict_del')")
    public R removeById(@PathVariable Integer id,@PathVariable String valuee) {
        return new R<>(sysDictService.removeById(id));
    }

    /**
     * 修改字典
     *
     * @param sysDict 字典信息
     * @return success/false
     */
    @PutMapping
    @SysLog("修改字典")
    @CacheEvict(value = "dict_details", key = "#sysDict.valuee")
    @PreAuthorize("@pms.hasPermission('sys_dict_edit')")
    public R updateById(@Valid @RequestBody SysDict sysDict) {
        return new R<>(sysDictService.updateById(sysDict));
    }


}