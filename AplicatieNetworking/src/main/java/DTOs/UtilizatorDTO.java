package DTOs;

import java.io.Serializable;

public class UtilizatorDTO implements Serializable {
    public String username;
    public String password;
    public Integer id;

    public UtilizatorDTO(String username, String password, Integer id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    @Override
    public String toString() {
        return "UtilizatorDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
