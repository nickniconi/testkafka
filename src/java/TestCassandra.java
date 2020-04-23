import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class TestCassandra {
    public static void main(String[] args){
        Cluster.Builder cluster=Cluster.builder().addContactPoint("192.168.8.250");
        Cluster cluster1 = cluster.build();
        Session session = cluster1.connect();
        String query = "create table tp.test2(" +
                "id int primary key," +
                "name text," +
                "address text);";
        String insert="insert into tt.test1(id,name) values(1,'JW');";
        session.execute(query);
        session.execute(insert);
        session.close();
    }
}
