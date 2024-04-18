package skrifer.github.com;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;

/**
 * 本类用于向管理进程发送Trap信息
 *
 * @author
 *
 */
public class SnmpUtilSendTrap {
    private Snmp snmp = null;

    private Address targetAddress = null;

    @SuppressWarnings("rawtypes")
    public void initComm() throws IOException {

        // 设置管理进程的IP和端口
        targetAddress = GenericAddress.parse("udp:127.0.0.1/162");//such as：192.168.0.1
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();

    }

    /**
     * 向管理进程发送Trap报文
     *
     * @throws IOException
     */
    public void sendPDU() throws IOException {

        // 设置 target
        CommunityTarget target = new CommunityTarget();
        target.setAddress(targetAddress);

        // 通信不成功时的重试次数
        target.setRetries(2);
        // 超时时间
        target.setTimeout(1500);
        target.setCommunity(new OctetString("Public"));
        // snmp版本
        target.setVersion(SnmpConstants.version2c);

        // 创建 PDU
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.1.1.1.1"), new OctetString("SnmpTrap-2 Good Morning")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.1.1.1.3.1"), new OctetString("Good Afternoon Hello Word!!!")));
        pdu.setType(PDU.TRAP);

        // 向Agent发送PDU，并接收Response
        ResponseEvent respEvnt = snmp.send(pdu, target);

        // 解析Response
        if (respEvnt != null && respEvnt.getResponse() != null) {
            Vector<? extends VariableBinding> recVBs = respEvnt.getResponse().getVariableBindings();
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                System.out.println(recVB.getOid() + " : " + recVB.getVariable());
            }
        }
    }

    public static void main(String[] args) {
        try {
            SnmpUtilSendTrap util = new SnmpUtilSendTrap();
            util.initComm();
            util.sendPDU();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
