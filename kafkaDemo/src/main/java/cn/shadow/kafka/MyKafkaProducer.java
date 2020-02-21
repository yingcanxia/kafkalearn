package cn.shadow.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import ch.qos.logback.core.util.TimeUtil;

public class MyKafkaProducer extends Thread{

	//这里是api
	KafkaProducer<Integer, String> producer;
	String topic;//主题
	public MyKafkaProducer(String topic) {
		// 比如说通过new方法通过工厂模式
		// 创建连接的话必然要有连接的字符串
		// 以properties为例的话
		this.topic = topic;
		Properties properties=new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.132:9092");// 在这里可以设置多个字符串
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producer");
		// 声明序列化方式
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());// key值的序列化
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());// value值的序列化
		producer=new KafkaProducer<Integer, String>(properties);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int num=0;
		String msg="my kafka practice msg"+num;
		while(num<20) {
			num++;
			try {
				// get会拿到发送的结果
				RecordMetadata recordMetadata= producer.send(new ProducerRecord<Integer, String>(topic, msg)).get();
				System.out.println(recordMetadata.offset()+"->"+recordMetadata.partition()+"->");
				//TimeUtil.SECONDS.
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//send方法有重载一个有回调，一个没有回调
		}
	}
	
	
}
