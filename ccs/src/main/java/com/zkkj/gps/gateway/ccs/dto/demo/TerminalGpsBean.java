package com.zkkj.gps.gateway.ccs.dto.demo;

import lombok.Data;

@Data
public class TerminalGpsBean {

    private String terminalId;
    private Double lat;
    private Double lon;

    public TerminalGpsBean(String terminalId, Double lat, Double lon) {
        this.terminalId = terminalId;
        this.lat = lat;
        this.lon = lon;
    }
}
