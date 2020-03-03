package cn.shadow.kafka.upgrade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyKafkaProducer {

	@Autowired
	private KafkaTemplate<String, String>kafkaTemplate;
}
