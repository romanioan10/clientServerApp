package Repository.Interface;

public interface IRepository<ID, T>
{
    public void add(T entitate);

    public void update(ID id, T entitate);
    public void remove(ID id);
    public T findOne(ID id);
    public Iterable<T> getAll();
    public void setAll(Iterable<T> entitati);

}
