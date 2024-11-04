package com.example.springBoot;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
public class WikimediaChangesProducer {
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    private static final Logger LOGGER= LoggerFactory.getLogger(WikimediaChangesProducer.class);


    private KafkaTemplate<String, String> kafkaTemplate;

    //@AutoWired
    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {
        String topic=topicName;
        //to read real time stream data from wikimedia, we use event source
        //this event will be trigger when ever there is change in the Wikimedia
        EventHandler eventHandler=new WikimediaChangesHandler(kafkaTemplate,topic);
        //Define Event source URL
        String url="https://stream.wikimedia.org/v2/stream/recentchange";
        //Event Source which connect to event source
        // i.e, Wikimedia source and it will read all the event data
        EventSource.Builder builder=new EventSource.Builder(eventHandler, URI.create(url));
        //Event source object
        EventSource eventSource=builder.build();
        //Now start this event source in seperate thread
        eventSource.start();
        TimeUnit.MINUTES.sleep(10);
    }
}
