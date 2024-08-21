package DTOs;

import Domeniu.Client;
import Domeniu.Cursa;
import Domeniu.Rezervare;
import Domeniu.Utilizator;

public class DTOUtils {
    public static Client getFromClientDTO(ClientDTO clientDTO) {
        Integer id = clientDTO.id;
        String nume = clientDTO.nume;
        Client client = new Client(id);
        client.setNume(nume);
        return client;
    }

    public static ClientDTO getDTO(Client client) {
        String nume = client.getNume();
        Integer id = client.getId();
        return new ClientDTO(nume, id);
    }

    public static UtilizatorDTO getDTO(Utilizator utilizator) {
        String username = utilizator.getUsername();
        String password = utilizator.getPassword();
        Integer id = utilizator.getId();
        return new UtilizatorDTO(username, password, id);
    }

    public static Utilizator getFromUtilizatorDTO(UtilizatorDTO utilizatorDTO){
        Integer id = utilizatorDTO.id;
        String username = utilizatorDTO.username;
        String password = utilizatorDTO.password;
        Utilizator utilizator = new Utilizator(id);
        utilizator.setUsername(username);
        utilizator.setPassword(password);
        return utilizator;
    }

    public static RezervareDTO getDTO(Rezervare rezervare) {
        Integer id = rezervare.getId();
        CursaDTO cursa = getDTO(rezervare.getCursa());
        ClientDTO client = getDTO(rezervare.getClient());
        int nrLocuri = rezervare.getNrLocuri();
        return new RezervareDTO(id, cursa, client, nrLocuri);
    }

    public static Rezervare getFromRezervareDTO(RezervareDTO rezervareDTO) {
        Integer id = rezervareDTO.id;
        Cursa cursa = getFromCursaDTO(rezervareDTO.cursa);
        Client client = getFromClientDTO(rezervareDTO.client);
        int nrLocuri = rezervareDTO.nrLocuri;
        Rezervare rezervare = new Rezervare(id);
        rezervare.setCursa(cursa);
        rezervare.setClient(client);
        rezervare.setNrLocuri(nrLocuri);
        return rezervare;
    }

    public static CursaDTO getDTO(Cursa cursa) {
        Integer id = cursa.getId();
        String destinatie = cursa.getDestinatie();
        String data = cursa.getData();
        String ora = cursa.getOra();
        int nrLocuri = cursa.getLocuriDisponibile();
        return new CursaDTO(id, destinatie, data, ora, nrLocuri);
    }

    public static Cursa getFromCursaDTO(CursaDTO cursaDTO) {
        Integer id = cursaDTO.id;
        String destinatie = cursaDTO.destinatie;
        String data = cursaDTO.data;
        String ora = cursaDTO.ora;
        int nrLocuri = cursaDTO.locuriDisponibile;
        Cursa cursa = new Cursa(id);
        cursa.setDestinatie(destinatie);
        cursa.setData(data);
        cursa.setOra(ora);
        cursa.setLocuriDisponibile(nrLocuri);
        return cursa;
    }

    public static UtilizatorDTO[] getDTO(Utilizator[] utilizatori) {
        UtilizatorDTO[] utilizatoriDTO = new UtilizatorDTO[utilizatori.length];
        for (int i = 0; i < utilizatori.length; i++) {
            utilizatoriDTO[i] = getDTO(utilizatori[i]);
        }
        return utilizatoriDTO;
    }

    public static Utilizator[] getFromUtilizatorDTO(UtilizatorDTO[] utilizatoriDTO) {
        Utilizator[] utilizatori = new Utilizator[utilizatoriDTO.length];
        for (int i = 0; i < utilizatoriDTO.length; i++) {
            utilizatori[i] = getFromUtilizatorDTO(utilizatoriDTO[i]);
        }
        return utilizatori;
    }

    public static ClientDTO[] getDTO(Client[] clienti) {
        ClientDTO[] clientiDTO = new ClientDTO[clienti.length];
        for (int i = 0; i < clienti.length; i++) {
            clientiDTO[i] = getDTO(clienti[i]);
        }
        return clientiDTO;
    }

    public static Client[] getFromClientDTO(ClientDTO[] clientiDTO) {
        Client[] clienti = new Client[clientiDTO.length];
        for (int i = 0; i < clientiDTO.length; i++) {
            clienti[i] = getFromClientDTO(clientiDTO[i]);
        }
        return clienti;
    }

    public static CursaDTO[] getDTO(Cursa[] curse) {
        CursaDTO[] curseDTO = new CursaDTO[curse.length];
        for (int i = 0; i < curse.length; i++) {
            curseDTO[i] = getDTO(curse[i]);
        }
        return curseDTO;
    }

    public static Cursa[] getFromCursaDTO(CursaDTO[] curseDTO) {
        Cursa[] curse = new Cursa[curseDTO.length];
        for (int i = 0; i < curseDTO.length; i++) {
            curse[i] = getFromCursaDTO(curseDTO[i]);
        }
        return curse;
    }

    public static RezervareDTO[] getDTO(Rezervare[] rezervari) {
        RezervareDTO[] rezervariDTO = new RezervareDTO[rezervari.length];
        for (int i = 0; i < rezervari.length; i++) {
            rezervariDTO[i] = getDTO(rezervari[i]);
        }
        return rezervariDTO;
    }

    public static Rezervare[] getFromRezervareDTO(RezervareDTO[] rezervariDTO) {
        Rezervare[] rezervari = new Rezervare[rezervariDTO.length];
        for (int i = 0; i < rezervariDTO.length; i++) {
            rezervari[i] = getFromRezervareDTO(rezervariDTO[i]);
        }
        return rezervari;
    }




}
