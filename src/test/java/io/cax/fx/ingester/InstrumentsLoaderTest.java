package io.cax.fx.ingester;

import io.cax.fx.ingester.domain.Instrument;
import io.cax.fx.ingester.domain.InstrumentsProcessor;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by cq on 12/5/16.
 */
public class InstrumentsLoaderTest {

    @Test
    public void testLoadInstruments() throws Exception {
        InstrumentsProcessor loader = new InstrumentsProcessor();
        loader.init();
        List<Instrument> instruments = loader.getInstruments();

        assertThat(instruments.size(),equalTo(122));

    }

    @Test
    public void testInstrumentsJoin() throws Exception {
        InstrumentsProcessor loader = new InstrumentsProcessor();
        loader.init();
        String instrumentsAsString = loader.getInstruments().stream().map(i-> i.getInstrumentName()).collect(Collectors.joining(","));
        assertThat(instrumentsAsString.length(),equalTo(1028));
    }
}
