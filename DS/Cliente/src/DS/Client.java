package DS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class Client {

    private MulticastSocket mSocket;
    private DatagramSocket ClientSocket;
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private byte[] sendData;
    private byte[] receiveData;

    private String DistritName;
    private String CentralServerIP;
    private int CentralServerPort;
    private String MultiCastIP;

    private int MultiCastPort;
    private String PeticionesIP;
    private int PeticionesPort;
    private Boolean Conectado = false; // Conectado a un distrito

    public String getMessage()
    {
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try
        {
            ClientSocket.receive(receivePacket);
        }
        catch(IOException e)
        {
            System.err.println("No llego el datagrama");
            return "";
        }
        return new String(receivePacket.getData()).trim();
    }

    public void sendMessage(String line,InetAddress ServerIP, int port) {
        sendData = line.getBytes();
        System.out.println(line);
        sendPacket = new DatagramPacket(sendData, sendData.length, ServerIP, port);
        try {
            ClientSocket.send(sendPacket);
        } catch (IOException e) {
            System.err.println("No se envio el datagrama");
        }
    }

    public int getMultiCastPort() {
        return MultiCastPort;
    }

    public void setMultiCastPort(int multiCastPort) {
        MultiCastPort = multiCastPort;
    }

    public String getPeticionesIP() {
        return PeticionesIP;
    }

    public void setPeticionesIP(String peticionesIP) {
        PeticionesIP = peticionesIP;
    }

    public int getPeticionesPort() {
        return PeticionesPort;
    }

    public void setPeticionesPort(int peticionesPort) {
        PeticionesPort = peticionesPort;
    }

    public String getDistritName() {
        return DistritName;
    }

    public void setDistritName(String distritName) {
        this.DistritName = distritName;
    }

    public String getCentralServerIP() {
        return CentralServerIP;
    }

    public void setCentralServerIP(String centralServerIP) {
        this.CentralServerIP = centralServerIP;
    }

    public int getCentralServerPort() {
        return CentralServerPort;
    }

    public void setCentralServerPort(int centralServerPort) {
        this.CentralServerPort = centralServerPort;
    }

    public String getMultiCastIP() {
        return MultiCastIP;
    }

    public void setMultiCastIP(String multiCastIP) {
        MultiCastIP = multiCastIP;
    }


    public static void main(String[] args) {
        Client cliente = new Client();
        Console console = System.console();
        System.out.println("Iniciando cliente\n");

        cliente.setCentralServerIP(console.readLine("[Cliente] Ingresar IP Servidor Central : "));
        cliente.setCentralServerPort(Integer.parseInt(console.readLine("[Cliente] Ingresar Puerto Servidor Central : ")));

        //Conexion a servidor
        try {
            cliente.ClientSocket = new DatagramSocket();
            int comando = 10;
            while (comando != 0) {
                String param = null;

                //******//
                //Menu  //
                //******//
                for (String cmd = console.readLine("[Cliente] Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina, x para salir\n");
                     !cmd.equals("x");
                     cmd = console.readLine("[Cliente] Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina, x para salir\n")) {

                    cliente.setDistritName(cmd);
                    cliente.sendMessage(
                            initJSONfirst(cmd),
                            InetAddress.getByName(cliente.getCentralServerIP()),
                            cliente.getCentralServerPort());
                    String fromserver = cliente.getMessage();
                    System.out.println("recibido del server: "+fromserver);
                    JSONObject fromserverobj = processJSON(fromserver);
                    if (fromserverobj.get("response").equals("aceptado")) {
                        //Conexion aceptada
                        JSONArray msg = (JSONArray) fromserverobj.get("datos");
                        try{
                            Iterator<String> iterator = msg.iterator();
                            List<String> list = new ArrayList<>();
                            while (iterator.hasNext()) {
                                list.add(iterator.next());
                            }
                            //Conexion a distrito
                            //
                            cliente.setMultiCastIP(list.get(0));
                            cliente.setMultiCastPort(Integer.parseInt(String.valueOf(list.get(1))));
                            cliente.setPeticionesIP(list.get(2));
                            cliente.setPeticionesPort(Integer.parseInt(String.valueOf(list.get(3))));

                            cliente.ClientSocket = new DatagramSocket();
                            cliente.Conectado = true;
                            cliente.mSocket = new MulticastSocket(cliente.getMultiCastPort());
                            cliente.mSocket.joinGroup(InetAddress.getByName(cliente.getMultiCastIP()));

                            Thread mt = new Thread(new Listener(InetAddress.getByName(cliente.getMultiCastIP()),cliente.getMultiCastPort()));
                            mt.start();


                            //mostrar consola
                            System.out.println("[Cliente] Consola");
                            String initString;
                            for (cmd = console.readLine("[Cliente] (1) Listar Titanes\n[Cliente] (2) Cambiar Distrito\n[Cliente] (3) Capturar Titan\n[Cliente] (4) Asesinar Titan\n[Cliente] (5) Lista de Titanes Capturados\n[Cliente] (6) Lista de Titanes Asesinados\n[Cliente] (x) Desconectar\n");
                                 !cmd.equals("x");
                                 cmd = console.readLine("****************************************************\n[Cliente] (1) Listar Titanes\n[Cliente] (2) Cambiar Distrito\n[Cliente] (3) Capturar Titan\n[Cliente] (4) Asesinar Titan\n[Cliente] (5) Lista de Titanes Capturados\n[Cliente] (6) Lista de Titanes Asesinados\n[Cliente] (x) Desconectar\n")) {
                                if (cmd.equals("1")) {
                                    //Listar Titanes

                                    //cliente.sendData = initJSON(String.valueOf(cmd), "comodin").getBytes();
                                    cliente.sendMessage(
                                            initJSON(String.valueOf(cmd),"comodin"),
                                            InetAddress.getByName(cliente.getPeticionesIP()),
                                            cliente.getPeticionesPort());
                                    //cliente.getMessage();
                                    fromserver = cliente.getMessage();
                                    System.out.println("UDP desde el server : " + fromserver);
                                    System.out.println("----------------------------------");

                                } else if (cmd.equals("2")) {
                                    if (!cliente.Conectado) {
                                        System.out.println("Usted no esta conectado a ningun distrito");
                                    } else {
                                        //Cambiar de distrito
                                        param = console.readLine("Ingrese distrito a investigar :");
                                        //initString = initJSON(String.valueOf(cmd), param);
                                        cliente.sendMessage(
                                                initJSONfirst(param),
                                                InetAddress.getByName(cliente.getCentralServerIP()),
                                                cliente.getCentralServerPort());
                                        fromserver = cliente.getMessage();
                                        System.out.println("mensaje buscador : "+fromserver);
                                        fromserverobj = processJSON(fromserver);
                                        if (fromserverobj.get("response").equals("aceptado")) {
                                            //Conexion aceptada
                                            cliente.setDistritName(param);
                                            msg = (JSONArray) fromserverobj.get("datos");
                                            iterator = msg.iterator();
                                            list = new ArrayList<>();
                                            while (iterator.hasNext()) {
                                                list.add(iterator.next());
                                            }

                                            //Conexion a distrito se logra mediante el reemplazo de los datos
                                            cliente.setDistritName(param);
                                            cliente.setMultiCastIP(list.get(0));
                                            cliente.setMultiCastPort(Integer.parseInt(String.valueOf(list.get(1))));
                                            cliente.setPeticionesIP(list.get(2));
                                            cliente.setPeticionesPort(Integer.parseInt(String.valueOf(list.get(3))));
                                            mt.interrupt();
                                            mt = new Thread(new Listener(InetAddress.getByName(cliente.getMultiCastIP()),cliente.getMultiCastPort()));
                                            mt.start();

                                            //System.out.println("");
                                            System.out.println("Conexión autorizada para el Distrito de " + cliente.getDistritName());
                                        }
                                        else{
                                            System.out.println("Conexión no autorizada para el Distrito de " + cliente.getDistritName());
                                            System.out.println("Usted sigue en el distrito de "+cliente.getDistritName());
                                        }
                                    }


                                } else if (cmd.equals("3")||cmd.equals("4")) {
                                    // Capturar titan,Asesinar titan
                                    //initString = initJSON(String.valueOf(cmd), param);
                                    //os.writeUTF(initString);
                                    System.out.println("Capturar asesinar CLiente");
                                    param = console.readLine("Ingrese id del titan :");
                                    cliente.sendMessage(initJSON(String.valueOf(cmd), param),
                                            InetAddress.getByName(cliente.getPeticionesIP()),
                                            cliente.getPeticionesPort());

                                    //Se espera una respuesta?

                                    //fromserver = cliente.getMessage();
                                    //fromserverobj = processJSON(fromserver);
                                } else if (cmd.equals("5")||cmd.equals("6")) {
                                    // Listas capturados/asesinados
                                    cliente.sendMessage(initJSON(String.valueOf(cmd), param),
                                            InetAddress.getByName(cliente.getPeticionesIP()),
                                            cliente.getPeticionesPort());

                                    //Se espera una respuesta?

                                    fromserver = cliente.getMessage();
                                    System.out.println("Mensaje L : "+fromserver);
                                    fromserverobj = processJSON(fromserver);

                                } else {
                                    System.out.println("Comando no válido");
                                }

                            }
                        }catch (NullPointerException e){
                            System.out.println(msg);
                        }



                    } else {
                        System.out.println("Conexión no autorizada para el Distrito de " + cliente.getDistritName());
                    }

                }
                comando = 0;

            }

        } catch (UnknownHostException e1) {
            System.out.println("Socket: " + e1.getMessage());
        } catch (IOException e2) {
            System.out.println("Linea: " + e2.getMessage());
        }
    }
    private static String initJSONfirst(String mode){
        JSONObject obj = new JSONObject();
        obj.put("nombre",mode);
        obj.put("tipo","Cliente");
        return obj.toJSONString();
    }


    private static String initJSON(String mode,String name){
        JSONObject obj = new JSONObject();
        obj.put("comando",mode);
        if(obj.get("comando").equals("2")){
            obj.put("distrit",name);
        }
        else if (obj.get("comando").equals("3")){
            obj.put("id",name);
        }
        else if (obj.get("comando").equals("4")){
            obj.put("id",name);
        }
        return obj.toJSONString();
    }
    public static JSONObject processJSON(String mensaje){
        JSONObject obj = null;
        JSONParser parser = new JSONParser();
        try {
            obj = (JSONObject) parser.parse(mensaje);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        //JSONArray msg = (JSONArray) obj.get("response");
        //Iterator<String> iterator = msg.iterator();
        //while (iterator.hasNext()) {
        //    System.out.println(iterator.next());
        //}
        return obj;
    }
}
