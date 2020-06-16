package com.zkkj.gps.gateway.automation.service;

import com.zkkj.gps.gateway.automation.entity.terminal.TerminalStatus;

import java.util.List;

public interface TerminalService {

    List<String> getTerminalListFromLast();

    Integer insertTerminal(List<String> terminals);

    List<String> getTerminalList();

    List<TerminalStatus> getTerminalObjList();

    boolean updateTerminalData(String terminalId);

}
