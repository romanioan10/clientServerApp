//import Domeniu.Cursa;
//import Domeniu.Rezervare;
//import Domeniu.Utilizator;
//import Service.AppException;
//import Service.IObserver;
//import Service.IService;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.stage.Stage;
//
//import java.util.Collection;
//
//public class ControllerUtils
//{
//    private static IService service;
//    private static Stage stage;
//
//    private Utilizator currentUser;
//
//    private static ObserverComponent instance;
//
//    private HomeController homeController;
//
//    public class ObserverComponent implements IObserver
//    {
//        @Override
//        public void updateCurse(Collection<Cursa> curse)
//        {
//            System.out.println("Update received");
//            homeController.loadCurse();
//        }
//
//        @Override
//        public void updateRezervari(Collection<Rezervare> rezervari) throws AppException {
//            System.out.println("Update received");
//            homeController.loadRezervari();
//        }
//    }
//
//    public ObserverComponent getObserverComponent()
//    {
//        if (instance == null)
//        {
//            instance = new ObserverComponent();
//        }
//        return instance;
//    }
//
//    public void setHomeController(HomeController homeController) {
//        this.homeController = homeController;
//    }
//
//
//    public static void setService(IService service) {
//        ControllerUtils.service = service;
//    }
//
//
//
//    public static IService getService() {
//        return service;
//    }
//
//    public static void setStage(Stage stage) {
//        ControllerUtils.stage = stage;
//    }
//
//    public static void setScene(Scene scene) {
//        double height = stage.getHeight();
//        double width = stage.getWidth();
//        stage.setScene(scene);
//        stage.setHeight(height);
//        stage.setWidth(width);
//    }
//
//
//    public static void createAlert(Exception e){
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setContentText(e.getMessage());
//        alert.show();
//
//    }
//
//
//
//    public void setCurrentUser(Utilizator user) {
//        currentUser = user;
//    }
//
//    public Utilizator getCurrentUser() {
//        return currentUser;
//    }
//
//}
