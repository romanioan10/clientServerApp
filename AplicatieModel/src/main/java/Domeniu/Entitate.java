package Domeniu;

public abstract class Entitate implements IEntitate<Integer> {
    protected int id;

    public Entitate(int id) {
        this.id = id;
    }

    public Entitate()
    {

    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    @Override
    public Integer getId() {
        return id;
    }

}
