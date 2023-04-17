package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;

public abstract class Communications implements Serializable {

    private int _key = 0;
    private Terminal _sender;
    private Terminal _receiver;
    private String _status = "ONGOING";
    private int _units;
    private int _price;
    private boolean _hasBeenPaid = false;

    public Communications(int key,Terminal sender, Terminal receiver){
        _sender = sender;
        _receiver = receiver;
        _key = key;
    }

    public Communications(int key,Terminal sender, Terminal receiver, String text){
        _sender = sender;
        _receiver = receiver;
        _units = text.length();
        _key = key;
    }

    public int getKey(){return _key;}
    public Terminal getSender(){return _sender;}
    public Terminal getReceiver(){return _receiver;}
    public String getStatus(){return _status;}
    public int getUnits(){return _units;}
    public int getPrice(){return _price;}
    public boolean getPaidStatus(){return _hasBeenPaid;}
    public abstract String commType();

    public void setDuration(int minutes){_units = minutes;}

    public void ongoing(){_status = "ONGOING";}
    public void finished(){_status = "FINISHED";}
    public void setPrice(int value){_price = value;}
    public void paid(){_hasBeenPaid = true;}

    public abstract void cost();

    public String toString(){
        return getKey() + "|" + getSender().getTerminalId() + "|" + getReceiver().getTerminalId() + "|"
        + getUnits() + "|" + getPrice() + "|" + getStatus();
    }
}
