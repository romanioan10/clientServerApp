package utils;

import Service.IService;
import rpcprotocol.AplicatieClientRpcWorker;

import java.net.Socket;


public class ChatRpcConcurrentServer extends AbsConcurrentServer {
    private IService chatServer;
    public ChatRpcConcurrentServer(int port, IService chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AplicatieClientRpcWorker worker=new AplicatieClientRpcWorker(chatServer, client);
//        ChatClientRpcReflectionWorker worker=new ChatClientRpcReflectionWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
