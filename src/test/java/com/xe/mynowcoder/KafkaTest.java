package com.xe.mynowcoder;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xjx
 * @DATE 2022/8/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MyNowCoderApplication.class)
public class KafkaTest {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    KafkaConsumer kafkaConsumer;
    @Test
    public void testKafka(){
        kafkaProducer.sendMessage("test","hello");
        kafkaProducer.sendMessage("test","yyg");
        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

@Component
class KafkaProducer{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic ,String content){

        kafkaTemplate.send(topic,content);
    }
}
@Component
class KafkaConsumer{

    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord record){
        System.out.println(record.value());
    }
}