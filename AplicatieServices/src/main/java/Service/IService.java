package Service;

import Domeniu.Client;
import Domeniu.Cursa;
import Domeniu.Rezervare;
import Domeniu.Utilizator;

import java.util.Collection;
import java.util.List;

public interface IService
{
    public Client getByName(String nume) throws AppException;

    public List<Cursa> cautareCurseDupaDestinatie(String destinatie) throws AppException;

    public List<String> cautareCurse(String destinatie, String data, String ora) throws AppException;

    public void addRezervare(Rezervare rezervare) throws AppException;
    public Collection<Rezervare> getAllRezervari() throws AppException;
    public Collection<Cursa> getAllCurse() throws AppException;

    public void login(Utilizator user, IObserver client) throws AppException;

    public void logout(Utilizator user) throws AppException;
}
