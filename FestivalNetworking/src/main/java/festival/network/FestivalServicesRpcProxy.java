package festival.network;

import festival.model.Employee;
import festival.model.Show;
import festival.model.Ticket;
import festival.network.utils.EmployeeDTO;
import festival.network.utils.ShowDTO;
import festival.network.utils.TicketDTO;
import festival.services.FestivalException;
import festival.services.IFestivalObserver;
import festival.services.IFestivalServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class FestivalServicesRpcProxy implements IFestivalServices {

    private String host;
    private int port;

    private IFestivalObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public FestivalServicesRpcProxy(String host,int port){
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingDeque<Response>();
    }

    private void closeConnection(){
        finished = true;
        try{
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws FestivalException{
        try{
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new FestivalException("Error sending object " + e);
        }
    }

    private Response readResponse() throws FestivalException{
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws FestivalException {
        try{
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response){
        if(response.type() == ResponseType.TICKET_SOLD){
            Ticket ticket = (Ticket) response.data();
            System.out.println("Ticket update for observer");
            try {
                client.soldTicket(ticket);
            } catch (FestivalException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type() == ResponseType.TICKET_SOLD;

    }

    private class ReaderThread implements Runnable{
        public void run(){
            while (!finished){
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if(isUpdate((Response)response)){
                        handleUpdate((Response) response);
                    }else{

                        try{
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }


    @Override
    public void login(Employee employee, IFestivalObserver client) throws FestivalException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(employee).build();
        sendRequest(req);
        Response response = readResponse();
        if( response.type() == ResponseType.OK ){
            this.client = client;
            return;
        }
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            closeConnection();
            throw new FestivalException(err);
        }
    }

    @Override
    public void logout(Employee employee, IFestivalObserver client) throws FestivalException {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(employee).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new FestivalException(err);
        }
    }

    @Override
    public List<String> getAllArtists() throws FestivalException {
        Request req =  new Request.Builder().type(RequestType.GET_ALL_ARTISTS).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            closeConnection();
            throw new FestivalException("Something went wrong");
        }
        List<String> artists = (List<String>) response.data();
        return artists;
    }

    @Override
    public List<Show> getArtistShows(String artistName) throws FestivalException {
        Request req =  new Request.Builder().type(RequestType.GET_ALL_ARTISTS_SHOWS).data(artistName).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            closeConnection();
            throw new FestivalException("Something went wrong");
        }
        List<Show> shows = (List<Show>) response.data();
        return shows;
    }

    @Override
    public List<Show> getArtistShowsInADay(String artistName, LocalDateTime showDay) throws FestivalException {
        ShowDTO showDTO = new ShowDTO(artistName,showDay);
        Request req = new Request.Builder().type(RequestType.GET_ALL_ARTISTS_SHOWS_DAY).data(showDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            closeConnection();
            throw new FestivalException("Something went wrong");
        }
        List<Show> shows = (List<Show>) response.data();
        return shows;
    }

    @Override
    public void sellTicket(String name, int tickets, Show show) throws FestivalException {
        TicketDTO ticketDTO = new TicketDTO(name, tickets, show);
        Request req = new Request.Builder().type(RequestType.SELL_TICKET).data(ticketDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            closeConnection();
            throw new FestivalException(err);
        }

    }



}
