package prr.terminals;

import java.io.Serializable;

import prr.Network;
import prr.clients.Clients;
import prr.communications.Call;
import prr.communications.Communications;
import prr.communications.Video;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.UnsupportedAtDestinationException;
import prr.notifications.Observer;

public class FancyTerminal extends Terminal implements Serializable{

    private static final long serialVersionUID = 202208091753L;
    
    public FancyTerminal(Network network,String terminalId, Clients client){
        super(network,terminalId, client);
    }

    @Override
    public boolean canMakeVideo(){return true;}

    @Override
    public void startInteractiveComm(Terminal receiver, String type)throws UnsupportedAtDestinationException,
        DestinationIsBusyException,DestinationIsOffException, DestinationIsSilentException{
            if (receiver.getTerminalId().equals(getTerminalId())){
                throw new DestinationIsBusyException(getTerminalId());
            }
            if (type.equals("VIDEO")){
                if (receiver.canMakeVideo()){
                    callStarted(receiver);
                    Communications comm = new Video(getNetwork().getCommCounter(),this, receiver);
                    setOngoingComm(comm);
                    addCommSent(comm);
                    receiver.addCommReceived(comm);
                    getNetwork().incrementCommCounter();
                }
                else{
                    throw new UnsupportedAtDestinationException(receiver.getTerminalId(), type);
                }
            }
            else{
                Communications comm = new Call(getNetwork().getCommCounter(),this, receiver);
                setOngoingComm(comm);
                addCommSent(comm);
                receiver.addCommReceived(comm);
                getNetwork().incrementCommCounter();
            }
    }

    @Override
    public String toString(){
        return "FANCY|" + super.toString();
    }
}
 