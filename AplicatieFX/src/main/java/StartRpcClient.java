import Service.IService;
import javafx.application.Application;
import javafx.stage.Stage;
import rpcprotocol.AplicatieServicesRpcProxy;

import java.io.IOException;
import java.util.Properties;


public class StartRpcClient extends Application{

    private static int defaultChatPort=55555;
    private static String defaultServer="localhost";

        @Override
        public void start(Stage primaryStage) throws Exception {
            Properties clientProps=new Properties();
            try {
                clientProps.load(StartRpcClient.class.getResourceAsStream("/aplicatie.properties"));
                System.out.println("Client properties set. ");
                clientProps.list(System.out);
            } catch (IOException e) {
                System.err.println("Cannot find aplicatie.properties "+e);
                return;
            }
            String serverIP=clientProps.getProperty("chat.server.host",defaultServer);
            int serverPort=defaultChatPort;
            try{
                serverPort=Integer.parseInt(clientProps.getProperty("chat.server.port"));
            }catch(NumberFormatException ex){
                System.err.println("Wrong port number "+ex.getMessage());
                System.out.println("Using default port: "+defaultChatPort);
            }
            System.out.println("Using server IP "+serverIP);
            System.out.println("Using server port "+serverPort);
            IService server=new AplicatieServicesRpcProxy(serverIP, serverPort);

            LoginMain loginMain = new LoginMain(server);
            loginMain.start(primaryStage);
        }

}
