package io.cax.fx.ingester.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cq on 17/4/16.
 */

public class Instrument {


    @JsonProperty("instrument")
    private String instrumentName;

    private String displayName;

    private double pip;

    private long maxTradeUnits;


    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public double getPip() {
        return pip;
    }

    public void setPip(double pip) {
        this.pip = pip;
    }

    public long getMaxTradeUnits() {
        return maxTradeUnits;
    }

    public void setMaxTradeUnits(long maxTradeUnits) {
        this.maxTradeUnits = maxTradeUnits;
    }
}
