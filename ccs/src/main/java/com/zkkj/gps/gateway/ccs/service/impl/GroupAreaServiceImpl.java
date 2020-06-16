package com.zkkj.gps.gateway.ccs.service.impl;

import com.zkkj.gps.gateway.ccs.dto.area.AreaEntity;
import com.zkkj.gps.gateway.ccs.dto.area.GroupAreaDetailInfo;
import com.zkkj.gps.gateway.ccs.dto.area.GroupAreaEntity;
import com.zkkj.gps.gateway.ccs.dto.area.GroupAreaUpdateDto;
import com.zkkj.gps.gateway.ccs.mappper.GroupAreaMapper;
import com.zkkj.gps.gateway.ccs.service.GroupAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupAreaServiceImpl implements GroupAreaService {

    @Autowired
    private GroupAreaMapper groupAreaMapper;

    @Override
    public int addGroupAreaInfo(AreaEntity areaEntity) {
        return groupAreaMapper.addGroupAreaInfo(areaEntity);
    }

    @Override
    public int deleteGroupArea(String id, String updateUser) {
        return groupAreaMapper.deleteGroupArea(id,updateUser);
    }

    @Override
    public int updateGroupArea(GroupAreaUpdateDto groupAreaUpdateDto) {
        return groupAreaMapper.updateGroupArea(groupAreaUpdateDto);
    }

    @Override
    public List<GroupAreaEntity> getGroupAreaListInfo(String appkey, String identity, String keyWords) {
        return groupAreaMapper.getGroupAreaListInfo(appkey,identity,keyWords);
    }

    @Override
    public GroupAreaDetailInfo getGroupAreaDetailInfoById(String id) {
        return groupAreaMapper.getGroupAreaDetailInfoById(id);
    }

    @Override
    public GroupAreaEntity getGroupAreaInfoByGroupIdAndAreaName(String appkey, String identity, String areaName) {
        return groupAreaMapper.getGroupAreaInfoByGroupIdAndAreaName(appkey, identity, areaName);
    }
}
