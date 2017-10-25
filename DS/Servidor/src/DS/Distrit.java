package DS;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Distrit {
    private String DistritName;
    private String MultiCastIp;
    private int MultiCastPort;
    private String RequestIP;
    private int ResquestPort;

    public String getDistritName() {
        return DistritName;
    }

    public void setDistritName(String distritName) {
        DistritName = distritName;
    }

    public String getMultiCastIp() {
        return MultiCastIp;
    }

    public void setMultiCastIp(String multiCastIp) {
        MultiCastIp = multiCastIp;
    }

    public int getMultiCastPort() {
        return MultiCastPort;
    }

    public void setMultiCastPort(int multiCastPort) {
        MultiCastPort = multiCastPort;
    }

    public String getRequestIP() {
        return RequestIP;
    }

    public void setRequestIP(String requestIP) {
        RequestIP = requestIP;
    }

    public int getResquestPort() {
        return ResquestPort;
    }

    public void setResquestPort(int resquestPort) {
        ResquestPort = resquestPort;
    }

    public static void main(String[] args){
        Server servidor = new Server();

        try{
            ServerSocket listenSocket = new ServerSocket(servidor.getListenPort());
            System.out.println("Server Central en espera...");
            while (true) {
                Socket cs = listenSocket.accept();
                System.out.println("Nueva conexion entrante: ");
                System.out.println("Puerto: " + servidor.getMultiCastPort());

                Thread t = new Thread(new CONNECTION(cs));
                t.start();
            }
        } catch (IOException e){
            System.out.println(e.getMessage());

            //Logica servidor de distrito

        }
    }
}
