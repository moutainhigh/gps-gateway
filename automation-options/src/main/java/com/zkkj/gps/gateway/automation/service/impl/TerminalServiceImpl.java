package com.zkkj.gps.gateway.automation.service.impl;

import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.zkkj.gps.gateway.automation.entity.terminal.Terminal;
import com.zkkj.gps.gateway.automation.entity.terminal.TerminalStatus;
import com.zkkj.gps.gateway.automation.mapper.TerminalMapper;
import com.zkkj.gps.gateway.automation.service.TerminalService;
import com.zkkj.gps.gateway.automation.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class TerminalServiceImpl implements TerminalService {

    @Autowired
    private TerminalMapper terminalMapper;

    @Override
    public List<String> getTerminalListFromLast() {
        List<String> terminalListFromLast = terminalMapper.getTerminalListFromLast();
        if (!CollectionUtils.isEmpty(terminalListFromLast)){
            return terminalListFromLast;
        }
        return null;
    }

    @Override
    public Integer insertTerminal(List<String> terminals) {
        try {
            List<Terminal> terminalIds = null;
            if (!CollectionUtils.isEmpty(terminals)){
                terminalIds = Lists.newArrayList();
                for (String terminalId : terminals) {
                    Terminal terminal = new Terminal();
                    terminal.setTerminalId(terminalId);
                    terminalIds.add(terminal);
                }
            }
            if (CollectionUtils.isEmpty(terminalIds)){
                return 0;
            }
            Integer insertTerminal = terminalMapper.insertTerminal(terminalIds);
            LoggerUtils.info(log,"本次新增的数据量：【" + insertTerminal + "】");
            return insertTerminal;
        } catch (Exception e){
            LoggerUtils.error(log,"本次新增数据异常：【" + e + "】");
        }
        return 0;
    }

    @Override
    public List<String> getTerminalList() {
        return terminalMapper.getTerminalList();
    }

    @Override
    public List<TerminalStatus> getTerminalObjList() {
        return terminalMapper.getTerminalObjList();
    }

    @Override
    public boolean updateTerminalData(String terminalId) {
        if (StringUtils.isEmpty(terminalId)){
            LoggerUtils.info(log,"终端号为空");
            return false;
        }
        try {
            if (terminalMapper.updateTerminalData(terminalId) > 0){
                LoggerUtils.info(log,terminalId,"更新设备数据成功");
                return true;
            }
        } catch (Exception e){
            LoggerUtils.error(log,terminalId,"更新设备数据异常：【" + e + "】");
        }
        return false;
    }
}
