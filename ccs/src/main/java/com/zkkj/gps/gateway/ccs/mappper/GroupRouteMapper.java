package com.zkkj.gps.gateway.ccs.mappper;

import com.zkkj.gps.gateway.ccs.dto.route.GroupRouteDetailInfo;
import com.zkkj.gps.gateway.ccs.dto.route.GroupRouteEntity;
import com.zkkj.gps.gateway.ccs.dto.route.GroupRouteUpdateDto;
import com.zkkj.gps.gateway.ccs.dto.route.RouteEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupRouteMapper {

    /*
     * @Author lx
     * @Description 新增线路信息
     * @Date 11:47 2019/5/13
     * @Param
     * @return
     **/
    public int addGroupRouteInfo(RouteEntity routeEntity);
    /*
     * @Author lx
     * @Description 修改线路信息
     * @Date 14:15 2019/5/13
     * @Param
     * @return
     **/
    public int updateGroupRoute(GroupRouteUpdateDto groupRouteUpdateDto);
    /*
     * @Author lx
     * @Description 根据id删除线路信息
     * @Date 14:29 2019/5/13
     * @Param
     * @return
     **/
    public int deleteGroupRoute(@Param("id")String id, @Param("updateUser")String updateUser);
    /*
     * @Author lx
     * @Description 根据appkey和identity获取群组线路信息
     * @Date 14:34 2019/5/13
     * @Param
     * @return
     **/
    public List<GroupRouteEntity> getGroupRouteListInfo(@Param("appkey")String appkey, @Param("identity")String identity, @Param("keyWords")String keyWords);
    /*
     * @Author lx
     * @Description 根据线路id获取线路信息详情
     * @Date 14:48 2019/5/13
     * @Param
     * @return
     **/
    public GroupRouteDetailInfo getGroupRouteDetailInfo(String id);
    /*
     * @Author lx
     * @Description
     * @Date 16:15 2019/5/15
     * @Param
     * @return
     **/
    public GroupRouteEntity getGroupInfoByGroupIdAndRouteName(@Param("appkey")String appkey,@Param("identity")String identity,@Param("routeName")String routeName);
}
