package protobuffprotocol;

import DTOs.*;
import Domeniu.Client;
import Domeniu.Cursa;
import Domeniu.Rezervare;
import Domeniu.Utilizator;
import Service.AppException;
import Service.IObserver;
import Service.IService;
import rpcprotocol.Request;
import rpcprotocol.RequestType;
import rpcprotocol.Response;
import rpcprotocol.ResponseType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static protobuffprotocol.AppProtobufs.Request.Type.*;


public class ProtoWorker implements Runnable, IObserver {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ProtoWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
//                Object request=input.readObject();
                System.out.println("Waiting requests ...");
                AppProtobufs.Request request=AppProtobufs.Request.parseDelimitedFrom(input);
                System.out.println("Request received: "+request);
                AppProtobufs.Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException | AppException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }


    private static final AppProtobufs.Response okResponse= ProtoUtils.createOKResponse();

    private AppProtobufs.Response handleRequest(AppProtobufs.Request request) throws AppException {
        AppProtobufs.Response response=null;
        if (request.getType()== LOGIN){
            System.out.println("Login request ..."+request.getType());
            try {
                server.login(ProtoUtils.getUser(request), this);
                return okResponse;
            } catch (AppException e)
            {
                connected=false;
                return ProtoUtils.createERRORResponse(e.getMessage());
            }
        }
        if (request.getType()== LOGOUT){
            System.out.println("Logout request");
            try {
                server.logout(ProtoUtils.getUser(request));
                connected=false;
                return okResponse;

            } catch (AppException e) {
                return ProtoUtils.createERRORResponse(e.getMessage());
            }
        }
        if (request.getType()== GET_ALL_REZERVARI)
        {
            System.out.println("GetAllRezervari Request ...");
            try {
                Collection<Rezervare> rezervari = server.getAllRezervari();
                return ProtoUtils.createSendAllRezervariResponse(rezervari);
            } catch (AppException e) {
                return ProtoUtils.createERRORResponse(e.getMessage());
            }
        }

        if (request.getType()== GET_ALL_CURSE)
        {
            System.out.println("GetAllCurse Request ...");
            try {
                Collection<Cursa> curse = server.getAllCurse();
                return ProtoUtils.createSendAllCurseResponse(curse);
            } catch (AppException e) {
                return ProtoUtils.createERRORResponse(e.getMessage());
            }
        }
        if (request.getType()== ADD_REZERVARE){
            System.out.println("AddRezervare Request ...");
            Rezervare rdto= ProtoUtils.getRezervare(request);
            try {
                server.addRezervare(rdto);
                return okResponse;
            } catch (AppException e)
            {
                return ProtoUtils.createERRORResponse(e.getMessage());
            }
        }
        if(request.getType() == GET_CLIENT_BY_NAME)
        {
            System.out.println("GetClientByName Request ...");
            String nume = request.getNume();
            try {
                Client utilizator = server.getByName(nume);
                return ProtoUtils.createSendClientResponse(utilizator);
            } catch (AppException e) {
                return ProtoUtils.createERRORResponse(e.getMessage());
            }
        }
        if(request.getType() == CAUTARE_CURSE_DUPA_DESTINATIE)
        {
            System.out.println("CautareCurseDupaDestinatie Request ...");
            String destinatie = request.getDestinatie();
            try {
                List<Cursa> curse = server.cautareCurseDupaDestinatie(destinatie);
                return ProtoUtils.createSendSpecificCurseResponse(curse);
            } catch (AppException e) {
                return ProtoUtils.createERRORResponse(e.getMessage());
            }
        }
//        if(request.getType() == CAUTARE_CURSE)
//        {
//            System.out.println("CautareCurse Request ...");
//            String[] data = (String[]) request.data();
//
//
//            List<String> curse = server.cautareCurse(data[0], data[1], data[2]);
//            return new Response.Builder().type(ResponseType.SEND_ALL).data(curse).build();
//        }
        return response;
    }

    private void sendResponse(AppProtobufs.Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    @Override
    public void updateRezervari(Collection<Rezervare> rezervari) throws AppException
    {

        AppProtobufs.Response response = ProtoUtils.createRezervareNouaResponse(rezervari);
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateCurse(Collection<Cursa> curse) throws AppException
    {

        AppProtobufs.Response  response = ProtoUtils.createUpdateCurseResponse(curse);
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
