import java.util.concurrent.Semaphore;

public class CustomerCounter { //Tæller 1 op for hver færdiggjort kunde.
    private static CustomerCounter instance;
    private long customersFinished = 0;
    private Semaphore gatekeeper; //Bruges til at sikre, at kun �n tr�d f�r adgang til t�llerens ressourcer.

    private CustomerCounter() { 
        gatekeeper = new Semaphore(1); //Semaphore instantieres med kun �n permit, s� kun �n tr�d kan f� adgang ad gangen. Fungerer som mutex lock.
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
            valueToReturn = customersFinished; //Værdien i customersFinished skal kopieres til en anden variabel, da et 'return statement' ikke ville frigøre Semaphore 'gatekeeper'...
            gatekeeper.release();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return valueToReturn;
    }
}