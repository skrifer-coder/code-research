package skrifer.github.com;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class SnmpManager {
    private static Snmp snmp = null;
    private static CommunityTarget target = null;
    private static Integer port = 161;
    private static String ip = "172.16.235.11";
    private static String communityName = "public";

    public static void main(String[] args) throws Exception {
        SnmpManager snmpManager = new SnmpManager(ip, port);
        Map<String, String> bulk = snmpManager.getBulk(SnmpCommand.KedaKDV8000ASystemDesc.getOid(), 0, 10);
        System.out.println(bulk);
        log.info("=================美丽的分割线===================");

        ResponseEvent responseEvent = snmpManager.snmpGet("1.3.6.1.4.1.8888.1.1.1.6.2.0");
//        ResponseEvent responseEvent = snmpManager.snmpGet(SnmpCommand.KedaKDV8000ASystemTime.getOid());
        PDU response = responseEvent.getResponse();
        for (int i = 0; i < response.size(); i++) {
            VariableBinding vb1 = response.get(i);
            System.out.println(vb1.getOid().toString() + "===" + vb1.getVariable());
        }
        snmpManager.close();

    }

    public SnmpManager(String intranetDeviceIp, Integer snmpPort) throws IOException {
        if (snmp == null) {
            snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();
        }
        //初始化CommunityTarget
        target = new CommunityTarget();
        target.setCommunity(new OctetString(communityName));
        target.setVersion(SnmpConstants.version2c);
        target.setAddress(new UdpAddress(intranetDeviceIp + "/" + snmpPort));
        target.setTimeout(1000);
        target.setRetries(3);
    }

    private ResponseEvent snmpGet(String oid) {
        PDU pdu = new PDU();
        pdu.addOID(new VariableBinding(new OID(oid)));
        ResponseEvent re = null;
        try {
            re = snmp.get(pdu, target);
        } catch (Exception e) {
            log.error("snmpGet 异常" + e.getMessage());
        }
        return re;
    }

    public Map<String, String> getBulk(String oid, int nonRepeaters, int maxRepetitions) throws Exception {
        Map<String, String> result = null;
        OID targetOID = new OID(oid);

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(targetOID));
        pdu.setType(PDU.GETBULK);
        pdu.setMaxRepetitions(maxRepetitions);
        pdu.setNonRepeaters(nonRepeaters);

        ResponseEvent responseEvent = snmp.getBulk(pdu, target);
        PDU response = responseEvent.getResponse();

        if (response != null && response.getVariableBindings() != null) {
            result = new HashMap<>();
            for (VariableBinding variableBinding : response.getVariableBindings()) {
                result.put(variableBinding.getOid().toString(), convertFrom16HEX(variableBinding.getVariable().toString()));
            }
        } else {
//            throw new Exception("SNMP request timed out");
            log.error("SNMP getBulk request timed out");
        }
        return result;
    }

    public void close() throws Exception {
        snmp.close();
    }

    public String convertFrom16HEX(String octetString) {
        if (octetString.contains(":") == false) {
            return octetString;
        }
        try {
            List<String> temps = Arrays.stream(octetString.split(":")).filter(e -> "00".equals(e) == false).collect(Collectors.toList());
            byte[] bs = new byte[temps.size()];
            for (int i = 0; i < temps.size(); i++)
                bs[i] = (byte) Integer.parseInt(temps.get(i), 16);

            return new String(bs, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }


//    private static List<TableEvent> snmpWalk(String oid) {
//        TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));
//        OID[] columnOid = new OID[]{new OID(oid)};
//        return utils.getTable(target, columnOid, null, null);
//    }
}
