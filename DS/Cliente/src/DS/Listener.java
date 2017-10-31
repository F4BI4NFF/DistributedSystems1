package DS;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;


public class Listener extends Thread {
    private volatile boolean isRunning = true;
    private InetAddress MultiIp;
    private int Multiport;

    private MulticastSocket msocket;

    private DatagramPacket receivePacket;
    private byte[] receiveData;

    public Listener(InetAddress mi,int mp){
        this.MultiIp = mi;
        this.Multiport = mp;
    }

    public String getMulticast()
    {
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try
        {
            this.msocket.setTimeToLive(50);
            this.msocket.receive(receivePacket);
        }
        catch(IOException e)
        {
            System.err.println("No llego el datagrama");
            return "";
        }
        return new String(receivePacket.getData()).trim();
    }

    @Override
    public void run() {
        try{
            while (!this.isInterrupted()){
                msocket = new MulticastSocket(this.Multiport);
                msocket.joinGroup(this.MultiIp);

                String response = getMulticast();
                System.out.println(response);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                msocket.leaveGroup(this.MultiIp);
                msocket.close();
            }catch (IOException e){

            }
        }

    }
}
