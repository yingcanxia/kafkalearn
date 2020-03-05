package cn.shadow.kafka.upgrade;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

/**
 * 如果消费者动态增减删除，或者分区书动态处理
 * @author notto
 *
 */
public class MyPartition implements Partitioner{

	@Override
	public void configure(Map<String, ?> configs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		// 设置存储到哪个分区，可以做消息有序性
		// 拿到所有的分区，经计算之后拿到那个分区就发给那个分区
		List<PartitionInfo>list=cluster.partitionsForTopic(topic);
		int lenth=list.size();
		if(key==null) {
			Random random=new Random();
			return random.nextInt(lenth);
		}
		return Math.abs(key.hashCode())%lenth;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	
}
