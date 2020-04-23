import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.sql.SQLOutput;
import java.util.Properties;

public class TestKafka {
    private final Producer<String,String> producer;
    private final static String TOPIC="TEST-TOPIC";
    private TestKafka(){
        Properties props=new Properties();
        props.put("metadata.broker.list","192.168.8.250:9092");

        props.put("serializer.class","kafka.serializer.StringEncoder");

        props.put("key.serializer.class","kafka.serializer.StringEncoder");

        props.put("request.required.acks","1");

        producer=new Producer<String, String>(new ProducerConfig(props));
    }
    void producer(){
        int messageNo=1000;
        final int count=10000;
        while (messageNo<count){
            String key=String.valueOf(messageNo);
            String data="hellow"+key;
            producer.send(new KeyedMessage<String, String>(TOPIC,key,data));
            System.out.println(data);
            messageNo++;
        }
    }
    public static  void main(String[] args){
        new TestKafka().producer();
    }
}
