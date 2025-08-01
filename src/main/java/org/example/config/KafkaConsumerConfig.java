package org.example.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Configuration
@EnableKafka
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumerConfig {

    String KAFKA_BOOTSTRAP_SERVICES;

    public KafkaConsumerConfig(
        @Value("${SPRING_KAFKA_BOOTSTRAP_SERVICES}") String KAFKA_BOOTSTRAP_SERVICES
    ){
        this.KAFKA_BOOTSTRAP_SERVICES = KAFKA_BOOTSTRAP_SERVICES;
    }

  public ConsumerFactory<String, String> consumerFactory(){
    Map<String, Object> configProperties = new HashMap<>();   
    configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVICES);
    configProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(configProperties);
  } 

  public ConcurrentKafkaListenerContainerFactory<String,String>
    kafkaListenerContainerFactory(
      ConsumerFactory<String, String> consumerFactory
  ){
    var kafkaListenerContainerFactory =
      new ConcurrentKafkaListenerContainerFactory<String, String>();

      kafkaListenerContainerFactory.setConcurrency(1);
      kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
      return kafkaListenerContainerFactory;
  }
  


  

}
