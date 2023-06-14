package com.yuanma.module.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.entity.PositionEntity;
import com.yuanma.module.system.model.dto.PositionDto;
import com.yuanma.module.system.model.dto.PositionQueryDto;
import com.yuanma.module.system.query.PositionQuery;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PositionService {

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    PositionDto findById(Long id);

    /**
     * 创建
     * @param resources /
     * @return /
     */
    PositionEntity create(PositionEntity resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(PositionEntity resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Map<String,Object> queryAll(PositionQueryDto criteria, Page pageable);

    /**
     * 查询全部数据
     * @param criteria /
     * @return /
     */
    List<PositionDto> queryAll(PositionQueryDto criteria);


    /**
     * 验证是否被用户关联
     * @param ids /
     */
    void verification(Set<Long> ids);

}
