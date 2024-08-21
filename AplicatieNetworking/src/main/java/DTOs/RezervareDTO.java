package DTOs;

import Domeniu.Client;
import Domeniu.Cursa;

import java.io.Serializable;

public class RezervareDTO implements Serializable {
    public Integer id;
    public CursaDTO cursa;
    public ClientDTO client;
    int nrLocuri;

    public RezervareDTO(Integer id, CursaDTO cursa, ClientDTO client, int nrLocuri) {
        this.id = id;
        this.cursa = cursa;
        this.client = client;
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return "RezervareDTO{" +
                "id=" + id +
                ", cursa=" + cursa +
                ", client=" + client +
                ", nrLocuri=" + nrLocuri +
                '}';
    }
}
