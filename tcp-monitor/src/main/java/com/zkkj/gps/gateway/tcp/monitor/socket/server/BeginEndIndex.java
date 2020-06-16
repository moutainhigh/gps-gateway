package com.zkkj.gps.gateway.tcp.monitor.socket.server;

public class BeginEndIndex
{
    private final int endDelimiterLength;

    public BeginEndIndex(int beginIndex, int endIndex, int endDelimiterLength) {
        this.setBeginIndex(beginIndex);
        this.setEndIndex(endIndex);
        this.endDelimiterLength=endDelimiterLength;
    }

    public int getBeginIndex() {
        return beginIndex;
    }
    public int getFrameLength() {
        return endIndex-beginIndex+endDelimiterLength;
    }
    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    private int beginIndex;
    private int endIndex;
}
