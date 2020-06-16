package com.zkkj.gps.gateway.ccs.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zkkj.gps.gateway.ccs.annotation.NoAccessResponseLogger;
import com.zkkj.gps.gateway.ccs.config.CommonBaseUtil;
import com.zkkj.gps.gateway.ccs.dto.area.*;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.service.GroupAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groupArea")
@Api(value = "GroupAreaController", description = "群组区域接口")
public class GroupAreaController extends CommonBaseUtil {
    private Logger logger = LoggerFactory.getLogger(GroupAreaController.class);

    @Autowired
    private GroupAreaService groupAreaService;

    @PostMapping(value = "/addGroupAreaInfo")
    @ApiOperation("新增区域信息")
    public ResultVo<String> addGroupAreaInfo(@RequestBody @ApiParam("新增区域实体") GroupAreaEntity groupAreaEntity) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            String appKey = getAppkey();
            if (groupAreaEntity != null && groupAreaEntity.getAreaName() != null &&
                    groupAreaEntity.getAreaName().length() > 0 &&
                    !StringUtils.isEmpty(groupAreaEntity.getIdentity())) {
                String identity = groupAreaEntity.getIdentity();
                if (identity.equals("*")) {
                    identity = appKey;
                }
                groupAreaEntity.setCreateUser(identity);
                groupAreaEntity.setIdentity(identity);
                AreaEntity areaEntity = new AreaEntity();
                BeanUtils.copyProperties(groupAreaEntity, areaEntity);
                areaEntity.setAppKey(appKey);
                //新增区域
                //判断当前区域名称是否存在
                GroupAreaEntity groupAreaNew = groupAreaService.getGroupAreaInfoByGroupIdAndAreaName(appKey, identity,
                        groupAreaEntity.getAreaName());
                if (groupAreaNew != null && groupAreaNew.getId() != null && groupAreaNew.getId().length() > 0) {
                    resultVo.resultFail("当前区域名称已存在，请勿重复添加");
                    return resultVo;
                }
                int result = groupAreaService.addGroupAreaInfo(areaEntity);
                if (result > 0) {
                    resultVo.resultSuccess("新增成功");
                    //新增绑定关系updateGroupAreaInfo
                } else {
                    resultVo.resultFail("新增失败");
                }
            } else {
                resultVo.resultFail("传入的参数有误");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:新增区域信息失败");
            logger.error("GroupAreaController addGroupAreaInfo is error", ex);
        }
        return resultVo;
    }

    @PostMapping(value = "/updateGroupAreaInfo")
    @ApiOperation("修改区域信息")
    public ResultVo<String> updateGroupAreaInfo(@RequestBody @ApiParam("修改区域信息实体") GroupAreaUpdateDto groupAreaUpdateDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (groupAreaUpdateDto != null && !StringUtils.isEmpty(groupAreaUpdateDto.getId())) {
                //修改区域信息
                String identity = groupAreaUpdateDto.getIdentity();
                if (!StringUtils.isEmpty(identity)) {
                    if (identity.equals("*")) {
                        identity = getAppkey();
                    }
                    groupAreaUpdateDto.setCreateUser(identity);
                    groupAreaUpdateDto.setIdentity(identity);
                    int updateAreaResult = groupAreaService.updateGroupArea(groupAreaUpdateDto);
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
            resultVo.resultFail("系统异常:修改区域信息失败");
            logger.error("GroupAreaController updateGroupAreaInfo is error", ex);
        }
        return resultVo;
    }

    @GetMapping(value = "/deleteGroupAreaInfo")
    @ApiOperation("删除区域信息")
    public ResultVo<String> deleteGroupAreaInfo(@RequestParam @ApiParam(name = "id", value = "区域id", required = true) String id,
                                                @RequestParam @ApiParam(name = "identity", value = "identity", required = true) String identity) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (id != null && id.length() > 0 && !StringUtils.isEmpty(identity)) {
                String appKey = getAppkey();
                if (identity.equals("*")) {
                    identity = appKey;
                }
                int result = groupAreaService.deleteGroupArea(id, identity);
                if (result > 0) {
                    resultVo.resultSuccess("删除成功");
                } else {
                    resultVo.resultFail("删除失败，该数据不存在或已删除");
                }
            } else {
                resultVo.resultFail("传入的参数有误");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:删除区域信息失败");
            logger.error("GroupAreaController deleteGroupAreaInfo is error",ex);
        }
        return resultVo;
    }

    @PostMapping(value = "/getGroupAreaListInfo")
    @ApiOperation("获取区域信息列表")
    @NoAccessResponseLogger
    public ResultVo<List<GroupAreaEntity>> getGroupAreaListInfo(@RequestBody KeyWordsEntity keyWordsEntity) {
        ResultVo<List<GroupAreaEntity>> resultVo = new ResultVo<>();
        try {
            //获取当前用户appkey和identity
            String appkey = getAppkey();
            if (keyWordsEntity != null && !StringUtils.isEmpty(keyWordsEntity.getIdentity())) {
                String identity = keyWordsEntity.getIdentity();
                if (identity.equals("*")) {
                    identity = "";
                }
                PageHelper.startPage(keyWordsEntity.getCurrentPage(), keyWordsEntity.getPageSize());
                List<GroupAreaEntity> groupAreaEntities = groupAreaService.getGroupAreaListInfo(appkey, identity, keyWordsEntity.getKeyWords());
                for (GroupAreaEntity groupAreaEntity : groupAreaEntities) {
                    groupAreaEntity.setAreaPoints(groupAreaEntity.getAreaPoints() == null ? "" : groupAreaEntity.getAreaPoints());
                    groupAreaEntity.setCreateUser(groupAreaEntity.getCreateUser() == null ? "" : groupAreaEntity.getCreateUser());
                }
                PageInfo<GroupAreaEntity> pageInfo = new PageInfo<>(groupAreaEntities);
                long total = pageInfo.getTotal();
                resultVo.resultSuccess(groupAreaEntities);
                resultVo.setTotal((int) (total));
                resultVo.setMsg("获取成功");
            } else {
                resultVo.resultFail("传入参数有误");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取区域信息列表失败");
            logger.error("GroupAreaController deleteGroupAreaInfo is error", ex);
        }
        return resultVo;
    }

    @GetMapping(value = "/getGroupAreaDetailInfoById")
    @ApiOperation("获取区域信息详情")
    public ResultVo<GroupAreaDetailInfo> getGroupAreaDetailInfoById(@RequestParam @ApiParam(name = "id", value = "区域id", required = true) String id) {
        ResultVo<GroupAreaDetailInfo> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isEmpty(id)) {
                resultVo.resultFail("请传入正确的区域id");
            } else {
                //获取区域对象信息
                GroupAreaDetailInfo groupAreaDetailInfo = groupAreaService.getGroupAreaDetailInfoById(id);
                if (groupAreaDetailInfo != null && groupAreaDetailInfo.getId() != null && groupAreaDetailInfo.getId().length() > 0) {
                    resultVo.resultSuccess(groupAreaDetailInfo);
                } else {
                    resultVo.resultFail("传入的区域id有误");
                }
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取区域信息详情失败");
            logger.error("GroupAreaController deleteGroupAreaInfo is error", ex);
        }
        return resultVo;
    }

}
