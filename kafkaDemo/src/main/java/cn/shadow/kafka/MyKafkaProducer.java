package cn.shadow.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

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
		// 默认是批量发送，会存在频繁的网络通信，没有批量发送出去之前，都是存在于内存之中的
		// properties.put(ProducerConfig.BATCH_SIZE_CONFIG,"");
		// 两次发送的间隔时间，这两个条件谁先满足了，谁就开始做
		// properties.put(ProducerConfig.LINGER_MS_CONFIG, "");
		// 指定分区部分
		properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "cn.shadow.kafka.upgrade.MyPartition");
		// 声明序列化方式，序列化方式是根据具体类型进行的，因为kafka服务器是需要处理不同语言的
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());// key值的序列化
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());// value值的序列化
		producer=new KafkaProducer<Integer, String>(properties);
	}
	@Override
	public void run() {
		int num=0;
		while(num<20) {
			String msg="my kafka practice msg"+num;
			num++;
			try {
				// get会拿到发送的结果,这个是阻塞方法
				//RecordMetadata recordMetadata= producer.send(new ProducerRecord<Integer, String>(topic, msg)).get();
				// 同步get -> future()
				RecordMetadata recordMetadata= producer.send(new ProducerRecord<Integer, String>(topic, msg),(metadata,exception)->{
					// 使用异步的回调通知
				}).get();
				System.out.println(recordMetadata.offset()+"->"+recordMetadata.partition()+"->"+recordMetadata.topic());
				TimeUnit.SECONDS.sleep(2);
				++num;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//send方法有重载一个有回调，一个没有回调
		}
	}
	public static void main(String[] args) {
		new MyKafkaProducer("test").start();
	}
	
}
