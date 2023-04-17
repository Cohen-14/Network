package prr.notifications;

import java.io.Serializable;

public class Notifications implements DisplayElement, Serializable{

    private String _type;
    private String _terminalId;

    public Notifications(String type, String terminalId){
        _type = type;
        _terminalId = terminalId;
    }

    public String display(){
        return _type + "|" + _terminalId;
    }
}
