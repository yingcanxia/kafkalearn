package cn.shadow.kafka;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import cn.shadow.kafka.upgrade.MyKafkaProducer;

@SpringBootApplication
public class KafkaDemoApplication {

	/**
	 * Producer想卡夫卡服务器发送消息，这些消息以topic进行归类存储，但是topic却是个逻辑上个概念
	 * kafka是存在分区的在存储的时候一般使用的是topic-分区号
	 * 如果没有做分区策略的话默认只有一个分区，再多消费者只能是浪费只有一个消费者能拿到
	 * 消费策略：rounbin,range,strick
	 */
	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context=SpringApplication.run(KafkaDemoApplication.class, args);
		MyKafkaProducer kp=context.getBean(MyKafkaProducer.class);
		for (int i = 0; i < 10; i++) {
			kp.send();
			 TimeUnit.SECONDS.sleep(2);
		}
		
	}

}
