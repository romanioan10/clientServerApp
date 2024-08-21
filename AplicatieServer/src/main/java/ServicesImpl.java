import Domeniu.Client;
import Domeniu.Cursa;
import Domeniu.Rezervare;
import Domeniu.Utilizator;
import Repository.Interface.IClientRepository;
import Repository.Interface.ICursaRepository;
import Repository.Interface.IRezervareRepository;
import Repository.Interface.IUtilizatorRepository;
import Service.AppException;
import Service.IObserver;
import Service.IService;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServicesImpl implements IService {

    private IUtilizatorRepository userRepository;
    private IRezervareRepository rezervareRepository;
    private IClientRepository clientRepository;
    private ICursaRepository cursaRepository;
    private Map<Integer, IObserver> loggedClients;

    public ServicesImpl(IUtilizatorRepository uRepo, IRezervareRepository mRepo, ICursaRepository cursaRepository, IClientRepository clientRepository) {

        userRepository= uRepo;
        rezervareRepository=mRepo;
        this.clientRepository = clientRepository;
        this.cursaRepository = cursaRepository;
        loggedClients=new ConcurrentHashMap<>();;
    }


    public synchronized void login(Utilizator user, IObserver client) throws AppException {
        Utilizator userR = userRepository.findByUsername(user.getUsername());
        if (userR != null) {
            if (userR.getPassword().equals(user.getPassword())) {
                if (loggedClients.get(userR.getId()) != null)
                    throw new AppException("User already logged in.");
                else
                    loggedClients.put(userR.getId(), client);
            }
            else
                throw new AppException("Wrong user or password.");
        }else
            throw new AppException("Authentication failed.");

    }


    @Override
    public synchronized Client getByName(String nume)
    {
        List<Client> clienti = (List<Client>) clientRepository.getAll();
        for (Client client : clienti) {
            if (client.getNume().equals(nume)) {
                return client;
            }
        }
        return null;
    }

    @Override
    public synchronized List<Cursa> cautareCurseDupaDestinatie(String destinatie) throws AppException
    {
        List<Cursa> curse = new ArrayList<>();
        Iterable<Cursa> allCurse = cursaRepository.getAll();
        for(Cursa cursa : allCurse)
        {
            if(cursa.getDestinatie().equals(destinatie))
            {
                curse.add(cursa);
            }
        }
        return curse;

    }

    @Override
    public synchronized List<String> cautareCurse(String destinatie, String data, String ora)
    {
        boolean gasit = false;
        Iterable<Cursa> curse = cursaRepository.getAll();
        for(Cursa cursa : curse)
        {
            if(cursa.getDestinatie().equals(destinatie) && cursa.getData().equals(data) && cursa.getOra().equals(ora))
            {
                gasit = true;
            }
        }
        if(!gasit)
        {
            System.out.println("Cursa nu a fost gasita!");
        }

        List<String> locuriCursa = new ArrayList<>();
        for(int i=0;i<18;i++)
        {
            locuriCursa.add("-");
        }

        Iterable<Rezervare> rezervari = rezervareRepository.getAll();
        int j = 0;

        for(Rezervare rezervare : rezervari)
        {
            if(rezervare.getCursa().getDestinatie().equals(destinatie) && rezervare.getCursa().getData().equals(data) && rezervare.getCursa().getOra().equals(ora))
            {

                for(int i=j ;i<rezervare.getNrLocuri();i++)
                {
                    locuriCursa.set(i, ": " + rezervare.getClient().getNume());
                }
                j += rezervare.getNrLocuri();
            }

        }

        return locuriCursa;

    }

    @Override
    public synchronized void addRezervare(Rezervare rezervare) throws AppException {

        rezervareRepository.add(rezervare);
        notifyUpdatedRezervari();
        notifyUpdatedCurse();

    }

    private final int defaultThreadsNo=5;
    private void notifyUpdatedCurse() throws AppException
    {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (IObserver client : loggedClients.values()) {
            if (client != null) {
                executor.execute(() -> {
                    try {
                        client.updateCurse(getAllCurse());
                    } catch (AppException e) {
                        System.err.println("Error notifying user " + e);
                    }
                });
            }
        }
        executor.shutdown();
    }

    private void notifyUpdatedRezervari() throws AppException
    {

        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (IObserver client : loggedClients.values()) {
            if (client != null) {
                executor.execute(() -> {
                    try {
                        client.updateRezervari(getAllRezervari());
                    } catch (AppException e) {
                        System.err.println("Error notifying user " + e);
                    }
                });
            }
        }
        executor.shutdown();



    }

    @Override
    public synchronized Collection<Rezervare> getAllRezervari()
    {
        return (Collection<Rezervare>) rezervareRepository.getAll();
    }

    @Override
    public synchronized Collection<Cursa> getAllCurse()
    {
        return (Collection<Cursa>) cursaRepository.getAll();
    }


    public synchronized void logout(Utilizator user) throws AppException
    {
        Utilizator userR = userRepository.findByUsername(user.getUsername());
        IObserver localClient=loggedClients.remove(userR.getId());
        if (localClient==null)
            throw new AppException("User "+user.getId()+" is not logged in.");
    }


}
