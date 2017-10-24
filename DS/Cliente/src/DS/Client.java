package DS;

import org.json.simple.JSONObject;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    @Option(name = "-port", usage = "Port to use")
    private int port = 1234;

    private String hostname = "localhost";

    @Option(name = "-comando", usage = "command to execute (get put delte list)")
    private String command = "";

    //@Option(name="-blocksize",usage="blocksize to use")
    //private int blocksize = 1024;

    @Argument
    private static List<String> arguments = new ArrayList<String>();

    public static void main(String[] args) {
        Client cliente = new Client();
        //CmdLineParser parser = new CmdLineParser(cliente);
        //parser.setUsageWidth(80);
        try {
            //parser.parseArgument(args);
            new CmdLineParser(cliente).parseArgument(args);
            //cliente.setterFilename();
            System.out.println("Iniciando cliente\n");
            //String comando = cliente.getCommand();
            //String filename = cliente.getFilename();
            //SynchronisedFile filename = cliente.getFilename();
            //Conexion a servidor
            try {
                Socket s = new Socket(cliente.getHostname(), cliente.getPort());
                DataInputStream is = null;
                DataOutputStream os = null;
                try {
                    is = new DataInputStream(s.getInputStream());
                    os = new DataOutputStream(s.getOutputStream());

                } catch (IOException e) {
                    System.out.println("Conexion: " + e.getMessage());
                }
                System.out.println("Conexion aceptada.");
                String initString = initJSON(cliente.getCommand(),cliente.getFilename());
                os.writeUTF(initString);
                String fromserver = is.readUTF();
                System.out.println ("Server: "+ fromserver);
                is.close();
                os.close();

            } catch (UnknownHostException e1) {
                System.out.println("Socket: " + e1.getMessage());
            } catch (IOException e2) {
                System.out.println("Linea: " + e2.getMessage());
            }
        } catch (CmdLineException e) {
            //Hubo un error
            System.err.println(e.getMessage());
            System.err.println("java MainCliente [opciones...] argumentos...");
            System.exit(-1);
            //Se muestra cual fue el error
            //parser.printUsage(System.err);
            //System.err.println();
            //Se muestran las opciones disponibles
            //System.err.println("  Ejemplo: java MainClient"+parser.printExample(ALL));
            return;
        }

    }
    private static String initJSON(String mode,String name){
        JSONObject obj = new JSONObject();
        obj.put("comando",mode);
        obj.put("filename",name);
        return obj.toJSONString();
    }
}
