import kafka.security.auth.Create;

import javax.management.*;
import javax.management.remote.*;
import javax.security.auth.Subject;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;
import java.util.*;

public class HellowService {
    private static final int RMI_PROT=8099;
    private static final String JMX_SERVER_NAME="TestJMXServer";
    private static final String USER_NAME="hello";
    private static final String PASS_WORD="world";

    public static void main(String[] args) throws Exception {
        HellowService hellowService = new HellowService();
        hellowService.startJMXServer();
    }
    private void startJMXServer() throws Exception{
        MBeanServer mBeanServer=getMBeanServer();
        LocateRegistry.createRegistry(RMI_PROT,null,RMISocketFactory.getDefaultSocketFactory());
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:"+RMI_PROT+"/"+JMX_SERVER_NAME);
        System.out.println("JMX"+url.toString());
        Map<String,?> env=this.putAuthenticator();
        JMXConnectorServer jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url,env,mBeanServer);
        jmxConnectorServer.start();
    }
    private MBeanServer getMBeanServer() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName=new ObjectName(JMX_SERVER_NAME+":name="+"hello");
        mBeanServer.registerMBean(new Hello(),objectName);
        return mBeanServer;
    }
    private Map<String,Object> putAuthenticator(){
        Map<String,Object> env=new HashMap<>();
        JMXAuthenticator auth = CreateJMXAuthentiator();
        env.put(JMXConnectorServer.AUTHENTICATOR,auth);
        env.put("com.sun.jndi.rmi.factory,socket", RMISocketFactory.getDefaultSocketFactory());
        return env;
    }

    private JMXAuthenticator CreateJMXAuthentiator() {
        return  new JMXAuthenticator() {
            @Override
            public Subject authenticate(Object credentials) {
                String[] clist=(String[]) credentials;
                if(null==clist||clist.length!=2)
                {
                    throw new SecurityException("账号或是密码不能为空");
                }
                String username=clist[0];
                String password=clist[1];
                if(USER_NAME.equals(username)&&PASS_WORD.equals(password)){
                    Set<JMXPrincipal> jmxPrincipalSet = new HashSet<>();
                    jmxPrincipalSet.add(new JMXPrincipal(username));
                    return new Subject(true,jmxPrincipalSet, Collections.EMPTY_SET,Collections.EMPTY_SET);
                }
                throw new SecurityException("账号密码错误");
                //return null;
            }
        };
    }
}
