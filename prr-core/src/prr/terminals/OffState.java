package prr.terminals;

import prr.exceptions.*;
import java.io.Serializable;

public class OffState extends Terminal.State implements Serializable{

    OffState(Terminal terminal){
        terminal.super();
    }

    @Override
    public void turnOFF() throws AlreadyOffException{
        throw new AlreadyOffException();
    }

    @Override
    public void turnOnIdle(){
        setState(new IdleState(getTerminal()));
        getTerminal().notifyObservers("O2I", getTerminal().getTerminalId());
    }

    @Override
    public void turnOnSilent(){
        setState(new SilentState(getTerminal()));
        getTerminal().notifyObservers("O2S", getTerminal().getTerminalId());
    }

    @Override
    public void callStarted(Terminal receiver){}

    @Override
    public void callReceived(Terminal sender) throws DestinationIsOffException {
        if (sender.getClient().getNotification().equals("YES")){
            getTerminal().registerObserver(sender.getClient());
        }
        throw new DestinationIsOffException(getTerminal().getTerminalId());
    }

    @Override
    public void callEnded(){}

    @Override
    public String status(){
        return "OFF";
    }

}
