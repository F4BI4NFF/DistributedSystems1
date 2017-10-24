package DS;

import org.kohsuke.args4j.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    //@Option(name="-port",usage="Puerto a usar ( default 6980)")
    private int port = 6980;
    //@Option(name="-grupo",usage="Dirección del grupo multicast")
    private int multiip;
    private int listenport = 5080;
    //@Option(name="-tipo",usage="Tipo de servidor ( default 0/central ,1/distrito)")
    //private int tipo = 0;

    //@Argument
    //private static List<String> arguments = new ArrayList<String>();

    //----------------//
    //Insertar customs//
    //----------------//

    public int getPort() {
        return port;
    }
    public void setListenport(int port){this.listenport = port;}
    public int getListenport(){return listenport;}

    public static void main(String[] args){
        Server servidor = new Server();
        try{
            new CmdLineParser(servidor).parseArgument(args);
            ServerSocket listenSocket = new ServerSocket(servidor.getPort());
            System.out.println("Server Central en espera...");
            while (true) {
                Socket cs = listenSocket.accept();
                System.out.println("Nueva conexion entrante: ");
                System.out.println("Puerto: " + servidor.getPort());

                Thread t = new Thread(new CONNECTION(cs));
                t.start();
            }
        } catch (IOException e){
                // Por omisión de texto se detecta el modo distrito
                System.out.println("Server de Distrito detectado...");

                //Logica servidor de distrito

        } catch (CmdLineException e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}
