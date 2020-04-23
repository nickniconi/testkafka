import org.apache.kafka.clients.admin.TopicListing;

public interface HelloMBean {
    String getName();
    void setName(String name);
    void print();
}
