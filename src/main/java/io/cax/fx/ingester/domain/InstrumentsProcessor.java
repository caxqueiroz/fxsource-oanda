package io.cax.fx.ingester.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cq on 22/4/16.
 */
@Component
public class InstrumentsProcessor {

    Logger logger = LoggerFactory.getLogger(InstrumentsProcessor.class);

    List<Instrument> instruments;


    @PostConstruct
    public void init(){

        ObjectMapper mapper = new ObjectMapper();
        try{

            Instrument[] arraysOfInstruments = mapper.readValue(
                    InstrumentsProcessor.class.getResource("/instruments.json"),Instrument[].class);
            instruments = Arrays.asList(arraysOfInstruments);

        }catch(Exception e){
            logger.error("Error loading instruments",e);
        }

    }

    public List<Instrument> getInstruments() {
        return instruments;
    }
}
