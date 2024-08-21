package Repository;

import Domeniu.Utilizator;
import Repository.Interface.IUtilizatorRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

//public class UtilizatorDBRepository implements IUtilizatorRepository
//{
//
//    private JdbcUtils dbUtils;
//    private static final Logger logger= LogManager.getLogger();
//
//    public UtilizatorDBRepository(Properties props) {
//        logger.info("Initializing UtilizatorDBRepository with properties: {} ", props);
//        dbUtils = new JdbcUtils(props);
//    }
//
//    @Override
//    public void add(Utilizator entitate)
//    {
//        logger.traceEntry("saving user {} ",entitate);
//        Connection con=dbUtils.getConnection();
//
//        try(PreparedStatement preStmt=con.prepareStatement("insert into Utilizator (username, password) values (?,?)"))
//        {
//            preStmt.setString(1,entitate.getUsername());
//            preStmt.setString(2,entitate.getPassword());
//
//            preStmt.executeUpdate();
//            logger.info("User added successfully");
//        } catch (SQLException e) {
//            logger.error(e);
//            System.out.println("Error DB "+e);
//        }
//        logger.traceExit();
//
//    }
//
//    @Override
//    public void update(Integer id, Utilizator entitate)
//    {
//        logger.traceEntry("modifying user with id {} ",id);
//        Connection con=dbUtils.getConnection();
//        try(PreparedStatement preStmt=con.prepareStatement("update Utilizator set username=?, password=? where id_utilizator=?"))
//        {
//            preStmt.setString(1,entitate.getUsername());
//            preStmt.setString(2,entitate.getPassword());
//            preStmt.setInt(3,id);
//
//            preStmt.executeUpdate();
//            logger.info("User modified successfully");
//        } catch (SQLException e) {
//            logger.error(e);
//            System.out.println("Error DB "+e);
//        }
//        logger.traceExit();
//
//    }
//
//    @Override
//    public void remove(Integer id)
//    {
//        logger.traceEntry("removing user with id {} ",id);
//        Connection con=dbUtils.getConnection();
//        try(PreparedStatement preStmt=con.prepareStatement("delete from Client where id_utilizator=?"))
//        {
//            preStmt.setInt(1,id);
//            preStmt.executeUpdate();
//            logger.info("User removed successfully");
//        } catch (SQLException e) {
//            logger.error(e);
//            System.out.println("Error DB "+e);
//        }
//        logger.traceExit();
//
//    }
//
//    @Override
//    public Utilizator findOne(Integer id)
//    {
//        logger.traceEntry("finding user with id {} ",id);
//        Connection con = dbUtils.getConnection();
//        Utilizator utilizator = null;
//        try (PreparedStatement preStmt = con.prepareStatement("select * from Utilizator where id_utilizator=?")) {
//            preStmt.setInt(1, id);
//            try (ResultSet result = preStmt.executeQuery()) {
//                while (result.next()) {
//                    String username = result.getString("username");
//                    String password = result.getString("password");
//                    utilizator = new Utilizator(id);
//                    utilizator.setUsername(username);
//                    utilizator.setPassword(password);
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//        } catch (SQLException e) {
//            logger.error(e);
//            System.out.println("Error DB "+e);
//        }
//        logger.traceExit(utilizator);
//        return utilizator;
//    }
//
//    public Utilizator findByUsername(String username){
//        logger.traceEntry("finding user with username {} ",username);
//        Connection con = dbUtils.getConnection();
//        Utilizator utilizator = null;
//        try (PreparedStatement preStmt = con.prepareStatement("select * from Utilizator where username=?")) {
//            preStmt.setString(1, username);
//            try (ResultSet result = preStmt.executeQuery()) {
//                while (result.next()) {
//                    int id = result.getInt("id_utilizator");
//                    String password = result.getString("password");
//                    utilizator = new Utilizator(id);
//                    utilizator.setUsername(username);
//                    utilizator.setPassword(password);
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//        } catch (SQLException e) {
//            logger.error(e);
//            System.out.println("Error DB "+e);
//        }
//        logger.traceExit(utilizator);
//        return utilizator;
//    }
//
//    @Override
//    public Iterable<Utilizator> getAll()
//    {
//        logger.traceEntry("finding all users");
//        Connection con = dbUtils.getConnection();
//        Collection<Utilizator> utilizators = new ArrayList<>();
//        try (PreparedStatement preStmt = con.prepareStatement("select * from Utilizator")) {
//            try (ResultSet result = preStmt.executeQuery()) {
//                while (result.next()) {
//                    int id = result.getInt("id_utilizator");
//                    String username = result.getString("username");
//                    String password = result.getString("password");
//                    Utilizator utilizator = new Utilizator(id);
//                    utilizators.add(utilizator);
//                    utilizator.setUsername(username);
//                    utilizator.setPassword(password);
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//        } catch (SQLException e) {
//            logger.error(e);
//            System.out.println("Error DB "+e);
//        }
//        logger.traceExit(utilizators);
//        return utilizators;
//    }
//
//    @Override
//    public void setAll(Iterable<Utilizator> entitati) {
//        logger.traceEntry("setting all users");
//        Connection con = dbUtils.getConnection();
//        try (PreparedStatement preStmt = con.prepareStatement("delete from Utilizator")) {
//            preStmt.executeUpdate();
//        } catch (SQLException e) {
//            logger.error(e);
//            System.out.println("Error DB "+e);
//        }
//        for (Utilizator utilizator : entitati) {
//            add(utilizator);
//        }
//        logger.traceExit();
//    }
//
//
//}

public class UtilizatorDBRepository implements IUtilizatorRepository
{


    @Override
    public void add(Utilizator entitate) {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(entitate));
    }

    @Override
    public void update(Integer integer, Utilizator entitate) {
    }

    @Override
    public void remove(Integer integer) {

    }

    @Override
    public Utilizator findOne(Integer integer) {
        try (Session session = HibernateUtils.getSessionFactory().openSession())
        {
            return session.createSelectionQuery("from Utilizator where id =: id_utilizator", Utilizator.class)
                    .setParameter("id_utilizator", integer)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public Iterable<Utilizator> getAll() {
        try(Session session = HibernateUtils.getSessionFactory().openSession())
        {
            return session.createQuery("from Utilizator ", Utilizator.class).getResultList();
        }
    }

    @Override
    public void setAll(Iterable<Utilizator> entitati) {

    }

    @Override
    public Utilizator findByUsername(String username) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Utilizator where username = :username", Utilizator.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }
}
