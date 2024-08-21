package Domeniu;

import java.io.Serializable;

public class Cursa extends Entitate implements Serializable
{

    String destinatie;
    String data;
    String ora;
    int locuriDisponibile;

    public Cursa(int id) {
        super(id);
    }

    public Cursa()
    {
        super();
    }

    public Cursa(String destinatie, String data, String ora)
    {
        super(0);
        this.destinatie = destinatie;
        this.data = data;
        this.ora = ora;
        this.locuriDisponibile = 18;
    }
    public Cursa(String destinatie, String data, String ora, int locuriDisponibile)
    {
        super(0);
        this.destinatie = destinatie;
        this.data = data;
        this.ora = ora;
        this.locuriDisponibile = locuriDisponibile;
    }



    public String getDestinatie()
    {
        return destinatie;
    }

    public void setDestinatie(String destinatie)
    {
        this.destinatie = destinatie;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getOra()
    {
        return ora;
    }

    public void setOra(String ora)
    {
        this.ora = ora;
    }

    public int getLocuriDisponibile()
    {
        return locuriDisponibile;
    }

    public void setLocuriDisponibile(int locuriDisponibile)
    {
        this.locuriDisponibile = locuriDisponibile;
    }

    @Override
    public String toString()
    {
        return "CURSA: " +
                "\nDestinatie: " + destinatie +
                "\nData: " + data +
                "\nOra: " + ora +
                "\nNumar de locuri disponibile: " + locuriDisponibile;
    }
}
