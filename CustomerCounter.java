import java.util.concurrent.Semaphore;

public class CustomerCounter { //TÃ¦ller 1 op for hver fÃ¦rdiggjort kunde.
    private static CustomerCounter instance;
    private long customersFinished = 0;
    private Semaphore gatekeeper; //Bruges til at sikre, at kun én tråd får adgang til tællerens ressourcer.

    private CustomerCounter() { 
        gatekeeper = new Semaphore(1); //Semaphore instantieres med kun én permit, så kun én tråd kan få adgang ad gangen. Fungerer som mutex lock.
    }

    public static CustomerCounter getInstance() {
        if (instance == null)
            instance = new CustomerCounter();
        return instance;
    }

    public void increment() {
        try {
            gatekeeper.acquire();
            ++customersFinished;
            System.out.println("+1:" + customersFinished);
            gatekeeper.release();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public long customerCount(){
        long valueToReturn = -1;
        try {
            gatekeeper.acquire();
            valueToReturn = customersFinished; //VÃ¦rdien i customersFinished skal kopieres til en anden variabel, da et 'return statement' ikke ville frigÃ¸re Semaphore 'gatekeeper'...
            gatekeeper.release();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return valueToReturn;
    }
}