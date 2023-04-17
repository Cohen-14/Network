package prr.clients;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import prr.Plan;
import prr.notifications.Notifications;
import prr.notifications.Observer;
import prr.notifications.Subject;
import prr.BasePlan;
import prr.terminals.Terminal;

public class Clients implements Serializable, Observer{

    private static final long serialVersionUID = 202208091753L;
    
    private Plan _plan;
    private String _clientKey;
    private String _name;
    private int _clientId;
    private Map<String,Terminal> _terminals = new TreeMap<>();
    private ArrayList<Notifications> _notifications = new ArrayList<Notifications>();
    private State _state;
    private double _payments = 0.0;
    private double _debt = 0.0;
    private double _balance = 0.0;
    private boolean _allowNotifications = true;
    private boolean _hasPendingNotifications = false;
    private int _consecutiveVideo = 0;
    private int _consecutiveTexts = 0;

    public abstract class State implements Serializable{

        public abstract void makePayment();

        public abstract void sendText();

        public abstract void endCall();

        public abstract void endVideoCall();

        public abstract String status();

        protected void setState(State newState){
            _state = newState;
        }

        protected Clients getClient(){
            return Clients.this;
        }
    }

    public static class DebtComparator implements Comparator<Clients>, Serializable {

        @Override
        public int compare(Clients c1, Clients c2){
            if (c1.getDebts() == c2.getDebts()){
                return c1.getClientKey().compareToIgnoreCase(c2.getClientKey());
            }
            return (int)c1.getDebts() - (int)c2.getDebts();
        }
    }

    public Clients(String name, String clientKey, int clientId){
        _name = name;
        _clientKey = clientKey;
        _clientId = clientId;
        _state = new NormalState(this);
        _plan = new BasePlan(this);
    }

    public String getClientKey(){return _clientKey;}
    public int getID(){return _clientId;}
    public Plan getPlan(){return _plan;}
    public int getConsTexts(){return _consecutiveTexts;}
    public int getConsVideo(){return _consecutiveVideo;}
    public long getDebts(){return (long)_debt;}
    public long getPayments(){return (long) _payments;}
    public boolean getPendingNotifs(){return _hasPendingNotifications;}

    public String getNotification(){
        if (_allowNotifications){
            return "YES";
        }
        return "NO";
    }

    public boolean getAllowNotif(){
        return _allowNotifications;
    }
    
    public void setConsTexts(int value){_consecutiveTexts = value;}
    public void setConsVideo(int value){_consecutiveVideo = value;}
    public void setPendingNotifications(boolean b){_hasPendingNotifications = b;}


    public double getBalance(){
        _balance = _payments - _debt;
        return _balance;
    }

    public Collection<Terminal> getTerminals(){
        return _terminals.values();
    }

    public void addTerminal(Terminal terminal){
        _terminals.put(terminal.getTerminalId(), terminal);
    }

    public int getTerminlsN(){
        return _terminals.size();
    }

    public String status(){
        return _state.status();
    }

    public void makePayment(double price){
        _state.makePayment();
        _payments += price;
        _debt -= price;
    }

    public void endCall(double price){
        _state.endCall();
        _debt += price;
    }

    public void sendText(double price){
        _consecutiveTexts++;
        _state.sendText();
        _debt += price;
    }

    public void endVideoCall(double price){
        _consecutiveVideo++;
        _state.endVideoCall();
        _debt += price;
    }

    public void update(String newState, String terminalId){
        setPendingNotifications(true);
        Notifications notification = new Notifications(newState, terminalId);
        _notifications.add(notification);
    }
    
    public Collection<String> showNotifications(){
        List<String> notifications = new ArrayList<String>();
        for (Notifications x : _notifications){
            notifications.add(x.display());
        }
        setPendingNotifications(false);
        _notifications.clear();
        return Collections.unmodifiableCollection(notifications);
    }

    public void enableNotifications(){
        _allowNotifications = true;
    }

    public void disableNotifications(){
        _allowNotifications = false;
    }

    @Override
    public String toString(){
        return "CLIENT|" + _clientKey + "|" + _name + "|" + _clientId + "|" + status() + "|" + getNotification()
        + "|" + getTerminlsN() + "|" + (int)_payments + "|" + (int)_debt;
    }
}
