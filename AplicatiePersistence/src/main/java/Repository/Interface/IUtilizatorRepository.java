package Repository.Interface;

import Domeniu.Utilizator;

public interface IUtilizatorRepository extends IRepository<Integer, Utilizator>{

    public Utilizator findByUsername(String username);
}
