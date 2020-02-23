package cn.shadow.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class MyKafkaConsumer extends Thread{

	KafkaConsumer<Integer, String>consumer;
	String topic;
	public MyKafkaConsumer(String topic) {
		// 开始构建
		Properties properties=new Properties();
		// 在这里可以设置多个字符串
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.132:9092");
		// 设置客户端id
		properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "consumer");
		// 设置组id，用groupid区分消费组，从而可以实现信息的发布与订阅
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "gid");
		properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "300000");
		// 设置自动提交的间隔单位是毫秒,这里是批量确认
		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		// key值的序列化
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
		// value值的序列化
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		/* 这个属性可以消费昨天帆布的数据
		 * 一个新的group的消费者要去消费topic的信息：
		 * earliest：消费该队列中最早存在的信息
		 * 
		 *  
		 */
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");//  
		consumer=new KafkaConsumer<Integer, String>(properties);
		this.topic = topic;
	}
	@Override
	public void run() {
		// 重写的run方法
		while(true) {
			consumer.subscribe(Collections.singleton(this.topic));
			ConsumerRecords<Integer, String>consumerRecords=consumer.poll(Duration.ofSeconds(1));
			consumerRecords.forEach(record->{
				System.out.println(record.key()+"->"+record.value()+"->"+record.offset());
			});
		}
		
	}
	public static void main(String[] args) {
		new MyKafkaConsumer("test").start();
	}
}
