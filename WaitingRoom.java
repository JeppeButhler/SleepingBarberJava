import java.util.concurrent.*;

public class WaitingRoom {
    private static WaitingRoom instance;
    private final int numberOfChairs = 20;
    private ArrayBlockingQueue<Customer> customerQueue;
    private Semaphore gatekeeper; //Bruges til at sikre, at kun én tråd får adgang til et objekt

    private WaitingRoom() {
        customerQueue = new ArrayBlockingQueue<>(numberOfChairs);
        gatekeeper = new Semaphore(1); //Semaphore instantieres med kun én permit, så kun én tråd kan få adgang ad gangen. Fungerer som mutex lock.
    }

    public void seatCustomer(Customer c) {
        try {
            gatekeeper.acquire(); //Sikrer sig at der ikke tilfÃ¸jes flere kunder pÃ¥ samme tid.
            if (customerQueue.size() < numberOfChairs) {
                customerQueue.add(c);
                System.out.println("Customer " + c.getID() + " has taken a seat");
            } else {
                System.out.println("There wasn't a seat for customer " + c.getID());
            }
            gatekeeper.release(); //Frigiver retten til at tilføje kunder til køen.
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }


    public Customer unseatCustomer() {
        Customer nextCustomer = customerQueue.poll(); //Tager den nÃ¦ste i kÃ¸en (FIFO) og fjerner vedkommende fra kÃ¸en.
        if(nextCustomer != null) { //Hvis kunden er null, skal den ikke udskrive informationer omkring den (kÃ¸en er tom)
            System.out.println("It's customer " + nextCustomer.getID() + "'s turn");
            nextCustomer.release(); //Frigiver kunden, sÃ¥ frisÃ¸ren kan overtage den og arbejde pÃ¥ den.
        }
        
        return nextCustomer;
    }

    public boolean anyoneThere() {
        return (customerQueue.size() == 0);
    }

    public static WaitingRoom getInstance() {
        if (instance == null)
            instance = new WaitingRoom();
        return instance;
    }
}