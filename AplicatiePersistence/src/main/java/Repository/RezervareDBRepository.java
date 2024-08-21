package Repository;

import Domeniu.Client;
import Domeniu.Cursa;
import Domeniu.Rezervare;
import Repository.Interface.IClientRepository;
import Repository.Interface.ICursaRepository;
import Repository.Interface.IRezervareRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class RezervareDBRepository implements IRezervareRepository
{
    private JdbcUtils dbUtils;

    Properties props = new Properties();

//    public void initializare()
//    {
//        try {
//            props.load(new FileReader("bd.config"));
//        } catch (IOException e) {
//            System.out.println("Cannot find bd.config "+e);
//        }
//    }

    IClientRepository clientDbRepository;
    ICursaRepository cursaDbRepository;

    private static final Logger logger= LogManager.getLogger();

    public RezervareDBRepository(Properties props, IClientRepository clientDbRepository, ICursaRepository cursaDbRepository) {
        logger.info("Initializing RezervareDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
        this.clientDbRepository = clientDbRepository;
        this.cursaDbRepository = cursaDbRepository;
    }


    @Override
    public void add(Rezervare entitate)
    {
        logger.traceEntry("saving rezervare {} ",entitate);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("insert into Rezervare (id_client, id_cursa, locuri_rezervate) values (?,?,?)"))
        {
            preStmt.setInt(1,entitate.getIdClient());
            preStmt.setInt(2,entitate.getIdCursa());
            preStmt.setInt(3,entitate.getNrLocuri());
            if(entitate.getNrLocuri()>cursaDbRepository.findOne(entitate.getIdCursa()).getLocuriDisponibile())
                throw new RuntimeException("Nu sunt suficiente locuri");
            entitate.getCursa().setLocuriDisponibile(entitate.getCursa().getLocuriDisponibile()-entitate.getNrLocuri());
            try(PreparedStatement updateStmt=con.prepareStatement("update Cursa set locuri_disponibile= locuri_disponibile - ? where id_cursa=?"))
            {
                updateStmt.setInt(1,entitate.getNrLocuri());
                updateStmt.setInt(2,entitate.getIdCursa());
                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("No cursa found for update");
                }
                if (rowsAffected > 0) {
                    logger.traceExit();
                }
            }
            preStmt.executeUpdate();
            logger.info("Rezervare added successfully");

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();

    }

    @Override
    public void update(Integer id, Rezervare entitate)
    {
        logger.traceEntry("modifying rezervare with id {} ",id);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Rezervare set id_client=?, id_cursa=?, locuri_disponibile=? where id_rezervare=?"))
        {
            preStmt.setInt(1,entitate.getIdClient());
            preStmt.setInt(2,entitate.getIdCursa());
            preStmt.setInt(3,entitate.getNrLocuri());
            preStmt.setInt(4,id);
            preStmt.executeUpdate();
            logger.info("Rezervare modified successfully");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public void remove(Integer id)
    {
        logger.traceEntry("removing rezervare with id {} ",id);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Rezervare where id_rezervare=?"))
        {
            preStmt.setInt(1,id);
            preStmt.executeUpdate();
            logger.info("Rezervare removed successfully");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
    }

    @Override
    public Rezervare findOne(Integer integer)
    {
        logger.traceEntry("finding rezervare with id {} ", integer);
        Connection con = dbUtils.getConnection();
        Rezervare rezervare = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Rezervare where id_rezervare=?")) {
            preStmt.setInt(1, integer);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id_rezervare");
                    int id_client = result.getInt("id_client");
                    int id_cursa = result.getInt("id_cursa");
                    int locuri_rezervate = result.getInt("locuri_rezervate");
                    rezervare = new Rezervare(id);
                    Client client = clientDbRepository.findOne(id_client);
                    Cursa cursa = cursaDbRepository.findOne(id_cursa);
                    rezervare.setClient(client);
                    rezervare.setCursa(cursa);
                    rezervare.setNrLocuri(locuri_rezervate);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit(rezervare);
        return rezervare;

    }

    @Override
    public Iterable<Rezervare> getAll()
    {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Collection<Rezervare> rezervari = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Rezervare")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_rezervare");
                    int id_client = result.getInt("id_client");
                    int id_cursa = result.getInt("id_cursa");
                    int locuri_rezervate = result.getInt("locuri_rezervate");
                    Rezervare rezervare = new Rezervare(id);
                    Client client = clientDbRepository.findOne(id_client);
                    Cursa cursa = cursaDbRepository.findOne(id_cursa);
                    rezervare.setClient(client);
                    rezervare.setCursa(cursa);
                    rezervare.setNrLocuri(locuri_rezervate);
                    rezervari.add(rezervare);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit(rezervari);
        return rezervari;
    }

    @Override
    public void setAll(Iterable<Rezervare> entitati) {

    }


    public void updateLocuriDisponibile(int idCursa, int nrLocuri)
    {
        logger.traceEntry("updating locuri disponibile for cursa with id {} ",idCursa);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Cursa set locuri_disponibile=? where id_cursa=?"))
        {

            preStmt.setInt(1,nrLocuri);
            preStmt.setInt(2,idCursa);
            preStmt.executeUpdate();
            logger.info("Locuri disponibile updated successfully");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();
    }


}
