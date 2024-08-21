package start;

import CursaClient.CursaClient;
import Domeniu.Cursa;
import org.springframework.web.client.RestTemplate;


public class StartRestClient
{

   private static final CursaClient cursaClient = new CursaClient();

    public StartRestClient()
    {
    }

    public static void main(String[] args)
    {
        new RestTemplate();

        Cursa cursa = new Cursa("Cluj", "12.12.2020", "12:00");


        try
        {
            show(() -> {
                System.out.println(cursaClient.add(cursa));
            });

            show(() -> {
                Iterable<Cursa> res = cursaClient.getAll();
                for (Cursa c : res)
                {
                    System.out.println(c.getId() + ": " + c.getDestinatie() + " " + c.getData() + " " + c.getOra());
                }
            });
        }
        catch (Exception e)
        {
            System.out.println("Exception ... " + e.getMessage());
        }
    }

private static void show(Runnable task)
    {
        try
        {
            task.run();
        }
        catch (Exception e)
        {
            System.out.println("Service exception" + e);
        }
    }


}
