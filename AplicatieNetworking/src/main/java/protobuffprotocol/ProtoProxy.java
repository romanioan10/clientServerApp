package protobuffprotocol;

import Domeniu.Client;
import Domeniu.Cursa;
import Domeniu.Rezervare;
import Domeniu.Utilizator;
import Service.AppException;
import Service.IObserver;
import Service.IService;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static protobuffprotocol.AppProtobufs.Request;
import static protobuffprotocol.AppProtobufs.Response;
import static protobuffprotocol.AppProtobufs.Response.Type.*;

public class ProtoProxy implements IService {
    private String host;
    private int port;

    private IObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<AppProtobufs.Response> qresponses;
    private volatile boolean finished;

    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    public void login(Utilizator user, IObserver client) throws AppException {
        initializeConnection();
        Request req = ProtoUtils.createLoginRequest(user);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == OK) {
            this.client = client;
            return;
        }
        if (response.getType() == ERROR) {
            String err = response.getError();
            closeConnection();
            throw new AppException(err);
        }
    }

    public void logout(Utilizator user) throws AppException {
        Request req = ProtoUtils.createLogoutRequest(user);
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.getType() == ERROR) {
            String err = response.getError();
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

    private void sendRequest(AppProtobufs.Request request) throws AppException {
        try {
            System.out.println("Sending request ..."+request);

            request.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            throw new AppException("Error sending object " + e);
        }

    }

    private AppProtobufs.Response readResponse() {
        AppProtobufs.Response response = null;
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
            output = connection.getOutputStream();
            output.flush();
            input = connection.getInputStream();
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


    private boolean isUpdate(AppProtobufs.Response.Type type) {
        return type == REZERVARE_NOUA || type == CURSE_UPDATE;
    }

    @Override
    public Client getByName(String nume) throws AppException {
        Request req = ProtoUtils.createGetClientByNameRequest(nume);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ERROR) {
            String err = response.getError();
            return null;
        }
        return ProtoUtils.getClient(response);


    }

    @Override
    public List<Cursa> cautareCurseDupaDestinatie(String destinatie) throws AppException {
        Request req = ProtoUtils.createCautareCurseDupaDestinatieRequest(destinatie);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ERROR) {
            String err = response.getError();
            throw new AppException(err);
        }

        return ProtoUtils.getCurse(response);
    }

    @Override
    public List<String> cautareCurse(String destinatie, String data, String ora) throws AppException {
//        Request req = new Request.Builder().type(CAUTARE_CURSE).data(new String[]{destinatie, data, ora}).build();
//        sendRequest(req);
//        Response response = readResponse();
//        if (response.getType() == ERROR) {
//            String err = response.getError();
//            return null;
//        }
//        CursaDTO[] cursaDTOS = (CursaDTO[]) response.data();
//        List<String> curse = new ArrayList<>();
//        for (CursaDTO cdto : cursaDTOS) {
//            curse.add(cdto.toString());
//        }
        return null;
    }

    @Override
    public void addRezervare(Rezervare rezervare) throws AppException {
        Request req = ProtoUtils.createAddRezervareRequest(rezervare);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ERROR) {
            String err = response.getError();
            throw new AppException(err);
        }
    }

    @Override
    public Collection<Rezervare> getAllRezervari() throws AppException {

        Request req = ProtoUtils.createGetAllRezervariRequest();
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ERROR) {
            String err = response.getError();
            throw new AppException(err);
        }
        return ProtoUtils.getRezervari(response);
    }

    @Override
    public Collection<Cursa> getAllCurse() throws AppException {
        Request req = ProtoUtils.createGetAllCurseRequest();
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ERROR) {
            String err = response.getError();
            throw new AppException(err);
        }
        return ProtoUtils.getCurse(response);
    }


    private void handleUpdate(Response response) throws AppException {
        if (response.getType() == REZERVARE_NOUA)
            client.updateRezervari(ProtoUtils.getRezervari(response));
        else if (response.getType() == CURSE_UPDATE)
            client.updateCurse(ProtoUtils.getCurse(response));
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    AppProtobufs.Response response = AppProtobufs.Response.parseDelimitedFrom(input);
                    System.out.println("response received " + response);
                    if (isUpdate( response.getType())) {
                        handleUpdate(response);
                    } else {

                        try {
                            qresponses.put( response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (AppException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
