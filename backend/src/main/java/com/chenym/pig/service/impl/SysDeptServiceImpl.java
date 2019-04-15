package com.chenym.pig.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenym.pig.dto.DeptTree;
import com.chenym.pig.dto.TreeUtil;
import com.chenym.pig.mapper.SysDeptMapper;
import com.chenym.pig.model.SysDept;
import com.chenym.pig.model.SysDeptRelation;
import com.chenym.pig.service.SysDeptRelationService;
import com.chenym.pig.service.SysDeptService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    private final SysDeptRelationService sysDeptRelationService;

    /**
     * 查询全部部门树
     *
     * @return 树
     */
    @Override
    public List<DeptTree> listDeptTrees() {
        return getDeptTree(this.list(Wrappers.emptyWrapper()));
    }

    /**
     * 构建部门树
     *
     * @param depts 部门
     * @return
     */
    private List<DeptTree> getDeptTree(List<SysDept> depts) {
        List<DeptTree> treeList = depts.stream()
                .filter(dept -> !dept.getDeptId().equals(dept.getParentId()))
                .map(dept -> {
                    DeptTree node = new DeptTree();
                    // node.setId(dept.getDeptId());
                    node.setTitle(dept.getName());
                    node.setKey(dept.getDeptId());
                    node.setParentId(dept.getParentId());
                    // node.setName(dept.getName());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.buildByLoop(treeList, 0);
    }

    /**
     * 添加信息部门
     *
     * @param dept 部门
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDept(SysDept dept) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept, sysDept);
        this.save(sysDept);
        sysDeptRelationService.insertDeptRelation(sysDept);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDeptById(Integer id) {
        //级联删除部门
        List<Integer> idList = sysDeptRelationService
                .list(Wrappers.<SysDeptRelation>query().lambda()
                        .eq(SysDeptRelation::getAncestor, id))
                .stream()
                .map(SysDeptRelation::getDescendant)
                .collect(Collectors.toList());

        if (CollUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }

        //删除部门级联关系
        sysDeptRelationService.deleteAllDeptRealtion(id);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDeptById(SysDept sysDept) {
        //更新部门状态
        this.updateById(sysDept);
        //更新部门关系
        SysDeptRelation relation = new SysDeptRelation();
        System.out.println("--------");
        System.out.println(sysDept.getParentId());
        System.out.println(sysDept.getDeptId());
        System.out.println("--------");
        relation.setAncestor(sysDept.getParentId());
        relation.setDescendant(sysDept.getDeptId());
        sysDeptRelationService.updateDeptRealtion(relation);
        return Boolean.TRUE;
    }

}
