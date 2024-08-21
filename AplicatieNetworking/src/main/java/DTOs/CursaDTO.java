package DTOs;

import java.io.Serializable;

public class CursaDTO implements Serializable {
    public Integer id;
    public String destinatie;
    public String data;
    public String ora;
    public int locuriDisponibile;
    public CursaDTO(Integer id, String destinatie, String data, String ora, Integer locuriDisponibile) {
        this.id = id;
        this.destinatie = destinatie;
        this.data = data;
        this.ora = ora;
        this.locuriDisponibile = locuriDisponibile;
    }

    @Override
    public String toString() {
        return "CursaDTO{" +
                "id=" + id +
                ", destinatie='" + destinatie + '\'' +
                ", data='" + data + '\'' +
                ", ora='" + ora + '\'' +
                ", locuriDisponibile=" + locuriDisponibile +
                '}';
    }
}
