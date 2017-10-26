package DS;

import org.json.simple.JSONObject;

import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

    public  void setDistritName(String distritName) {
        this.DistritName = distritName;
    }

    public String getCentralServerIP() {
        return CentralServerIP;
    }

    public  void setCentralServerIP(String centralServerIP) {
        this.CentralServerIP = centralServerIP;
    }

    public int getCentralServerPort() {
        return CentralServerPort;
    }

    public  void setCentralServerPort(int centralServerPort) {
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

        cliente.setCentralServerIP(console.readLine("[Cliente] Ingresar IP Servidor Central"));
        cliente.setCentralServerPort(Integer.parseInt(console.readLine("[Cliente] Ingresar Puerto Servidor Central")));

        //Conexion a servidor
        try {
            Socket s = new Socket( cliente.getCentralServerIP(),cliente.getCentralServerPort());
            int comando = 10;
            while(comando != 0){
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
                for (String cmd = console.readLine("[Cliente] Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina, x para salir");
                     !cmd.equals("x");
                     cmd = console.readLine("[Cliente] Introducir Nombre de Distrito a Investigar, Ej: Trost, Shiganshina, x para salir")) {
                    if (cmd.equals("1")) {
                        //Conexion aceptada
                        System.out.println("[Cliente] Consola");
                        for (cmd = console.readLine("[Cliente] (1) Listar Titanes\n[Cliente] (2) Cambiar Distrito\n[Cliente] (3) Capturar Titan\n[Cliente] (4) Asesinar Titan\n[Cliente] (5) Lista de Titanes Capturados\n[Cliente] (6) Lista de Titanes Asesinados\n[Cliente] (x) Desconectar\n");
                             !cmd.equals("x");
                             cmd = console.readLine("****************************************************\n[Cliente] (1) Listar Titanes\n[Cliente] (2) Cambiar Distrito\n[Cliente] (3) Capturar Titan\n[Cliente] (4) Asesinar Titan\n[Cliente] (5) Lista de Titanes Capturados\n[Cliente] (6) Lista de Titanes Asesinados\n[Cliente] (x) Desconectar\n")) {
                            if (cmd.equals("1")) {


                            }
                            else if (cmd.equals("2")){
                                if(cliente.Conectado){
                                    System.out.println("Ya esta conectado a un distrito");
                                }
                                else{
                                    System.out.println("");
                                }

                            }
                            else if (cmd.equals("3")){

                            }
                            else if (cmd.equals("4")){

                            }
                            else if (cmd.equals("5")){

                            }
                            else if (cmd.equals("6")){

                            }
                            else{
                                System.out.println("Comando no válido");
                            }

                        }


                    }
                    else{
                        System.out.println("Conexión no autorizada para el Distrito de "+cliente.getDistritName());
                    }

                }
                comando = scanner.nextInt();
                if(comando == 2 || comando == 3 || comando == 4){
                    param = scanner.nextLine();
                }
                String initString = initJSON(String.valueOf(comando), param);


                os.writeUTF(initString);
                String fromserver = is.readUTF();
                System.out.println ("Server: "+ fromserver);
                is.close();
                os.close();
            }

        } catch (UnknownHostException e1) {
            System.out.println("Socket: " + e1.getMessage());
        } catch (IOException e2) {
            System.out.println("Linea: " + e2.getMessage());
        }
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
}
