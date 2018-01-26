package com.baloise.firewall;
import java.net.InetAddress;
import java.net.UnknownHostException;
 
/*
 * Main.java
 *
 * @author www.javadb.com
 */
public class Main {
    
    /*
     * This method performs a NS Lookup
     */
    public void performNSLookup() {
        
        try {
            
            InetAddress inetHost = InetAddress.getByName("test-solr-ch");
            String hostName = inetHost.getHostName();
            System.out.println("The host name was: " + hostName);
            System.out.println("The hosts IP address is: " + inetHost.getHostAddress());
            
        } catch(UnknownHostException ex) {
            
            System.out.println("Unrecognized host");
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().performNSLookup();
    }
}