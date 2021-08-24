import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JPHInteractor interactor = new JPHInteractor();
        try {
            //interactor.create("user.json");
            //interactor.update("user.json", 9);
            //interactor.delete(9);
            //System.out.println(interactor.getUsers());
            //System.out.println(interactor.getUser(9));
            //System.out.println(interactor.getUser("Delphine"));
            //interactor.getPostComments(1);
            interactor.printRelevantTasks(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
