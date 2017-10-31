package DS;

import javax.xml.crypto.Data;
import java.io.Console;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Notify implements Runnable{
    Distrit distrito;
    byte[] sendData;
    DatagramPacket sendPacket;
    MulticastSocket multicastSocket;


    public Notify(Distrit Distrito,MulticastSocket ms){
        System.out.println("datos notify: "+Distrito.getCentralPort()+" ad" + Distrito.getCentralIP());
        this.distrito = Distrito;
        this.multicastSocket = ms;
    }
    public void sendMultiCast(String line, InetAddress ServerIP, int port) {
        System.out.println(line);
        sendData = line.getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, ServerIP, port);
        try {
            multicastSocket.send(sendPacket);
        } catch (IOException e) {
            System.err.println("No se envio el datagrama");
        }
    }
    @Override
    public void run() {
        Console console = System.console();
        String menu = "[Distrito "+distrito.getDistritName()+"] Ingrese accion a realizar\n[Distrito "+distrito.getDistritName()+"] 1.- Publicar Titan\n[Distrito "+distrito.getDistritName()+"] x.- Para Salir\n";
        for (String cmd = console.readLine(menu);!cmd.equals("x");cmd = console.readLine("********************************\n" + menu))
        {
            if (cmd.equals("1")){
                //Se agrega un Titan
                String NombreTitan = console.readLine("[Distrito"+distrito.getDistritName()+"]Introducir nombre \n>");
                String TipoTitan = console.readLine("[Distrito"+distrito.getDistritName()+"]Introducir tipo :\n 1.- Normal\n 2.- Excentrico\n 3.- Cambiante\n > ");
                Titan titan = new Titan(NombreTitan,Integer.parseInt(TipoTitan),distrito.getID());
                System.out.println("[Distrito"+distrito.getDistritName()+"] Se ha publicado el titán"+ NombreTitan );
                System.out.println("*****************");
                System.out.println("ID: "+titan.getID_titan());
                System.out.println("Nombre: "+titan.getNombre_titan());
                System.out.println("Tipo: " + titan.getTipo_titan());
                System.out.println("*****************");
                distrito.lista_titanes.add(titan);
                //Multicast...
                String tipo = "";
                if (titan.getTipo_titan() == 1) {
                    tipo = "Normal";
                }else if(titan.getTipo_titan() == 2){
                    tipo = "Excentrico";
                }else if(titan.getTipo_titan() == 3){
                    tipo = "Cambiante";
                }
                String mensaje = "[Cliente] Aparece nuevo titan, "+titan.getNombre_titan()+", tipo "+tipo+", id "+titan.getID_titan();
                try{
                    sendMultiCast(mensaje,InetAddress.getByName(distrito.getMultiCastIp()),distrito.getMultiCastPort());

                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
            else{
                System.out.println("[Distrito"+distrito.getDistritName()+"] Comando invalido");
            }
        }
    }
}
