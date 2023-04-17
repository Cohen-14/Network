package prr.terminals;

import prr.exceptions.*;
import prr.Network;
import prr.clients.Clients;
import prr.communications.Call;
import prr.communications.Communications;
import prr.notifications.Observer;

import java.io.Serializable;

public class BasicTerminal extends Terminal implements Serializable {

    private static final long serialVersionUID = 202208091753L;
    
    public BasicTerminal(Network network,String terminalId, Clients client){
        super(network,terminalId, client);
    }

    @Override
    public boolean canMakeVideo(){return false;}

    @Override
    public void startInteractiveComm(Terminal receiver, String type) 
    throws UnsupportedAtOriginException, DestinationIsBusyException, DestinationIsOffException, DestinationIsSilentException{
        if (type.equals("VIDEO")){
            throw new UnsupportedAtOriginException(getTerminalId(), type);
        }
        if (receiver.getTerminalId().equals(getTerminalId())){
            throw new DestinationIsBusyException(getTerminalId());
        }
        else{ //maybe a try - catch here
            callStarted(receiver);
            Communications comm = new Call(getNetwork().getCommCounter(),this, receiver);
            setOngoingComm(comm);
            addCommSent(comm);
            receiver.addCommReceived(comm);
            getNetwork().incrementCommCounter();
        }
    }

    @Override
    public String toString(){
        return "BASIC|" + super.toString();
    }
}
