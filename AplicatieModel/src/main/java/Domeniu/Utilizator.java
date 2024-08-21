package Domeniu;

import jakarta.persistence.*;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import jakarta.persistence.Entity;

//public class Utilizator extends Entitate implements Serializable
//{
//
//    String username;
//    String password;
//
//
//
//    public Utilizator(int id) {
//        super(id);
//    }
//
//    public Utilizator(String username, String password)
//    {
//        super(0);
//        this.username = username;
//        this.password = password;
//    }
//
//    public String getUsername()
//    {
//        return username;
//    }
//
//    public void setUsername(String username)
//    {
//        this.username = username;
//    }
//
//    public String getPassword()
//    {
//        return password;
//    }
//
//    public void setPassword(String password)
//    {
//        this.password = password;
//    }
//
//    @Override
//    public String toString()
//    {
//        return "UTILIZATOR: " +
//                "ID: " + getId() +
//                "\nUsername: " + username;
//    }
//}


@Entity
@Table(name = "Utilizator")
public class Utilizator implements Serializable, Entityy<Integer>
{
    private int id;
    private String username;
    private String password;


    public Utilizator()
    {
        id=0;
    }
    public Utilizator(int id)
    {
        this.id=id;
    }

    public Utilizator(String username, String password)
    {
        this.username = username;
        this.password = password;
    }



    @Column (name = "username")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Column (name = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    @Override
    public String toString()
    {
        return "UTILIZATOR: " +
                "ID: " + getId() +
                "\nUsername: " + username;
    }



}
