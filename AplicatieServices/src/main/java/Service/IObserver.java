package Service;

import Domeniu.Cursa;
import Domeniu.Rezervare;

import java.util.Collection;
import java.util.List;

public interface IObserver {
    public void updateCurse(Collection<Cursa> curse) throws AppException;
    public void updateRezervari(Collection<Rezervare> rezervari) throws AppException;
}
