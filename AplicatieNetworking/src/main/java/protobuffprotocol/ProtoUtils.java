package protobuffprotocol;


import Domeniu.Client;
import Domeniu.Cursa;
import Domeniu.Rezervare;
import Domeniu.Utilizator;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProtoUtils {
    public static AppProtobufs.Request createLoginRequest(Utilizator user){
        AppProtobufs.User userDTO=AppProtobufs.User.newBuilder().setUsername(user.getUsername()).setPasswd(user.getPassword()).build();
        AppProtobufs.Request request= AppProtobufs.Request.newBuilder().setType(AppProtobufs.Request.Type.LOGIN)
                .setUser(userDTO).build();
        return request;
    }
    public static AppProtobufs.Request createLogoutRequest(Utilizator user){
        AppProtobufs.User userDTO=AppProtobufs.User.newBuilder().setUsername(user.getUsername()).build();
        AppProtobufs.Request request= AppProtobufs.Request.newBuilder().setType(AppProtobufs.Request.Type.LOGOUT)
                .setUser(userDTO).build();
        return request;
    }

    private static AppProtobufs.Client createClient(Client client)
    {
        return AppProtobufs.Client.newBuilder()
                .setId(client.getId())
                .setNume(client.getNume())
                .build();
    }
    
    private static Client createClient(AppProtobufs.Client protoClient){
        Client client = new Client(protoClient.getNume());
        client.setId(protoClient.getId());
        return client;
    }

    private static AppProtobufs.Cursa createCursa(Cursa cursa)
    {
        return AppProtobufs.Cursa.newBuilder()
                .setId(cursa.getId())
                .setDestinatie(cursa.getDestinatie())
                .setData(cursa.getData())
                .setOra(cursa.getOra())
                .setLocuriDisponibile(cursa.getLocuriDisponibile())
                .build();
    }
    private static Cursa createCursa(AppProtobufs.Cursa protoCursa){
        Cursa cursa = new Cursa(protoCursa.getDestinatie(), protoCursa.getData(), protoCursa.getOra());
        cursa.setId(protoCursa.getId());
        cursa.setLocuriDisponibile(protoCursa.getLocuriDisponibile());
        return cursa;
    }

    private static AppProtobufs.Rezervare createRezervare(Rezervare rezervare)
    {
        return AppProtobufs.Rezervare.newBuilder()
                .setId(rezervare.getId())
                .setClient(createClient(rezervare.getClient()))
                .setCursa(createCursa(rezervare.getCursa()))
                .setLocuriDisponibile(rezervare.getNrLocuri())
                .build();
    }
    
    private static Rezervare createRezervare(AppProtobufs.Rezervare protoRezervare){
        Rezervare rezervare = new Rezervare(createClient(protoRezervare.getClient()), createCursa(protoRezervare.getCursa()), protoRezervare.getLocuriDisponibile());
        rezervare.setId(protoRezervare.getId());
        return rezervare;
    }
    
    public static AppProtobufs.Request createAddRezervareRequest(Rezervare rezervare){
        AppProtobufs.Rezervare rezervareDTO= createRezervare(rezervare);
        AppProtobufs.Request request= AppProtobufs.Request.newBuilder()
                .setType(AppProtobufs.Request.Type.ADD_REZERVARE)
                .setRezervare(rezervareDTO)
                .build();
        return request;
    }

    public static AppProtobufs.Request createGetAllCurseRequest(){
        AppProtobufs.Request request= AppProtobufs.Request.newBuilder()
                .setType(AppProtobufs.Request.Type.GET_ALL_CURSE)
                .build();
        return request;
    }

    public static AppProtobufs.Request createGetAllRezervariRequest()
    {
        AppProtobufs.Request request= AppProtobufs.Request.newBuilder()
                .setType(AppProtobufs.Request.Type.GET_ALL_REZERVARI)
                .build();
        return request;
    }

    public static AppProtobufs.Request createCautareCurseDupaDestinatieRequest(String destinatie)
    {
        AppProtobufs.Request request = AppProtobufs.Request.newBuilder()
                .setType(AppProtobufs.Request.Type.CAUTARE_CURSE_DUPA_DESTINATIE)
                .setDestinatie(destinatie)
                .build();
        return request;
    }

    public static AppProtobufs.Request createGetClientByNameRequest(String nume)
    {
        AppProtobufs.Request request = AppProtobufs.Request.newBuilder()
                .setType(AppProtobufs.Request.Type.GET_CLIENT_BY_NAME)
                .setNume(nume)
                .build();
        return request;
    }

    private static AppProtobufs.Rezervari createRezervari(Collection<Rezervare> rezervari)
    {
        return AppProtobufs.Rezervari.newBuilder()
                .addAllRezervari(rezervari.stream().map(ProtoUtils::createRezervare).collect(Collectors.toList()))
                .build();
    }

    private static Collection<Rezervare> createRezervari(AppProtobufs.Rezervari rezervariProto){
        return rezervariProto.getRezervariList().stream().map(ProtoUtils::createRezervare).toList();
    }

    private static AppProtobufs.Curse createCurse(Collection<Cursa> curse)
    {
        return AppProtobufs.Curse.newBuilder()
                .addAllCurse(curse.stream().map(ProtoUtils::createCursa).collect(Collectors.toList()))
                .build();
    }

    public static AppProtobufs.Response createRezervareNouaResponse(Collection<Rezervare> rezervari)
    {
        AppProtobufs.Rezervari rezervariDTO = createRezervari(rezervari);
        AppProtobufs.Response response = AppProtobufs.Response.newBuilder()
                .setType(AppProtobufs.Response.Type.REZERVARE_NOUA)
                .setRezervari(rezervariDTO)
                .build();
        return response;
    }

    public static AppProtobufs.Response createUpdateCurseResponse(Collection<Cursa> curse)
    {
        AppProtobufs.Curse curseDTO = createCurse(curse);
        AppProtobufs.Response response = AppProtobufs.Response.newBuilder()
                .setType(AppProtobufs.Response.Type.CURSE_UPDATE)
                .setCurse(curseDTO)
                .build();
        return response;
    }

    public static AppProtobufs.Response createSendAllRezervariResponse(Collection<Rezervare> rezervari)
    {
        AppProtobufs.Rezervari rezervariDTO = createRezervari(rezervari);
        AppProtobufs.Response response = AppProtobufs.Response.newBuilder()
                .setType(AppProtobufs.Response.Type.SEND_ALL_REZERVARI)
                .setRezervari(rezervariDTO)
                .build();
        return response;
    }

    public static AppProtobufs.Response createSendAllCurseResponse(Collection<Cursa> curse)
    {
        AppProtobufs.Curse curseDTO = createCurse(curse);
        AppProtobufs.Response response = AppProtobufs.Response.newBuilder()
                .setType(AppProtobufs.Response.Type.SEND_ALL_CURSE)
                .setCurse(curseDTO)
                .build();
        return response;
    }

    public static AppProtobufs.Response createSendClientResponse(Client client)
    {
        AppProtobufs.Client clientDTO = createClient(client);
        AppProtobufs.Response response = AppProtobufs.Response.newBuilder()
                .setType(AppProtobufs.Response.Type.SEND_CLIENT)
                .setClient(clientDTO)
                .build();
        return response;
    }

    public static AppProtobufs.Response createSendSpecificCurseResponse(Collection<Cursa> curse)
    {
        AppProtobufs.Curse curseDTO = createCurse(curse);
        AppProtobufs.Response response = AppProtobufs.Response.newBuilder()
                .setType(AppProtobufs.Response.Type.SEND_SPECIFIC_CURSE)
                .setCurse(curseDTO)
                .build();
        return response;
    }

    public static AppProtobufs.Response createOKResponse(){
        AppProtobufs.Response response=AppProtobufs.Response.newBuilder()
                .setType(AppProtobufs.Response.Type.OK).build();
        return response;
    }

    public static AppProtobufs.Response createERRORResponse(String text){
        AppProtobufs.Response response=AppProtobufs.Response.newBuilder()
                .setType(AppProtobufs.Response.Type.ERROR)
                .setError(text).build();
        return response;
    }

    public static String getERROR(AppProtobufs.Response response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static Utilizator getUser(AppProtobufs.Request request){
        Utilizator user = new Utilizator(request.getUser().getUsername(), request.getUser().getPasswd());
        user.setId(request.getUser().getId());
        return user;
    }

    public static Utilizator getUser(AppProtobufs.Response response){
        Utilizator user = new Utilizator(response.getUtilizator().getUsername(), response.getUtilizator().getPasswd());
        user.setId(response.getUtilizator().getId());
        return user;
    }
    
    public static Client getClient(AppProtobufs.Response response){
        Client client = new Client(response.getClient().getNume());
        client.setId(response.getClient().getId());
        return client;
    }
    
    public static Rezervare getRezervare(AppProtobufs.Request request){
        return createRezervare(request.getRezervare());
    }

    public static Collection<Rezervare> getRezervari(AppProtobufs.Response response){
        return createRezervari(response.getRezervari());
    }

    public static List<Cursa> getCurse(AppProtobufs.Response response) {
        AppProtobufs.Curse curseProto = response.getCurse();
        return curseProto.getCurseList().stream().map(ProtoUtils::createCursa).toList();
    }
}
