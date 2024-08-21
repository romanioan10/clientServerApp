package Repository;

import Domeniu.Cursa;
import Repository.Interface.ICursaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

@Component
public class CursaDBRepository implements ICursaRepository
{
    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();


    public CursaDBRepository(Properties props) {
        logger.info("Initializing CursaDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Cursa entitate)
    {
        logger.traceEntry("saving cursa {} ",entitate);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("insert into Cursa ( destinatie, data_plecare, ora_plecare, locuri_disponibile) values (?,?,?,?)"))
        {
            preStmt.setString(1,entitate.getDestinatie());
            preStmt.setString(2,entitate.getData());
            preStmt.setString(3,entitate.getOra());
            preStmt.setInt(4, 18);
            preStmt.executeUpdate();
            logger.info("Cursa added successfully");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();

    }

    @Override
    public void update(Integer id, Cursa entitate)
    {
        logger.traceEntry("modifying cursa with id {} ",id);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Cursa set destinatie=?, data_plecare=?, ora_plecare=?, locuri_disponibile=? where id_cursa=?"))
        {
            preStmt.setInt(5,id);
            preStmt.setString(1,entitate.getDestinatie());
            preStmt.setString(2,entitate.getData());
            preStmt.setString(3,entitate.getOra());
            preStmt.setInt(4, entitate.getLocuriDisponibile());
            preStmt.executeUpdate();
            logger.info("Cursa modified successfully");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();

    }

    @Override
    public void remove(Integer id)
    {
        logger.traceEntry("removing cursa with id {} ",id);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Cursa where id_cursa=?"))
        {
            preStmt.setInt(1,id);
            preStmt.executeUpdate();
            logger.info("Cursa removed successfully");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit();

    }


    @Override
    public Cursa findOne(Integer id)
    {
        logger.traceEntry("finding cursa with id {} ",id);
        Connection con = dbUtils.getConnection();
        Cursa cursa = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Cursa where id_cursa=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String destinatie = result.getString("destinatie");
                    String data = result.getString("data_plecare");
                    String ora = result.getString("ora_plecare");
                    int locuriDisponibile = result.getInt("locuri_disponibile");

                    cursa = new Cursa(id);
                    cursa.setDestinatie(destinatie);
                    cursa.setData(data);
                    cursa.setOra(ora);
                    cursa.setLocuriDisponibile(locuriDisponibile);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit(cursa);
        return cursa;
    }

    @Override
    public Iterable<Cursa> getAll()
    {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Collection<Cursa> curse = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Cursa")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_cursa");
                    String destinatie = result.getString("destinatie");
                    String data = result.getString("data_plecare");
                    String ora = result.getString("ora_plecare");
                    int locuriDisponibile = result.getInt("locuri_disponibile");

                    Cursa cursa = new Cursa(id);
                    cursa.setDestinatie(destinatie);
                    cursa.setData(data);
                    cursa.setOra(ora);
                    cursa.setLocuriDisponibile(locuriDisponibile);
                    curse.add(cursa);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit(curse);
        return curse;
    }

    @Override
    public void setAll(Iterable<Cursa> entitati) {
        logger.traceEntry("setting all curse");
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Cursa")) {
            preStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        for (Cursa cursa : entitati) {
            add(cursa);
        }
        logger.traceExit();
    }

    @Override
    public Cursa findById(Integer id)
    {
        return findOne(id);

    }

    @Override
    public Cursa findByAll(String destinatie, String data, String ora, int locuri) {
        logger.traceEntry("finding cursa by all {} ", destinatie);
        Connection con = dbUtils.getConnection();
        Cursa cursa = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Cursa where destinatie=? and data_plecare=? and ora_plecare=? and locuri_disponibile=?")) {
            preStmt.setString(1, destinatie);
            preStmt.setString(2, data);
            preStmt.setString(3,ora);
            preStmt.setInt(4, locuri);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_cursa");

                    cursa = new Cursa(id);
                    cursa.setDestinatie(destinatie);
                    cursa.setData(data);
                    cursa.setOra(ora);
                    cursa.setLocuriDisponibile(locuri);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.traceExit(cursa);
        return cursa;
    }
}
