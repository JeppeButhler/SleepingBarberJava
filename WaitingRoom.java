import java.util.concurrent.*;

public class WaitingRoom {
    private static WaitingRoom instance;
    private final int numberOfChairs = 20;
    private ArrayBlockingQueue<Customer> customerQueue;
    private Semaphore gatekeeper; //Bruges til at sikre, at kun �n tr�d f�r adgang til et objekt

    private WaitingRoom() {
        customerQueue = new ArrayBlockingQueue<>(numberOfChairs);
        gatekeeper = new Semaphore(1); //Semaphore instantieres med kun �n permit, s� kun �n tr�d kan f� adgang ad gangen. Fungerer som mutex lock.
    }

    public void seatCustomer(Customer c) {
        try {
            gatekeeper.acquire(); //Sikrer sig at der ikke tilføjes flere kunder på samme tid.
            if (customerQueue.size() < numberOfChairs) {
                customerQueue.add(c);
                System.out.println("Customer " + c.getID() + " has taken a seat");
            } else {
                System.out.println("There wasn't a seat for customer " + c.getID());
            }
            gatekeeper.release(); //Frigiver retten til at tilf�je kunder til k�en.
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }


    public Customer unseatCustomer() {
        Customer nextCustomer = customerQueue.poll(); //Tager den næste i køen (FIFO) og fjerner vedkommende fra køen.
        if(nextCustomer != null) { //Hvis kunden er null, skal den ikke udskrive informationer omkring den (køen er tom)
            System.out.println("It's customer " + nextCustomer.getID() + "'s turn");
            nextCustomer.release(); //Frigiver kunden, så frisøren kan overtage den og arbejde på den.
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