package com.zkkj.gps.gateway.automation.runner;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.automation.entity.ResultVo;
import com.zkkj.gps.gateway.automation.entity.terminal.TerminalStatus;
import com.zkkj.gps.gateway.automation.entity.terminalargs.TerminalParams;
import com.zkkj.gps.gateway.automation.service.TerminalOptionsService;
import com.zkkj.gps.gateway.automation.service.TerminalService;
import com.zkkj.gps.gateway.automation.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 应用程序启动后将开始执行终端参数读写操作
 */
@Component
@Order(value = 1)
@Slf4j
public class TerminalRunner implements ApplicationRunner {

    @Autowired
    private TerminalOptionsService optionsService;

    @Autowired
    private TerminalService terminalService;

    @Value("${terminal_main_ip}")
    private String terminalIp;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (StringUtils.isEmpty(terminalIp)) {
            return;
        }
        try {
            List<String> listAllLast = terminalService.getTerminalListFromLast();
            LoggerUtils.info(log, "最新点位表中的数据量：【" + listAllLast.size() + "】");
            List<String> terminalListDb = terminalService.getTerminalList();
            LoggerUtils.info(log, "终端设置表中增加数据前的数据量：【" + terminalListDb.size() + "】");
            List<String> terminalListAdd = Lists.newArrayList();
            //如果数据库和点位表中的数据均为空
            if (CollectionUtils.isEmpty(terminalListDb) && CollectionUtils.isEmpty(listAllLast)){
                LoggerUtils.info(log,"终端设置表和点位表的数据均为空");
                return;
            } else if (CollectionUtils.isEmpty(terminalListDb) && !CollectionUtils.isEmpty(listAllLast)){//如果数据库中数据为空
                terminalListAdd = listAllLast;
            } else if (!CollectionUtils.isEmpty(terminalListDb) && CollectionUtils.isEmpty(listAllLast)){
                terminalListAdd = terminalListDb;
            } else {
                for (String lastStr : listAllLast) {
                    if (!StringUtils.isEmpty(lastStr) && !terminalListDb.contains(lastStr)) {
                        terminalListAdd.add(lastStr);
                    }
                }
            }
            LoggerUtils.info(log, "终端设置表中需要新增的数据增量：【" + terminalListAdd.size() + "】");
            if (!CollectionUtils.isEmpty(terminalListAdd)) {
                Integer addCount = terminalService.insertTerminal(terminalListAdd);
                LoggerUtils.info(log, "终端设置表中增加成功的数据增量：【" + addCount + "】");
            }
            List<TerminalStatus> terminalListCurrent = terminalService.getTerminalObjList();
            LoggerUtils.info(log, "终端设置表中增加数据后的数据量：【" + terminalListCurrent.size() + "】");
            //terminalListCurrent.clear();
            if (CollectionUtils.isEmpty(terminalListCurrent)) {
                return;
            }
            for (TerminalStatus terminalStatus : terminalListCurrent) {
                if (!ObjectUtils.isEmpty(terminalStatus) && !StringUtils.isEmpty(terminalStatus.getTerminalId()) && terminalStatus.getOptionsSuc() != 1) {
                    String terminalId = terminalStatus.getTerminalId();
                    try {
                        ResultVo<TerminalParams> terminalArgsBefore = null;
                        int terminalArgsBeforeIndex = 0;
                        while (terminalArgsBeforeIndex < 6){
                            terminalArgsBefore = optionsService.readTerminalArgs(terminalId);
                            if (!ObjectUtils.isEmpty(terminalArgsBefore) && terminalArgsBefore.isSuccess()) {
                                break;
                            }
                            ++terminalArgsBeforeIndex;
                        }
                        if (!ObjectUtils.isEmpty(terminalArgsBefore) &&
                                terminalArgsBefore.isSuccess() &&
                                !ObjectUtils.isEmpty(terminalArgsBefore.getData()) &&
                                !StringUtils.isEmpty(terminalArgsBefore.getData().getMainIp())) {
                            if (!terminalIp.equals(terminalArgsBefore.getData().getMainIp())) {
                                terminalArgsBefore.getData().setMainIp(terminalIp);
                                ResultVo<String> setTerminalArgs = null;
                                int setTerminalArgsIndex = 0;
                                while (setTerminalArgsIndex < 6){
                                    setTerminalArgs = optionsService.setTerminalArgs(terminalId, terminalArgsBefore.getData());
                                    if (!ObjectUtils.isEmpty(setTerminalArgs) && setTerminalArgs.isSuccess()) {
                                        break;
                                    }
                                    ++setTerminalArgsIndex;
                                }
                                if (!ObjectUtils.isEmpty(setTerminalArgs) && setTerminalArgs.isSuccess()) {
                                    Thread.sleep(40 * 1000);
                                    ResultVo<TerminalParams> terminalArgsAfter = null;
                                    int terminalArgsAfterIndex = 0;
                                    while (terminalArgsAfterIndex < 6){
                                        terminalArgsAfter = optionsService.readTerminalArgs(terminalId);
                                        if (!ObjectUtils.isEmpty(terminalArgsAfter) &&
                                                terminalArgsAfter.isSuccess()){
                                            break;
                                        }
                                        ++terminalArgsAfterIndex;
                                    }
                                    if (!ObjectUtils.isEmpty(terminalArgsAfter) &&
                                            terminalArgsAfter.isSuccess() &&
                                            !ObjectUtils.isEmpty(terminalArgsAfter.getData()) &&
                                            !StringUtils.isEmpty(terminalArgsAfter.getData().getMainIp()) &&
                                            terminalIp.equals(terminalArgsAfter.getData().getMainIp())) {//设置成功
                                        LoggerUtils.info(log, terminalId, "终端参数更新成功！");
                                        boolean updateTerminalData = terminalService.updateTerminalData(terminalId);
                                        if (updateTerminalData) {
                                            LoggerUtils.info(log, terminalId, "终端数据数据库更新成功！");
                                        } else {
                                            LoggerUtils.error(log, terminalId, "终端数据数据库更新失败！");
                                        }
                                    } else {
                                        LoggerUtils.error(log, terminalId, "终端数据更新失败！");
                                    }
                                } else {
                                    LoggerUtils.error(log, terminalId, "终端数据更新后读取失败！");
                                }
                            } else {
                                LoggerUtils.info(log, terminalId, "终端数据无需更改！");
                            }
                        } else {
                            LoggerUtils.info(log, terminalId, "终端数据更新前读取失败！");
                        }
                    } catch (Exception e) {
                        LoggerUtils.error(log, terminalId, "终端数据更新异常：【" + e + "】");
                    }
                }
            }
        } catch (Exception e) {
            log.error("AmqpInfoRunner.run is error", e);
        }
        LoggerUtils.info(log,"自动更新终端参数执行完毕！！！！！！！！");
    }

    private List<String> getNeedAddOpenidList(List<String> allOpenidList, List<String> dbOpenidList) {
        if (dbOpenidList != null && !dbOpenidList.isEmpty()) {
            Map<String, String> dataMap = new HashMap<>();
            for (String id : dbOpenidList) {
                dataMap.put(id, id);
            }

            List<String> newList = Lists.newArrayList();
            for (String id : allOpenidList) {
                if (!dataMap.containsKey(id)) {
                    newList.add(id);
                }
            }
            return newList;
        } else {
            return allOpenidList;
        }
    }

}
