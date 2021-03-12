import java.util.concurrent.Semaphore;

public class Customer {
    private static int id_s = 1;
    private int id;
    private Semaphore gatekeeper; //Bruges til at sikre, at kun én tråd får adgang til et stykke kode ad gangen.

    public Customer() throws InterruptedException {
        id = id_s++;
        gatekeeper = new Semaphore(1); //Semaphore instantieres med kun én permit, så kun én tråd kan få adgang ad gangen. Fungerer som mutex lock.
        acquire();
    }

    public void acquire() throws InterruptedException {
        gatekeeper.acquire(); //Bruges til at blokere adgangen til et kundeobjekts ressourcer til én tråd.
    }

    public void release() {
        gatekeeper.release(); //Frigiver adgangen til en kunde.
    }

    public void shave()
    {
        System.out.println(id + " has been shaved... :)");
    }

    public int getID() {
        return id;
    }
}