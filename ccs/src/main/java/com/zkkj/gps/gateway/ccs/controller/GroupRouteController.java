package com.zkkj.gps.gateway.ccs.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zkkj.gps.gateway.ccs.annotation.NoAccessResponseLogger;
import com.zkkj.gps.gateway.ccs.config.CommonBaseUtil;
import com.zkkj.gps.gateway.ccs.dto.area.KeyWordsEntity;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.route.GroupRouteDetailInfo;
import com.zkkj.gps.gateway.ccs.dto.route.GroupRouteEntity;
import com.zkkj.gps.gateway.ccs.dto.route.GroupRouteUpdateDto;
import com.zkkj.gps.gateway.ccs.dto.route.RouteEntity;
import com.zkkj.gps.gateway.ccs.service.GroupRouteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/groupRoute")
@Api(value = "GroupRouteController", description = "群组线路接口")
public class GroupRouteController extends CommonBaseUtil {

    private Logger logger = LoggerFactory.getLogger(GroupRouteController.class);

    @Autowired
    private GroupRouteService groupRouteService;

    @PostMapping(value = "/addGroupRouteInfo")
    @ApiOperation("新增线路信息")
    @NoAccessResponseLogger
    public ResultVo<String> addGroupRouteInfo(@RequestBody @ApiParam("新增线路实体") GroupRouteEntity groupRouteEntity) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            String appkey = getAppkey();
            if (groupRouteEntity != null && !StringUtils.isEmpty(groupRouteEntity) &&
                    !StringUtils.isEmpty(groupRouteEntity.getRouteName()) &&
                    !StringUtils.isEmpty(groupRouteEntity.getIdentity())) {
                String identity = groupRouteEntity.getIdentity();
                if (identity.equals("*")){
                    identity = appkey;
                }
                RouteEntity routeEntity = new RouteEntity();
                groupRouteEntity.setCreateUser(identity);
                groupRouteEntity.setIdentity(identity);
                BeanUtils.copyProperties(groupRouteEntity,routeEntity);
                routeEntity.setAppKey(appkey);
                //判断当前线路是否存在
                GroupRouteEntity groupRouteNew = groupRouteService.getGroupInfoByGroupIdAndRouteName(appkey,identity, groupRouteEntity.getRouteName());
                if (groupRouteNew != null && groupRouteNew.getId() != null && groupRouteNew.getId().length() > 0) {
                    resultVo.resultFail("当前线路名称已存在，请勿重复添加");
                    return resultVo;
                }
                int result = groupRouteService.addGroupRouteInfo(routeEntity);
                if (result > 0) {
                    resultVo.resultSuccess("新增成功");
                } else {
                    resultVo.resultFail("新增失败");
                }
            } else {
                resultVo.resultFail("传入的参数有误");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:新增线路信息失败");
            logger.error("GroupRouteController addGroupRouteInfo is error" , ex);
        }
        return resultVo;
    }

    @PostMapping(value = "/updateGroupRouteInfo")
    @ApiOperation("修改线路信息")
    @NoAccessResponseLogger
    public ResultVo<String> updateGroupRouteInfo(@RequestBody @ApiParam("修改线路信息") GroupRouteUpdateDto groupRouteUpdateDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (groupRouteUpdateDto != null && !StringUtils.isEmpty(groupRouteUpdateDto.getId())) {
                //修改区域信息
                String identity = groupRouteUpdateDto.getIdentity();
                if (!StringUtils.isEmpty(identity)){
                    if (identity.equals("*")){
                        identity = getAppkey();
                    }
                    groupRouteUpdateDto.setCreateUser(identity);
                    groupRouteUpdateDto.setIdentity(identity);
                    int updateAreaResult = groupRouteService.updateGroupRoute(groupRouteUpdateDto);
                    if (updateAreaResult > 0) {
                        resultVo.resultSuccess("修改成功");
                    } else {
                        resultVo.resultFail("修改失败");
                    }
                } else {
                    resultVo.resultFail("请传入identity");
                }
            } else {
                resultVo.resultFail("传入的参数有误");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:修改线路信息失败");
            logger.error("GroupRouteController updateGroupRouteInfo is error" + ex.getMessage());
        }
        return resultVo;
    }

    @GetMapping (value = "/deleteGroupRouteInfo")
    @ApiOperation("删除线路信息")
    public ResultVo<String> deleteGroupRouteInfo(@RequestParam @ApiParam(name = "id",value = "线路id",required = true) String id,
                                                 @RequestParam @ApiParam(name = "identity",value = "identity",required = true) String identity) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (id != null && id.length() > 0 && !StringUtils.isEmpty(identity)) {
                String appKey = getAppkey();
                if (identity.equals("*")){
                    identity = appKey;
                }
                int result = groupRouteService.deleteGroupRoute(id, identity);
                if (result > 0) {
                    resultVo.resultSuccess("删除成功");
                } else {
                    resultVo.resultFail("删除失败，该数据不存在或已删除");
                }
            } else {
                resultVo.resultFail("传入的参数有误");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:删除线路信息失败");
            logger.error("GroupRouteController deleteGroupRouteInfo is error" + ex.getMessage());
        }
        return resultVo;
    }

    @PostMapping(value = "/getGroupRouteListInfo")
    @ApiOperation("获取线路信息列表")
    @NoAccessResponseLogger
    public ResultVo<List<GroupRouteEntity>> getGroupRouteListInfo(@RequestBody KeyWordsEntity keyWordsEntity) {
        ResultVo<List<GroupRouteEntity>> resultVo = new ResultVo<>();
        try {
            if (keyWordsEntity != null && !StringUtils.isEmpty(keyWordsEntity.getIdentity())) {
                //获取当前用户appkey和identity
                String appkey = getAppkey();
                String identity = keyWordsEntity.getIdentity();
                if (identity.equals("*")){
                    identity = "";
                }
                PageHelper.startPage(keyWordsEntity.getCurrentPage(), keyWordsEntity.getPageSize());
                List<GroupRouteEntity> groupRouteEntities = groupRouteService.getGroupRouteListInfo(appkey, identity, keyWordsEntity.getKeyWords());
                for (GroupRouteEntity groupRouteEntity :groupRouteEntities) {
                    groupRouteEntity.setCreateUser(groupRouteEntity.getCreateUser()==null?"":groupRouteEntity.getCreateUser());
                    groupRouteEntity.setRemark(groupRouteEntity.getRemark()==null?"":groupRouteEntity.getRemark());
                    groupRouteEntity.setRouteColor(groupRouteEntity.getRouteColor()==null?"":groupRouteEntity.getRouteColor());
                }
                PageInfo<GroupRouteEntity> pageInfo = new PageInfo<>(groupRouteEntities);
                long total = pageInfo.getTotal();
                resultVo.resultSuccess(groupRouteEntities);
                resultVo.setTotal((int) (total));
                resultVo.setMsg("获取成功");
            } else {
                resultVo.resultFail("传入参数有误");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取线路信息列表失败");
            logger.error("GroupRouteController getGroupRouteListInfo is error" + ex.getMessage());
        }
        return resultVo;
    }

    @GetMapping (value = "/getGroupRouteDetailInfoById")
    @ApiOperation("获取线路信息详情")
    @NoAccessResponseLogger
    public ResultVo<GroupRouteDetailInfo> getGroupRouteDetailInfoById(@RequestParam @ApiParam(name = "id",value = "线路id",required = true) String id) {
        ResultVo<GroupRouteDetailInfo> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isEmpty(id)){
                resultVo.resultFail("请传入正确的线路id");
            } else {
                //获取区域对象信息
                GroupRouteDetailInfo groupRouteDetailInfo = groupRouteService.getGroupRouteDetailInfo(id);
                if (groupRouteDetailInfo != null && groupRouteDetailInfo.getId() != null && groupRouteDetailInfo.getId().length() > 0) {
                    resultVo.resultSuccess(groupRouteDetailInfo);
                } else {
                    resultVo.resultFail("传入的线路id有误");
                }
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取线路信息详情失败");
            logger.error("GroupRouteController getGroupRouteDetailInfoById is error" + ex.getMessage());
        }
        return resultVo;
    }

}

