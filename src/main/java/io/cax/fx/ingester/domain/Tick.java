package io.cax.fx.ingester.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by cq on 15/4/16.
 * {"tick":{"instrument":"EUR_USD","time":"1461396366924145","bid":1.13929,"ask":1.14029}}
 */
@JsonRootName(value = "tick")
public class Tick {

    @JsonProperty("time")
    private long time;

    private String instrument;

    private double bid;

    private double ask;

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long dateTime) {
        this.time = dateTime;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tick{");
        sb.append("instrument='").append(instrument).append('\'');
        sb.append(", time=").append(time);
        sb.append(", bid=").append(bid);
        sb.append(", ask=").append(ask);
        sb.append('}');
        return sb.toString();
    }
}