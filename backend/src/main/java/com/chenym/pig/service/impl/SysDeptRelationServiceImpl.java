package com.chenym.pig.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenym.pig.mapper.SysDeptRelationMapper;
import com.chenym.pig.model.SysDept;
import com.chenym.pig.model.SysDeptRelation;
import com.chenym.pig.service.SysDeptRelationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SysDeptRelationServiceImpl extends ServiceImpl<SysDeptRelationMapper, SysDeptRelation> implements SysDeptRelationService {
    private final SysDeptRelationMapper sysDeptRelationMapper;
    /**
     * 维护部门关系
     *
     * @param sysDept 部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertDeptRelation(SysDept sysDept) {
        //增加部门关系表
        SysDeptRelation condition = new SysDeptRelation();
        condition.setDescendant(sysDept.getParentId());
        List<SysDeptRelation> relationList = sysDeptRelationMapper
                .selectList(Wrappers.<SysDeptRelation>query().lambda()
                        .eq(SysDeptRelation::getDescendant, sysDept.getParentId()))
                .stream().map(relation -> {
                    relation.setDescendant(sysDept.getDeptId());
                    return relation;
                }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(relationList)) {
            this.saveBatch(relationList);
        }

        //自己也要维护到关系表中
        SysDeptRelation own = new SysDeptRelation();
        own.setDescendant(sysDept.getDeptId());
        own.setAncestor(sysDept.getDeptId());
        sysDeptRelationMapper.insert(own);
    }

    @Override
    public void deleteAllDeptRealtion(Integer id) {
        baseMapper.deleteDeptRelationsById(id);
    }

    @Override
    public void updateDeptRealtion(SysDeptRelation relation) {
        baseMapper.updateDeptRelations(relation);
    }
}
