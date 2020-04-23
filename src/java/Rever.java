
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Rever {
    private final ConsumerConnector consumer;
    public Rever(){
        Properties props=new Properties();
        props.put("zookeeper.connect","192.168.8.250:2181");
        props.put("group.id","jd-group");
        props.put("zookeeper.session.timeout.ms","4000");
        props.put("zookeeper.sync.time.ms","200");
        props.put("auto.commit.interval.ms","1000");
        props.put("serializer.class","kafka.serializer.StringEndocer");
        ConsumerConfig config=new ConsumerConfig(props);

        consumer=kafka.consumer.Consumer.createJavaConsumerConnector(config);
    }
    void consumer(){
        Map<String,Integer> topicMap=new HashMap();
        topicMap.put("TEST-TOPIC",new Integer(1));
        StringDecoder keyDecoder= new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder=new StringDecoder(new VerifiableProperties());
        Map<String, List<KafkaStream<String,String>>> consumerMap=
                consumer.createMessageStreams(topicMap,keyDecoder,valueDecoder);
        KafkaStream<String,String> kafkaStream=consumerMap.get("TEST-TOPIC").get(0);
        ConsumerIterator<String,String> it = kafkaStream.iterator();
        while (it.hasNext()){
            System.out.println(it.next().key());
        }
    }
    public static void main(String[] args){
        new Rever().consumer();
    }
}
