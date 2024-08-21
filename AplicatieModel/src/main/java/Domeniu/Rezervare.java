package Domeniu;

import java.io.Serializable;

public class Rezervare extends Entitate implements Serializable
{


    public Cursa cursa;
    Client client;
    int nrLocuri;

    public Rezervare(int id) {
        super(id);
    }

    public Rezervare(Client client, Cursa cursa, int nrLocuri)
    {
        super(0);
        this.cursa = cursa;
        this.client = client;
        this.nrLocuri = nrLocuri;
    }

    public Cursa getCursa()
    {
        return cursa;
    }

    public void setCursa(Cursa cursa)
    {
        this.cursa = cursa;
    }

    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public int getIdCursa()
    {
        return cursa.getId();
    }

    public int getIdClient()
    {
        return client.getId();
    }

    public void setIdCursa(int id)
    {
        cursa.setId(id);
    }

    public void setIdClient(int id)
    {
        client.setId(id);
    }


    public int getNrLocuri()
    {
        return nrLocuri;
    }

    public void setNrLocuri(int nrLocuri)
    {
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return "REZERVAREA: " +
                "ID: " + getId() +
                "\nClient: " + client.toString() +
                "\nCursa: " + cursa.toString() +
                "\nNumar de locuri: " + nrLocuri;
    }

}
