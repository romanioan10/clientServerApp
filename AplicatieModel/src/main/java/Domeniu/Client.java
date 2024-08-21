package Domeniu;

import jakarta.persistence.*;

import java.io.Serializable;

public class Client extends Entitate implements Serializable {
    private String nume;

    public Client(int id) {
        super(id);
    }

    public Client(String nume)
    {
        super(0);
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "CLIENT: " +
                "Nume: " + nume;
    }
}


//@Entity
//@Table(name = "client")
//public class Client extends Entitate implements Serializable {
//    @Column(name = "nume")
//    private String nume;
//
//    public Client(int id) {
//        super(id);
//    }
//
//    public Client(String nume)
//    {
//        super(0);
//        this.nume = nume;
//    }
//
//    public Client() {
//        super(0);
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    public Integer getId() {
//        return id;
//    }
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getNume() {
//        return nume;
//    }
//    public void setNume(String nume) {
//        this.nume = nume;
//    }
//
//    @Override
//    public String toString() {
//        return "CLIENT: " +
//                "Nume: " + nume;
//    }
//}
