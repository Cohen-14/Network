package prr.terminals;

import java.io.Serializable;

import prr.exceptions.DestinationIsBusyException;

public class BusyState extends Terminal.State implements Serializable{

    BusyState(Terminal terminal){
        terminal.super();
    }

    @Override
    public void turnOFF(){}

    @Override
    public void turnOnSilent(){}

    @Override
    public void turnOnIdle(){}

    @Override
    public void callEnded(){
        setState(new IdleState(getTerminal()));
        getTerminal().notifyObservers("B2I", getTerminal().getTerminalId());
    }

    @Override
    public void callStarted(Terminal receiver){}

    @Override
    public void callReceived(Terminal sender) throws DestinationIsBusyException{
        if (sender.getClient().getNotification().equals("YES")){
            getTerminal().registerObserver(sender.getClient());
        }
        throw new DestinationIsBusyException(getTerminal().getTerminalId());
    }

    @Override
    public String status(){
        return "BUSY";
    }
}
