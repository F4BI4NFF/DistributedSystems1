package DS;

import com.sun.xml.internal.messaging.saaj.soap.SOAPIOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.rmi.server.InactiveGroupException;

import javax.xml.crypto.Data;
import java.io.Console;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Distrit {
    //**********Server Information***********//
    private String DistritName;
    private String MultiCastIp;
    private int MultiCastPort;
    private String RequestIP;
    private int RequestPort;
    private String CentralIP;

    public String getCentralIP() {
        return CentralIP;
    }

    public void setCentralIP(String centralIP) {
        CentralIP = centralIP;
    }

    public int getCentralPort() {
        return CentralPort;
    }

    public void setCentralPort(int centralPort) {
        CentralPort = centralPort;
    }

    private int CentralPort;

    private DatagramSocket serverSocket;
    private DatagramPacket Packet;
    private byte[] Data;

    private int packetSize = 1024;

    public int getPacketSize() {
        return packetSize;
    }

    //**********************************************//
    public int getID() {
        int ID = -1;
        String fromserver;
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        JSONObject peticion = new JSONObject();
        peticion.put("tipo", "Distrito");
        peticion.put("comando", "3");

        System.out.println(peticion.toJSONString());

        DatagramPacket receivePa;
        System.out.println(this.getCentralIP()+" puerto : "+this.getCentralPort());
        try {
            sendData = peticion.toJSONString().getBytes();
            DatagramPacket sendPa = new DatagramPacket(sendData,sendData.length,InetAddress.getByName(this.getCentralIP()),this.getCentralPort());
            DatagramSocket pSocket = new DatagramSocket();
            try {
                pSocket.send(sendPa);
                try {
                    receiveData = new byte[1024];
                    receivePa = new DatagramPacket(receiveData, receiveData.length);
                    pSocket.receive(receivePa);
                    fromserver =  new String(receivePa.getData()).trim();
                    JSONParser parser = new JSONParser();
                    try {
                        peticion = (JSONObject) parser.parse(fromserver);
                        ID = Integer.parseInt(peticion.get("ID").toString());
                        return ID;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }catch (SocketException e) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.getMessage();
        }
        return ID;
    }


    public DatagramPacket getPacket() {
        Data = new byte[1024];
        Packet = new DatagramPacket(Data, Data.length);
        try {
            serverSocket.receive(Packet);
        } catch (IOException e) {
            System.err.println("Error: error while recieving packet");
            return null;
        }
        return Packet;
    }

    public void sendPacket(DatagramPacket packet) {
        try {
            serverSocket.send(packet);
        } catch (IOException e) {
            System.err.println("Error: error while sending packet");
        }
    }

    //***********Informacion relevante**************//
    List<Titan> lista_titanes = Collections.synchronizedList(new ArrayList<Titan>());
    List<Titan> lista_titanes_capturados = Collections.synchronizedList(new ArrayList<Titan>());
    List<Titan> lista_titanes_asesinados = Collections.synchronizedList(new ArrayList<Titan>());
    //*************************//

    public List<Titan> getLista_titanes() {
        return lista_titanes;
    }

    public void setLista_titanes(List<Titan> lista_titanes) {
        this.lista_titanes = lista_titanes;
    }

    public int getRequestPort() {
        return RequestPort;
    }

    public void setRequestPort(int requestPort) {
        RequestPort = requestPort;
    }

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

    //********************************************//

    public List<Titan> getLista_titanes_capturados() {
        return lista_titanes_capturados;
    }

    public void setLista_titanes_capturados(List<Titan> lista_titanes_capturados) {
        this.lista_titanes_capturados = lista_titanes_capturados;
    }

    public List<Titan> getLista_titanes_asesinados() {
        return lista_titanes_asesinados;
    }

    public void setLista_titanes_asesinados(List<Titan> lista_titanes_asesinados) {
        this.lista_titanes_asesinados = lista_titanes_asesinados;
    }

    //********************************************//

    //********************************************//

    private static Distrit distrito= null;

    private Distrit(){}
    private synchronized static void createInstance(){
        if (distrito == null){
            distrito = new Distrit();
        }
    }
    public static Distrit getInstance(){
        if(distrito == null){
            createInstance();
        }
        return distrito;

    }


    //********************************************//

    public static void main(String[] args) {
        Console console = System.console();
        Distrit distrito = new Distrit();
        distrito.setDistritName(console.readLine("[Distrito] Nombre Servidor : "));
        distrito.setMultiCastIp(console.readLine("[Distrito " + distrito.getDistritName() + "] IP Multicast : "));
        distrito.setMultiCastPort(Integer.parseInt(console.readLine("[Distrito " + distrito.getDistritName() + "] Puerto Multicast : ")));
        distrito.setRequestIP(console.readLine("[Distrito " + distrito.getDistritName() + "] IP Peticiones : "));
        distrito.setRequestPort(Integer.parseInt(console.readLine("[Distrito " + distrito.getDistritName() + "] Puerto Peticiones : ")));
        distrito.setCentralIP(console.readLine("[Distrito " + distrito.getDistritName() + "] IP Central : "));
        distrito.setCentralPort(Integer.parseInt(console.readLine("[Distrito " + distrito.getDistritName() + "] CentralPort : ")));

        try {
            DatagramSocket serverSocket = new DatagramSocket(distrito.getRequestPort());
            MulticastSocket castSocket = new MulticastSocket(distrito.getMultiCastPort());
            //castSocket.joinGroup(InetAddress.getByName(distrito.getMultiCastIp()));

            //System.out.println("[Distrito " + distrito.getDistritName() + "] En espera de peticiones...");

            //Thread-< 1 solo porque se maneja en 1 sola consola.
            Thread n = new Thread(new Notify(distrito,castSocket));
            n.start();

            while (true) {
                System.out.println("[Distrito " + distrito.getDistritName() + "] En espera de peticiones...");

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
                try {
                    serverSocket.receive(receivePacket);
                } catch (IOException e) {
                    System.err.println("Error: no se pudo recibir el packete UDP");
                    System.exit(-1);
                }
                //Socket cs = listenSocket.accept();
                System.out.println("packet:"+ new String(receivePacket.getData()).trim());
                System.out.println("Nueva conexion entrante: "+ receivePacket.getAddress());
                System.out.println("Puerto: " + receivePacket.getPort() );
                Thread t = new Thread(new CONNECTION(serverSocket, castSocket, distrito, receivePacket));
                //System.out.println("Opciones... \n Notify un titan:");
                t.start();
            }
        }catch (IOException e) {
            //no se pudo abrir socket
            System.out.println(e.getMessage());
        }
    }
}
