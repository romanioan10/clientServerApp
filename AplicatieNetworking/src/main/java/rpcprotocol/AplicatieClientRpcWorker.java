package rpcprotocol;

import DTOs.*;
import Domeniu.Client;
import Domeniu.Cursa;
import Domeniu.Rezervare;
import Domeniu.Utilizator;
import Service.AppException;
import Service.IObserver;
import Service.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class AplicatieClientRpcWorker implements Runnable, IObserver {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public AplicatieClientRpcWorker(IService server, Socket connection) {
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
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException | AppException | ClassNotFoundException e) {
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


    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) throws AppException {
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            UtilizatorDTO udto=(UtilizatorDTO) request.data();
            Utilizator user= DTOUtils.getFromUtilizatorDTO(udto);
            try {
                server.login(user, this);
                return okResponse;
            } catch (AppException e)
            {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            UtilizatorDTO udto=(UtilizatorDTO) request.data();
            Utilizator user= DTOUtils.getFromUtilizatorDTO(udto);
            try {
                server.logout(user);
                connected=false;
                return okResponse;

            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.GET_ALL_REZERVARI)
        {
            System.out.println("GetAllRezervari Request ...");
            try {
                Rezervare[] rezervari = server.getAllRezervari().toArray(new Rezervare[0]);
                RezervareDTO[] rezervareDTOS = DTOUtils.getDTO(rezervari);
                return new Response.Builder().type(ResponseType.SEND_ALL_REZERVARI).data(rezervareDTOS).build();
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.GET_ALL_CURSE)
        {
            System.out.println("GetAllCurse Request ...");
            try {
                Cursa[] curse = server.getAllCurse().toArray(new Cursa[0]);
                CursaDTO[] cursaDTOS = DTOUtils.getDTO(curse);
                return new Response.Builder().type(ResponseType.SEND_ALL_CURSE).data(cursaDTOS).build();
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.ADD_REZERVARE){
            System.out.println("AddRezervare Request ...");
            RezervareDTO rdto=(RezervareDTO) request.data();
            Rezervare rezervare= DTOUtils.getFromRezervareDTO(rdto);
            try {
                server.addRezervare(rezervare);
                return okResponse;
            } catch (AppException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.GET_CLIENT_BY_NAME)
        {
            System.out.println("GetClientByName Request ...");
            String nume = (String) request.data();
            try {
                Client utilizator = server.getByName(nume);
                ClientDTO utilizatorDTO = DTOUtils.getDTO(utilizator);
                return new Response.Builder().type(ResponseType.SEND_CLIENT).data(utilizatorDTO).build();
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.CAUTARE_CURSE_DUPA_DESTINATIE)
        {
            System.out.println("CautareCurseDupaDestinatie Request ...");
            String destinatie = (String) request.data();
            try {
                List<Cursa> curse = server.cautareCurseDupaDestinatie(destinatie);
                CursaDTO[] cursaDTOS = DTOUtils.getDTO(curse.toArray(new Cursa[0]));
                return new Response.Builder().type(ResponseType.SEND_SPECIFIC_CURSE).data(cursaDTOS).build();
            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
//        if(request.type() == RequestType.CAUTARE_CURSE)
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

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    @Override
    public void updateRezervari(Collection<Rezervare> rezervari) throws AppException
    {
        Collection<RezervareDTO> rezervareDTOS = rezervari.stream()
                .map(DTOUtils::getDTO)
                .toList();
        Response response = new Response.Builder().type(ResponseType.REZERVARE_NOUA  ).data(rezervareDTOS).build();
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateCurse(Collection<Cursa> curse) throws AppException
    {
        Collection<CursaDTO> curseDTOs = curse.stream()
                .map(DTOUtils::getDTO)
                .toList();
        Response response = new Response.Builder().type(ResponseType.CURSE_UPDATE).data(curseDTOs).build();
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
