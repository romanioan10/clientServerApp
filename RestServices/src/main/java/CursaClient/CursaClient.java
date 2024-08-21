package CursaClient;

import Domeniu.Cursa;

import java.util.Collections;

public class CursaClient
{
    public static final String URL = "http://localhost:8080/restcontroller/cursa";

    private org.springframework.web.client.RestTemplate restTemplate;

    public CursaClient() {
        restTemplate = new org.springframework.web.client.RestTemplate();
    }

    private <T> T execute(java.util.concurrent.Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Iterable<Cursa> getAll() {
        return Collections.singleton(execute(() -> restTemplate.getForObject(URL, Cursa.class)));
    }

    public Cursa getById(String id) {
        return execute(() -> restTemplate.getForObject(URL + "/" + id, Cursa.class));
    }

    public Cursa add(Cursa cursa) {
        return execute(() -> restTemplate.postForObject("http://localhost:8080/restcontroller/cursa", cursa, Cursa.class));
    }

    public void update(Cursa cursa) {
        execute(() -> {
            restTemplate.put(URL + "/" + cursa.getId(), cursa);
            return null;
        });
    }

    public void delete(String id) {
        execute(() -> {
            restTemplate.delete(URL + "/" + id);
            return null;
        });
    }


}
