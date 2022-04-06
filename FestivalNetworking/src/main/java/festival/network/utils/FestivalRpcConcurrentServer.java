package festival.network.utils;

import festival.network.FestivalClientRpcReflectionWorker;
import festival.services.IFestivalServices;

import java.net.Socket;

public class FestivalRpcConcurrentServer extends AbsConcurrentServer{

    private IFestivalServices travelServices;

    public FestivalRpcConcurrentServer(int port, IFestivalServices travelServices) {
        super(port);
        this.travelServices = travelServices;
        System.out.println("Festival - FestivalRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        FestivalClientRpcReflectionWorker worker = new FestivalClientRpcReflectionWorker(travelServices, client);
        Thread tw = new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }

}
