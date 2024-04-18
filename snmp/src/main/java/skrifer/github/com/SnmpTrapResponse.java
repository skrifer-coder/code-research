package skrifer.github.com;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
public class SnmpTrapResponse implements CommandResponder {
    private MultiThreadedMessageDispatcher dispatcher;
    private Snmp snmp = null;
    private Address listenAddress;
    private ThreadPool threadPool;

    /*public SnmpTrapResponse() {
        BasicConfigurator.configure();
    }*/

    @SuppressWarnings("rawtypes")
    private void init() throws UnknownHostException, IOException {
//        InetAddress inet = InetAddress.getLocalHost();
        String IP = "0.0.0.0";//inet.getHostAddress();
        threadPool = ThreadPool.create("Trap", 2);
        dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
        listenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress", IP+"/162"));
        System.out.println(IP);                                                                                                                // 监听IP与监听端口
        TransportMapping transport;
        // 对TCP与UDP协议进行处理
        if (listenAddress instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
        } else {
            transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
        }
        snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.listen();
    }

    public void run() {
        try {
            init();
            snmp.addCommandResponder(this);
            System.out.println("开始监听Trap信息!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 实现CommandResponder的processPdu方法, 用于处理传入的请求、PDU等信息 当接收到trap时，会自动进入这个方法
     *
     * @param respEvnt
     */
    public void processPdu(CommandResponderEvent respEvnt) {
        // 取设备的IP地址
        String IP = respEvnt.getPeerAddress().toString();
        System.out.println("***************************");

        String ip[] = IP.split("/");
        System.out.println("设备的IP地址:" + ip[0]);
        PDU command = respEvnt.getPDU();
        System.out.println("PDU信息:" + command.toString());
        GregorianCalendar gcal = new GregorianCalendar(); // 获得当前时间
        // 设定格式yyyy-mm-dd hh:mm:ss
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = gcal.getTime(); // 将当前时间转成日期对象
        String datetime = df.format(date2); // 获得符合格式的字符串，当前日期时间
        System.out.println("当前日期:" + datetime);
        // 解析Response
        if (respEvnt != null && respEvnt.getPDU() != null) {
            Vector<? extends VariableBinding> recVBs = respEvnt.getPDU().getVariableBindings();
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                System.out.println(recVB.getOid() + " : " + recVB.getVariable());
            }
        }

    }

    public static void main(String[] args) {
        SnmpTrapResponse multithreadedtrapreceiver = new SnmpTrapResponse();
        multithreadedtrapreceiver.run();
    }
}
