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
	 * range：假如说现在有10（0-9）个分区3个消费者，
	 * n=分区数量/消费者适量10/3=3
	 * m=分区数量%消费者数量10%3=1
	 * 前m个消费者会分配n+1个分区
	 * 后面的消费n个分区
	 * 消费者1消费0-3，消费者2消费4-6，消费者3消费7-9
	 * roundRobin：轮询
	 * 按照分区hashCode，然后依次轮询指派给消费者
	 * 只要不出发重新平衡，消费者指定访问的分区是不会变得
	 * stricky：保持相对粘性，原来这几个消费者消费哪个分区尽量保持不动
	 * 尽可能均匀
	 * 例三个消费者，4*topic，每个topic两个分区
	 * 组合起来是t0p0,t0p1,t1p0,t1p1,t2p0,t2p1,t3p0,t3p1
	 * c1 :t0p0,t1p1
	 * c2 :t0p1,t2p0 .......
	 * c3 :t1p0,t2p1
	 * 
	 * kafka集群中会存在一个coordinator
	 * 当请求groupCoordinatorRequest的时候返回最小负载的broker节点的id这个id就是coordinator
	 * 
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
