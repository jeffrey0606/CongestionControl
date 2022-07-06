package com.example.congestioncontrol.NBPInRouter;

public class RateSeriesDataModel {
    private Number packetNum;
    private Number rateAtInRouter;

    public RateSeriesDataModel(Number packetNum, Number rateAtInRouter) {
        this.rateAtInRouter = rateAtInRouter;
        this.packetNum = packetNum;
    }

    public Number x() {
        return packetNum;
    }

    public Number y() {
        return rateAtInRouter;
    }
}
