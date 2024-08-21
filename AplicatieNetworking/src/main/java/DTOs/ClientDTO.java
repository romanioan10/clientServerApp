package DTOs;

import java.io.Serializable;

public class ClientDTO implements Serializable {
    public String nume;
    public Integer id;
    public ClientDTO(String nume, Integer id) {
        this.nume = nume;
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
                "nume='" + nume + '\'' +
                ", id=" + id +
                '}';
    }
}
