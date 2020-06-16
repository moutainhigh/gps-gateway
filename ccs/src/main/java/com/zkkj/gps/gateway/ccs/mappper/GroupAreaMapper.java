package com.zkkj.gps.gateway.ccs.mappper;

import com.zkkj.gps.gateway.ccs.dto.area.AreaEntity;
import com.zkkj.gps.gateway.ccs.dto.area.GroupAreaDetailInfo;
import com.zkkj.gps.gateway.ccs.dto.area.GroupAreaEntity;
import com.zkkj.gps.gateway.ccs.dto.area.GroupAreaUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupAreaMapper {
    /*
     * @Author lx
     * @Description 新增区域信息
     * @Date 14:20 2019/5/11
     * @Param
     * @return
     **/
    public int addGroupAreaInfo(AreaEntity areaEntity);


    /*
     * @Author lx
     * @Description 根据id删除area信息
     * @Date 16:26 2019/5/11
     * @Param
     * @return
     **/
    public int deleteGroupArea(@Param("id")String id,@Param("updateUser") String updateUser);

    /*
     * @Author lx
     * @Description 修改区域信息
     * @Date 16:54 2019/5/11
     * @Param
     * @return
     **/
    public int updateGroupArea(GroupAreaUpdateDto groupAreaUpdateDto);

    /*
     * @Author lx
     * @Description 根据appkey和identity获取区域列表
     * @Date 17:22 2019/5/11
     * @Param
     * @return
     **/
    public List<GroupAreaEntity> getGroupAreaListInfo(@Param("appkey")String appkey, @Param("identity")String identity, @Param("keyWords")String keyWords);

    /*
     * @Author lx
     * @Description 根据区域id获取区域信息详情
     * @Date 17:38 2019/5/11
     * @Param
     * @return
     **/
    public GroupAreaDetailInfo getGroupAreaDetailInfoById(String id);

    /*
     * @Author lx
     * @Description 判断当前区域名称是否存在
     * @Date 15:03 2019/5/15
     * @Param
     * @return
     **/
    public GroupAreaEntity getGroupAreaInfoByGroupIdAndAreaName(@Param("appkey")String appkey,@Param("identity")String identity,@Param("areaName")String areaName);
}
