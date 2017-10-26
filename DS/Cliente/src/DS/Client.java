package DS;

import org.json.simple.JSONObject;

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
        Scanner scanner = new Scanner(System.in);
        System.out.println("[Cliente] Ingresar IP Servidor Central");
        cliente.setCentralServerIP(scanner.nextLine());
        System.out.println("[Cliente] Ingresar Puerto Servidor Central");
        cliente.setCentralServerPort(scanner.nextInt());
        scanner.nextLine();
        System.out.println("[Cliente] Introducir Nombre de Distrito a Investigar. Ej: Trost, Shiganshina");
        cliente.setDistritName(scanner.nextLine());
        //System.out.println("Distrito"+ cliente.getDistritName());

        System.out.println("Iniciando cliente\n");

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
                System.out.println("Conexion aceptada.");

                //******//
                //Menu  //
                //******//
                System.out.println("[Cliente] Consola");
                System.out.println("[Cliente] (1) Listar Titanes");
                System.out.println("[Cliente] (2) Cambiar Distrito");
                System.out.println("[Cliente] (3) Capturar Titan");
                System.out.println("[Cliente] (4) Asesinar Titan");
                System.out.println("[Cliente] (5) Lista de Titanes Capturados");
                System.out.println("[Cliente] (6) Lista de Titanes Asesinados");
                System.out.println("[Cliente] (0) Desconectar");
                //scanner.nextLine();
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
