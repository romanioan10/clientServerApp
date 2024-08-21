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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;


public class AplicatieServicesRpcProxy implements IService {
    private String host;
    private int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public AplicatieServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    public void login(Utilizator user, IObserver client) throws AppException {
        initializeConnection();
        UtilizatorDTO udto = DTOUtils.getDTO(user);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(udto).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new AppException(err);
        }
    }

    public void logout(Utilizator user) throws AppException {
        UtilizatorDTO udto = DTOUtils.getDTO(user);
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws AppException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new AppException("Error sending object " + e);
        }

    }

    private Response readResponse() {
        Response response = null;
        try {

            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.REZERVARE_NOUA || response.type() == ResponseType.CURSE_UPDATE;
    }

    @Override
    public Client getByName(String nume) throws AppException {
        Request req = new Request.Builder().type(RequestType.GET_CLIENT_BY_NAME).data(nume).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            return null;
        }
        ClientDTO cdto = (ClientDTO) response.data();
        return DTOUtils.getFromClientDTO(cdto);


    }

    @Override
    public List<Cursa> cautareCurseDupaDestinatie(String destinatie) throws AppException {
        Request req = new Request.Builder().type(RequestType.CAUTARE_CURSE_DUPA_DESTINATIE).data(destinatie).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        CursaDTO[] cursaDTOS = (CursaDTO[]) response.data();
        List<Cursa> curse = new ArrayList<>();
        for (CursaDTO cdto : cursaDTOS) {
            curse.add(DTOUtils.getFromCursaDTO(cdto));
        }
        return curse;
    }

    @Override
    public List<String> cautareCurse(String destinatie, String data, String ora) throws AppException {
        Request req = new Request.Builder().type(RequestType.CAUTARE_CURSE).data(new String[]{destinatie, data, ora}).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            return null;
        }
        CursaDTO[] cursaDTOS = (CursaDTO[]) response.data();
        List<String> curse = new ArrayList<>();
        for (CursaDTO cdto : cursaDTOS) {
            curse.add(cdto.toString());
        }
        return curse;
    }

    @Override
    public void addRezervare(Rezervare rezervare) throws AppException {
        RezervareDTO rdto = DTOUtils.getDTO(rezervare);
        Request req = new Request.Builder().type(RequestType.ADD_REZERVARE).data(rdto).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
    }

    @Override
    public Collection<Rezervare> getAllRezervari() throws AppException {

        Request req = new Request.Builder().type(RequestType.GET_ALL_REZERVARI).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        Collection<Rezervare> rezervari = new ArrayList<>();
        RezervareDTO[] rezervareDTOs = (RezervareDTO[]) response.data();
        for (RezervareDTO rezervareDTO : rezervareDTOs) {
            rezervari.add(DTOUtils.getFromRezervareDTO(rezervareDTO));
        }
        return rezervari;
    }

    @Override
    public Collection<Cursa> getAllCurse() throws AppException {
        Request req = new Request.Builder().type(RequestType.GET_ALL_CURSE).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        Collection<Cursa> curse = new ArrayList<>();
        CursaDTO[] cursaDTOs = (CursaDTO[]) response.data();
        for (CursaDTO cursaDTO : cursaDTOs) {
            curse.add(DTOUtils.getFromCursaDTO(cursaDTO));
        }
        return curse;
    }


    private void handleUpdate(Response response) throws AppException {
        if (response.type() == ResponseType.REZERVARE_NOUA) {
            Collection<RezervareDTO> rezervareDTOS = (Collection<RezervareDTO>) response.data();
            Collection<Rezervare> rezervari = rezervareDTOS.stream().map(DTOUtils::getFromRezervareDTO).toList();
            client.updateRezervari(rezervari);
        } else if (response.type() == ResponseType.CURSE_UPDATE) {
            Collection<CursaDTO> cursaDTOS = (Collection<CursaDTO>) response.data();
            Collection<Cursa> curse = cursaDTOS.stream().map(DTOUtils::getFromCursaDTO).toList();
            client.updateCurse(curse);
        }
    }



    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                } catch (AppException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
