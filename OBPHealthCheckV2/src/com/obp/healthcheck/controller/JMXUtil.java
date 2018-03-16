package com.obp.healthcheck.controller;
import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
 
public class JMXUtil {
 
    private static JMXConnector connector = null;
    
    private static final String USERNAME = "weblogic";
    private static final String PASSWORD = "weblogic1";
 
    public static void doJRockitDiagnosticAction(String urlPath, String diagnostic, Boolean authenticate) {
        try {
            MBeanServerConnection connection = getMBeanServerConnection(urlPath, authenticate);
 
            ObjectName name = new ObjectName("oracle.jrockit.management:type=DiagnosticCommand");
            String operationName = "execute";
            Object[] parameters = new Object[]{diagnostic};
            String[] signature = new String[]{"java.lang.String"};
 
            Object result = connection.invoke(name, operationName, parameters, signature);
 
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String filename = "output_" + timeStamp + ".txt";
            PrintWriter writer = new PrintWriter(filename);
            writer.println(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnectors();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    public static void doGarbageCollect(String urlPath, Boolean authenticate) {
        try {
            MBeanServerConnection connection = getMBeanServerConnection(urlPath, authenticate);
 
            ObjectName name = new ObjectName("java.lang:type=Memory");
            String operationName = "gc";
            Object[] parameters = null;
            String[] signature = null;
 
            connection.invoke(name, operationName, parameters, signature);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnectors();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    public static void printFullGarbageCollectInfo(String urlPath, Boolean authenticate) {
        try {
            MBeanServerConnection connection = getMBeanServerConnection(urlPath, authenticate);
 
            // parallel collector
            ObjectName name = new ObjectName("java.lang:type=GarbageCollector,name=PS MarkSweep");
 
            // g1 collector
            //ObjectName name = new ObjectName("java.lang:type=GarbageCollector,name=G1 Old Generation");
 
            Object result = connection.getAttribute(name, "LastGcInfo");
            CompositeDataSupport lastGCInfoData = (CompositeDataSupport) result;
 
            Long durationOfLastGc = (Long) lastGCInfoData.get("duration");
 
            // parallel collector
            Long usedMemoryBeforeGc = (Long) getMemoryMetric(lastGCInfoData, "memoryUsageBeforeGc", "PS Old Gen", "used");
            Long usedMemoryAfterGc = (Long) getMemoryMetric(lastGCInfoData, "memoryUsageAfterGc", "PS Old Gen", "used");
            Long maxMemory = (Long) getMemoryMetric(lastGCInfoData, "memoryUsageAfterGc", "PS Old Gen", "max");
 
            // g1 collector
            //Long usedMemoryBeforeGc = (Long) getMemoryMetric(lastGCInfoData, "memoryUsageBeforeGc", "G1 Old Gen", "used");
            //Long usedMemoryAfterGc = (Long) getMemoryMetric(lastGCInfoData, "memoryUsageAfterGc", "G1 Old Gen", "used");
            //Long maxMemory = (Long) getMemoryMetric(lastGCInfoData, "memoryUsageAfterGc", "G1 Old Gen", "max");
 
            System.out.println("GC Duration: " + durationOfLastGc + " msec, bytes used before GC: " + usedMemoryBeforeGc
                    + ", bytes used after GC: " + usedMemoryAfterGc);
            System.out.println("Collected " + (usedMemoryBeforeGc - usedMemoryAfterGc) + " bytes");
            System.out.println((1.0 * usedMemoryAfterGc / maxMemory) * 100.0 + "% of the heap is in use");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnectors();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    private static Object getMemoryMetric(CompositeDataSupport data, String diagnostic, String heapGeneration, String type) {
        Object memoryMetric = null;
        TabularDataSupport tabularData = (TabularDataSupport) data.get(diagnostic);
        Iterator tabularDataIterator = tabularData.entrySet().iterator();
        while (tabularDataIterator.hasNext()) {
            Map.Entry tabularDataEntry = (Map.Entry) tabularDataIterator.next();
            if (((List) tabularDataEntry.getKey()).get(0).equals(heapGeneration)) {
                CompositeDataSupport compositeData = (CompositeDataSupport) tabularDataEntry.getValue();
                Iterator compositeDataIterator = compositeData.values().iterator();
                while (compositeDataIterator.hasNext()) {
                    Object object = compositeDataIterator.next();
                    if (!(object instanceof java.lang.String)) {
                        CompositeDataSupport memoryUsage = (CompositeDataSupport) object;
                        memoryMetric = memoryUsage.get(type);
                    }
                }
            }
        }
        return memoryMetric; 
    }
 
    private static MBeanServerConnection getMBeanServerConnection(String urlPath, Boolean authenticate) throws IOException {
        System.out.println("RUNTIME MBEAN SERVER CONNECTION");
 
        JMXServiceURL serviceURL = new JMXServiceURL(urlPath);
 
        Hashtable hashtable = new Hashtable();
        if (authenticate) {
            System.out.println("AUTHENTICATION NEEDED: SETTING PRINCIPAL AND CREDENTIALS");
            hashtable.put(Context.SECURITY_PRINCIPAL, USERNAME);
            hashtable.put(Context.SECURITY_CREDENTIALS, PASSWORD);
        }
 
        if (urlPath.contains("weblogic")) {
            System.out.println("WEBLOGIC CONNECTION: SETTING EXTRA PARAMETERS");
            hashtable.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
        }
 
        connector = JMXConnectorFactory.connect(serviceURL, hashtable);
 
        return connector.getMBeanServerConnection();
    }
 
    private static void closeConnectors() throws IOException {
        if (connector != null) {
            System.out.println("CLOSE RUNTIME MBEAN SERVER CONNECTION");
            connector.close();
        }
    }
}