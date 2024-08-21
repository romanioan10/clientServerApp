package Repository;

import Domeniu.Client;
import Repository.Interface.IClientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientDBRepository implements IClientRepository
{

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public ClientDBRepository(Properties props)
    {
        logger.info("Initializing ClientDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }



    @Override
    public void add(Client entitate)
    {
        logger.traceEntry("saving client {} ",entitate);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("insert into Client (nume_client) values (?)"))
        {
            preStmt.setString(1,entitate.getNume());

            preStmt.executeUpdate();
            logger.info("Client added successfully");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();

    }

    @Override
    public void update(Integer integer, Client entitate)
    {
        logger.traceEntry("modifying client with id {} ",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Client set nume_client=? where id_client=?"))
        {
            preStmt.setInt(3,integer);
            preStmt.setString(1,entitate.getNume());

            preStmt.executeUpdate();
            logger.info("Client modified successfully");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();

    }

    @Override
    public void remove(Integer integer)
    {
        logger.traceEntry("deleting client with {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Client where id_client=?"))
        {
            preStmt.setInt(1,integer);
            preStmt.executeUpdate();
            logger.info("Client deleted successfully");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();

    }

    @Override
    public Client findOne(Integer integer)
    {
        logger.traceEntry("finding client with id {} ", integer);
        Connection con = dbUtils.getConnection();
        Client client = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Client where id_client=?")) {
            preStmt.setInt(1, integer);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String nume = result.getString("nume_client");
                    client = new Client(integer);
                    client.setNume(nume);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(client);
        return client;

    }

    @Override
    public Iterable<Client> getAll()
    {
        logger.traceEntry("finding all clients");
        Connection con = dbUtils.getConnection();
        List<Client> clienti= new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Client"))
        {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_client");
                    String nume = result.getString("nume_client");
                    Client client = new Client(id);
                    client.setNume(nume);
                    clienti.add(client);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(clienti);
        return clienti;
    }

    @Override
    public void setAll(Iterable<Client> entitati)
    {
        logger.traceEntry("setting all clients");
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Client")) {
            preStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        for (Client client : entitati) {
            add(client);
        }
        logger.traceExit();

    }

}
