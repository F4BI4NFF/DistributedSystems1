package DS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Client {
    private String DistritName;
    private String CentralServerIP;
    private int CentralServerPort;
    private String MultiCastListen;
    private Boolean Conectado = false; // Conectado a un distrito

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

    public String getMultiCastListen() {
        return MultiCastListen;
    }

    public void setMultiCastListen(String multiCastListen) {
        MultiCastListen = multiCastListen;
    }


    public static void main(String[] args) {
        Client cliente = new Client();
        Console console = System.console();
        System.out.println("Iniciando cliente\n");

        cliente.setCentralServerIP(console.readLine("[Cliente] Ingresar IP Servidor Central : "));
        cliente.setCentralServerPort(Integer.parseInt(console.readLine("[Cliente] Ingresar Puerto Servidor Central : ")));

        //Conexion a servidor
        try {
            Socket s = new Socket(cliente.getCentralServerIP(), cliente.getCentralServerPort());
            int comando = 10;
            while (comando != 0) {
                String param = null;
                DataInputStream is = null;
                DataOutputStream os = null;
                try {
                    is = new DataInputStream(s.getInputStream());
                    os = new DataOutputStream(s.getOutputStream());

                } catch (IOException e) {
                    System.out.println("Conexion: " + e.getMessage());
                }
                System.out.println("Conexion aceptada Servidor Central.");

                //******//
                //Menu  //
                //******//
                for (String cmd = console.readLine("[Cliente] Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina, x para salir\n");
                     !cmd.equals("x");
                     cmd = console.readLine("[Cliente] Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina, x para salir\n")) {
                    cliente.setDistritName(cmd);
                    os.writeUTF(initJSONfirst(cmd));
                    String fromserver = is.readUTF();
                    JSONObject fromserverobj = processJSON(fromserver);
                    if (fromserverobj.get("response").equals("aceptado")) {
                        //Conexion aceptada
                        JSONArray msg = (JSONArray) fromserverobj.get("datos");
                        Iterator<String> iterator = msg.iterator();
                        List<String> list = new ArrayList<>();
                        while (iterator.hasNext()) {
                            list.add(iterator.next());
                        }

                        //Conexion a distrito

                        int puerto_peticion = Integer.parseInt(String.valueOf(list.get(3)));
                        Socket distritsocket = new Socket(list.get(2), puerto_peticion);

                        DataInputStream Dis = null;
                        DataOutputStream Dos = null;
                        cliente.Conectado = true;
                        try {
                            Dis = new DataInputStream(distritsocket.getInputStream());
                            Dos = new DataOutputStream(distritsocket.getOutputStream());

                        } catch (IOException e) {
                            System.out.println("Conexion: " + e.getMessage());
                        }

                        //mostrar consola
                        System.out.println("[Cliente] Consola");
                        String initString;
                        for (cmd = console.readLine("[Cliente] (1) Listar Titanes\n[Cliente] (2) Cambiar Distrito\n[Cliente] (3) Capturar Titan\n[Cliente] (4) Asesinar Titan\n[Cliente] (5) Lista de Titanes Capturados\n[Cliente] (6) Lista de Titanes Asesinados\n[Cliente] (x) Desconectar\n");
                             !cmd.equals("x");
                             cmd = console.readLine("****************************************************\n[Cliente] (1) Listar Titanes\n[Cliente] (2) Cambiar Distrito\n[Cliente] (3) Capturar Titan\n[Cliente] (4) Asesinar Titan\n[Cliente] (5) Lista de Titanes Capturados\n[Cliente] (6) Lista de Titanes Asesinados\n[Cliente] (x) Desconectar\n")) {
                            if (cmd.equals("1")) {
                                //Listar Titanes
                                initString = initJSON(String.valueOf(cmd), "caca");
                                Dos.writeUTF(initString);
                                fromserver = Dis.readUTF();
                                System.out.println(fromserver);
                                System.out.println("----------------------------------");

                            } else if (cmd.equals("2")) {
                                if (!cliente.Conectado) {
                                    System.out.println("Usted no esta conectado a ningun distrito");
                                } else {
                                    //Cambiar de distrito
                                    param = console.readLine("Ingrese distrito a investigar :");
                                    //initString = initJSON(String.valueOf(cmd), param);
                                    try {
                                        os.writeUTF(param);
                                    }catch (IOException e){
                                        e.getMessage();
                                    }
                                    fromserver = is.readUTF();
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

                                        //Conexion a distrito

                                        puerto_peticion = Integer.parseInt(String.valueOf(list.get(3)));
                                        distritsocket.close();
                                        distritsocket = new Socket(list.get(2), puerto_peticion);

                                        Dis = null;
                                        Dos = null;
                                        cliente.Conectado = true;
                                        try {
                                            Dis = new DataInputStream(s.getInputStream());
                                            Dos = new DataOutputStream(s.getOutputStream());

                                        } catch (IOException e) {
                                            System.out.println("Conexion: " + e.getMessage());
                                        }


                                        //System.out.println("");
                                    }
                                    else{
                                        System.out.println("Conexión no autorizada para el Distrito de " + cliente.getDistritName());
                                        System.out.println("Usted sigue en el distrito de "+cliente.getDistritName());
                                    }
                                }


                            } else if (cmd.equals("3")) {
                                // Capturar titan
                                initString = initJSON(String.valueOf(cmd), param);
                                os.writeUTF(initString);

                            } else if (cmd.equals("4")) {
                                // Asesinar titan
                                initString = initJSON(String.valueOf(cmd), param);
                                os.writeUTF(initString);

                            } else if (cmd.equals("5")) {
                                // Lista titanes caputados
                                initString = initJSON(String.valueOf(cmd), param);
                                os.writeUTF(initString);
                            } else if (cmd.equals("6")) {
                                // Lista titanes asesinados
                                initString = initJSON(String.valueOf(cmd), param);
                                os.writeUTF(initString);

                            } else {
                                System.out.println("Comando no válido");
                            }

                        }


                    } else {
                        System.out.println("Conexión no autorizada para el Distrito de " + cliente.getDistritName());
                    }

                }
                //String fromserver = is.readUTF();
                //System.out.println("Server: " + fromserver);
                is.close();
                os.close();
                comando = 0;

            }
            s.close();

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
