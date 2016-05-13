package io.cax.fx.ingester;

import io.cax.fx.ingester.domain.InstrumentsProcessor;
import io.cax.fx.ingester.domain.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.google.common.math.DoubleMath.mean;
import static io.cax.fx.ingester.Utils.convertToTick;

/**
 * Created by cq on 12/5/16.
 */

@EnableBinding(TickSourceOanda.MultiOutputSource.class)
public class TickSourceOanda {

    Logger logger = LoggerFactory.getLogger(TickSourceOanda.class);

    @Autowired
    private MultiOutputSource channels;


    @Value("${oanda.access_token}")
    private String accessToken;

    @Value("${oanda.account_id}")
    private String accountId;


    @Value("${oanda.domain}")
    private String domain;


    private final RestOperations restTemplate = new RestTemplate();

    private InstrumentsProcessor loader;

    @Autowired
    public void setLoader(InstrumentsProcessor loader) {
        this.loader = loader;
    }


    private CounterService counterService;

    @Autowired
    public void setCounterService(CounterService counterService) {
        this.counterService = counterService;
    }


    private GaugeService gaugeService;

    @Autowired
    public void setGaugeService(GaugeService service) {
        this.gaugeService = service;
    }

    private AtomicBoolean running = new AtomicBoolean();

    @Async
    public void start(){

        running.compareAndSet(false,true);

        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(domain)
                .path("/v1/prices")
                .queryParam("accountId",accountId)
                .queryParam("instruments",loader.getInstruments().stream().map(i-> i.getInstrumentName()).collect(Collectors.joining(","))).build();


        while(running.get()){

            try{

                logger.info("FX Oanda Tick service started!!");

                restTemplate.execute(uriComponents.toUriString(),
                        HttpMethod.GET,
                        clientHttpRequest -> setHeaders(clientHttpRequest),
                        clientHttpResponse -> {
                            try(Scanner scanner = new Scanner(clientHttpResponse.getBody(),"utf-8")){

                                while(scanner.hasNext()) processData(scanner.nextLine());
                            }
                            return new ResponseEntity<>(HttpStatus.OK);
                        });

            }catch(Exception e){

                logger.error("Error reading from the remote connection",e);
            }
        }

    }

    /**
     * Stops the data streaming
     */
    public void stop(){

        running.compareAndSet(true, false);

    }

    /**
     * Processes the data and saves the tick if pertinent. Hearbeat ticks are not saved.
     * @param content
     */
    private void processData(String content){

        try{

            Tick tick = convertToTick(content);

            if(tick!=null){

                Message message = MessageBuilder.withPayload(content).build();
                channels.output0().send(message);
                channels.output1().send(message);

                counterService.increment("tick");
                gaugeService.submit(tick.getInstrument(),mean(tick.getAsk(),tick.getBid()));

            }

        } catch(Exception e){
            logger.error("Error processing tick: " + e.getMessage());
        }

    }


    private ClientHttpRequest setHeaders(ClientHttpRequest clientHttpRequest){
        clientHttpRequest.getHeaders().add("X-Accept-Datetime-Format","UNIX");
        clientHttpRequest.getHeaders().add("Authorization","Bearer " + accessToken);
        return clientHttpRequest;
    }

    /**
     * Created by cq on 12/5/16.
     */
    public interface MultiOutputSource {

        String OUTPUT0 = "output0";

        String OUTPUT1 = "output1";

        @Output(MultiOutputSource.OUTPUT0)
        MessageChannel output0();

        @Output(MultiOutputSource.OUTPUT1)
        MessageChannel output1();
    }
}
