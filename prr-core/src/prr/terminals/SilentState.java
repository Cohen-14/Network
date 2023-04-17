package prr.terminals;

import java.io.Serializable;

import prr.exceptions.AlreadySilentException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;

public class SilentState extends Terminal.State implements Serializable{
    
    SilentState(Terminal terminal){
        terminal.super();
    }

    @Override
    public void turnOFF(){
        setState(new OffState(getTerminal()));
    }

    @Override
    public void turnOnIdle(){
        setState(new IdleState(getTerminal()));
        getTerminal().notifyObservers("S2I", getTerminal().getTerminalId());
    }

    @Override
    public void turnOnSilent() throws AlreadySilentException{
        throw new AlreadySilentException();
    }

    @Override
    public void callEnded(){}

    @Override
    public void callStarted(Terminal receiver)throws DestinationIsSilentException, DestinationIsBusyException, DestinationIsOffException{
        receiver.callReceived(getTerminal());
        setState(new BusyState(getTerminal()));
    }

    @Override
    public void callReceived(Terminal sender) throws DestinationIsSilentException{
        if (sender.getClient().getNotification().equals("YES")){
            getTerminal().registerObserver(sender.getClient());
        }
        throw new DestinationIsSilentException(getTerminal().getTerminalId());
    }

    @Override
    public String status(){
        return "SILENT";
    }
}
