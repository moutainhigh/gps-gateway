package com.zkkj.gps.gateway.ccs.service.impl;

import com.zkkj.gps.gateway.ccs.dto.route.GroupRouteDetailInfo;
import com.zkkj.gps.gateway.ccs.dto.route.GroupRouteEntity;
import com.zkkj.gps.gateway.ccs.dto.route.GroupRouteUpdateDto;
import com.zkkj.gps.gateway.ccs.dto.route.RouteEntity;
import com.zkkj.gps.gateway.ccs.mappper.GroupRouteMapper;
import com.zkkj.gps.gateway.ccs.service.GroupRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupRouteServiceImpl implements GroupRouteService {

    @Autowired
    private GroupRouteMapper groupRouteMapper;

    @Override
    public int addGroupRouteInfo(RouteEntity routeEntity) {
        return groupRouteMapper.addGroupRouteInfo(routeEntity);
    }

    @Override
    public int updateGroupRoute(GroupRouteUpdateDto groupRouteUpdateDto) {
        return groupRouteMapper.updateGroupRoute(groupRouteUpdateDto);
    }

    @Override
    public int deleteGroupRoute(String id, String updateUser) {
        return groupRouteMapper.deleteGroupRoute(id,updateUser);
    }

    @Override
    public List<GroupRouteEntity> getGroupRouteListInfo(String appkey, String identity, String keyWords) {
        return groupRouteMapper.getGroupRouteListInfo(appkey, identity, keyWords);
    }

    @Override
    public GroupRouteDetailInfo getGroupRouteDetailInfo(String id) {
        return groupRouteMapper.getGroupRouteDetailInfo(id);
    }

    @Override
    public GroupRouteEntity getGroupInfoByGroupIdAndRouteName(String appkey, String identity, String routeName) {
        return groupRouteMapper.getGroupInfoByGroupIdAndRouteName(appkey, identity, routeName);
    }
}
