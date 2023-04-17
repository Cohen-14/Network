package prr.terminals;

import java.io.Serializable;

import prr.exceptions.AlreadyOnException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;

public class IdleState extends Terminal.State implements Serializable{

    IdleState(Terminal terminal){
        terminal.super();
    }

    @Override
    public void turnOFF(){
        setState(new OffState(getTerminal()));
    }

    @Override
    public void turnOnIdle() throws AlreadyOnException{
        throw new AlreadyOnException();
    }

    @Override
    public void turnOnSilent(){
        setState(new SilentState(getTerminal()));
    }

    @Override
    public void callStarted(Terminal receiver) throws DestinationIsBusyException, DestinationIsOffException, DestinationIsSilentException{
        receiver.callReceived(getTerminal());
        setState(new BusyState(getTerminal()));
    }

    @Override
    public void callReceived(Terminal sender){
        setState(new BusyState(getTerminal()));
    }

    @Override
    public void callEnded(){}

    @Override
    public String status(){
        return "IDLE";
    }

}
