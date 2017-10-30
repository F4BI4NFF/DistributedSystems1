package DS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by breathtaKing on 07-06-2016.
 */
public class CONNECTION implements Runnable {
    private DatagramSocket ServerSocket;
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private byte[] sendData;
    private byte[] receiveData;

    public String getMessage()
    {
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try
        {
            this.ServerSocket.receive(this.receivePacket);
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
            System.err.println("No llego el datagrama");
            return "";
        }
        return new String(this.receivePacket.getData()).trim();
    }
    public void sendMessage(String line, InetAddress ServerIP, int port) {
        System.out.println(line);
        sendData = line.getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, ServerIP, port);
        try {
            ServerSocket.send(sendPacket);
        } catch (IOException e) {
            System.err.println("No se envio el datagrama");
        }
    }
    //private DataInputStream is = null;
    //private DataOutputStream os = null;

    public JSONObject getDA() {
        return DA;
    }

    public void setDA(JSONObject DA) {
        this.DA = DA;
    }

    private JSONObject DA = new JSONObject();

    public CONNECTION(DatagramPacket packet, JSONObject distritos){
        try{
            this.ServerSocket = new DatagramSocket();

        }catch (SocketException e){
            System.out.println(e.getMessage());
        }
        //System.out.println(new String(packet.getData()));
        //System.out.println(socket.getLocalPort());
        this.receivePacket = packet;
        this.DA = distritos;
    }

    @Override
    public void run(){
        try {
            String comando = new String(this.receivePacket.getData()).trim();
            System.out.println(comando);

            JSONObject fromclient = processJSON(comando);
            Console console = System.console();
            System.out.println(fromclient.get("tipo"));
            if (fromclient.get("tipo").equals("Cliente")){
                System.out.println("aqui");
                for(String cmd =console.readLine(
                        "[Servidor Central]Dar autorizacion a "+this.receivePacket.getAddress() +" por Distrito "+fromclient.get("nombre")+"\n1.-SI\n2.-NO\nx.- Salir\n[Servidor Central]>");
                    !cmd.equals("x");
                    cmd=console.readLine("[Servidor Central]>")){

                    if(cmd.equals("1")){
                        JSONObject mensaje = getDA();
                        System.out.println(mensaje.get(fromclient.get("nombre")));
                        JSONArray conectiondata = (JSONArray) mensaje.get(fromclient.get("nombre"));
                        JSONObject respuesta = new JSONObject();
                        respuesta.put("response","aceptado");
                        respuesta.put("datos",conectiondata);
                        //Enviar info por UDP
                        System.out.println(respuesta.toJSONString());

                        this.sendMessage(
                                respuesta.toJSONString(),
                                this.receivePacket.getAddress(),
                                this.receivePacket.getPort());

                        //os.writeUTF(respuesta.toJSONString());
                        break;
                    }
                    else if(cmd.equals(("2"))){
                        JSONObject respuesta = new JSONObject();
                        respuesta.put("response","rechazado");
                        this.sendMessage(
                                respuesta.toJSONString(),
                                this.receivePacket.getAddress(),
                                this.receivePacket.getPort());
                        break;
                    }
                    else{
                        System.out.println("[Servidor Central]Comando invalido\n");
                    }
                    //comando = this.getMessage();

                }
                System.out.println("[Servidor Central] En espera de peticiones...");
                //os.close();
                //is.close();
            }else if (fromclient.get("tipo").equals("Distrito")){
                if (fromclient.get("comando").equals("1")){
                    // Logica recepcion de lista (Capturados/Asesinados Globales)
                    this.ListarCapturados();
                }else if (fromclient.get("comando").equals("2")){
                    // Logica envio de peticion de lista (Capturados/Asesinados Globales)
                    this.ListarAsesinados();
                }else if (fromclient.get("comando").equals("3")){
                    // El distrito pide un id unico para asignar
                    AtomicInteger ID= Server.getInstance().getUnico();
                    this.sendMessage(
                            String.valueOf(ID.getAndIncrement()),
                            this.receivePacket.getAddress(),
                            this.receivePacket.getPort());

                }else {
                    System.out.println("Comando invalido recivido de Distrito");
                }
                // OJO que tambien sirve el paso de comandos por json
            }

        }catch (Exception e) {
                System.out.println(e);
                assert (false);
        } finally {
            this.ServerSocket.disconnect();
        }

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
        return obj;
    }

    public void ListarCapturados() throws Exception{
        //os.writeUTF("Hola soy Listar Capturados");
        System.out.println("Hola soy L Capturados");
    }
    public void ListarAsesinados() throws Exception{
        //os.writeUTF("Hola soy Listar Asesinados");
        System.out.println("Hola soy L Asesinados");
    }
}
