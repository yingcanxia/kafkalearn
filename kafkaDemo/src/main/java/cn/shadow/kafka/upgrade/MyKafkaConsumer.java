package cn.shadow.kafka.upgrade;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MyKafkaConsumer{
	// 组件一般情况上不需要我们进行调优，如果调优的话公司会自行开发组件
	
	@KafkaListener(topics= {"test"})
	public void listener(ConsumerRecord record) {
		Optional msg= Optional.ofNullable(record.value());
		if(msg.isPresent()) {
			System.out.println(msg.get());
		}
	}
}
