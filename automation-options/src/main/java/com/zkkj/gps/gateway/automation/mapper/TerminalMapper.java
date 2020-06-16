package com.zkkj.gps.gateway.automation.mapper;

import com.zkkj.gps.gateway.automation.entity.terminal.Terminal;
import com.zkkj.gps.gateway.automation.entity.terminal.TerminalStatus;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TerminalMapper {

    List<String> getTerminalList();

    List<TerminalStatus> getTerminalObjList();

    Integer updateTerminalData(String terminalId);

    List<String> getTerminalListFromLast();

    Integer insertTerminal(List<Terminal> terminals);

}
