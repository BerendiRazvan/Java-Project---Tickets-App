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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Collection;
import java.util.List;

public class FestivalClientRpcReflectionWorker implements Runnable, IFestivalObserver {

    private IFestivalServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public FestivalClientRpcReflectionWorker(IFestivalServices server, Socket connection){
        System.out.println("Create FestivalClientRpcReflectionWorker");
        this.server = server;
        this.connection = connection;

        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        while(connected){
            try{
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if(response != null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response = null;
        String handlerName = "handle" + (request).type();
        System.out.println("HandlerName " + handlerName);

        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }


    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }





    private Response handleLOGIN(Request request){
        System.out.println("Login request ..." + request.type());
        Employee employee = (Employee)request.data();
        try{
            server.login(employee, this);
            return okResponse;
        } catch (FestivalException e) {
            connected = false;
            return  new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request){
        System.out.println("LogOUT request ...");
        Employee employee = (Employee) request.data();
        try {
            server.logout(employee, this);
            connected = false;
            return okResponse;
        } catch (FestivalException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ALL_ARTISTS(Request request){
        System.out.println("Getting all artists ...");
        try{
            List<String> flights = server.getAllArtists();
            return new Response.Builder().type(ResponseType.ARTISTS).data(flights).build();
        } catch (FestivalException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ALL_ARTISTS_SHOWS(Request request){
        System.out.println("Getting all shows for an artist ...");
        String artistName = (String) request.data();
        try{
            List<Show> flights = server.getArtistShows(artistName);
            return new Response.Builder().type(ResponseType.SHOWS).data(flights).build();
        } catch (FestivalException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ALL_ARTISTS_SHOWS_DAY(Request request){
        System.out.println("Getting all shows for an artist in a day ...");
        ShowDTO showDTO = (ShowDTO) request.data();
        try{
            List<Show> flights = server.getArtistShowsInADay(showDTO.getArtistName(),showDTO.getShowDay());
            return new Response.Builder().type(ResponseType.SHOWS).data(flights).build();
        } catch (FestivalException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleSELL_TICKET(Request request){
        System.out.println("Selling tickets ...");
        TicketDTO ticketDTO = (TicketDTO) request.data();
        try{
            server.sellTicket(ticketDTO.getName(),ticketDTO.getTickets(),ticketDTO.getShow());
            return okResponse;
        } catch (FestivalException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    @Override
    public void soldTicket(Ticket ticket) throws FestivalException {
        Response resp = new Response.Builder().type(ResponseType.TICKET_SOLD).data(ticket).build();
        System.out.println("Ticket added " + ticket);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
