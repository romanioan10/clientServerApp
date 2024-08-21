package Repository.Interface;

import Domeniu.Cursa;

public interface ICursaRepository extends IRepository<Integer, Cursa>
{
    public Cursa findById(Integer id);
    public Cursa findByAll(String destinatie,String data, String ora, int locuri);
}
