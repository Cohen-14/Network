package prr.terminals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import prr.clients.Clients;
import prr.communications.Communications;
import prr.communications.Text;
import prr.exceptions.AlreadyOffException;
import prr.exceptions.AlreadyOnException;
import prr.exceptions.AlreadySilentException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.InvalidCommunicationException;
import prr.exceptions.UnsupportedAtDestinationException;
import prr.exceptions.UnsupportedAtOriginException;
import prr.notifications.Observer;
import prr.notifications.Subject;
import prr.Network;

// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable, Subject/* FIXME maybe addd more interfaces */{

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

        private String _terminalID;
        private Clients _client;
        private State _state;
        private Map<String,Terminal> _friends = new TreeMap<>();
        private Map<Integer, Communications> _commsSent = new TreeMap<>();
        private Map<Integer, Communications> _commsReceived = new TreeMap<>();
        private ArrayList<Observer> _observers = new ArrayList<Observer>();
        private Communications _ongoingComm;
        private Network _network;
        private double _paidBalance = 0.0;
        private double _debt = 0.0;
        private boolean _hasFriends = false;
        private boolean _madeComms = false;
        private boolean _receivedComms = false;
        private boolean _hasOngoingComm = false;

        public abstract class State implements Serializable{

                public abstract void turnOFF() throws AlreadyOffException;

                public abstract void turnOnSilent() throws AlreadySilentException;

                public abstract void turnOnIdle() throws AlreadyOnException;

                public abstract void callStarted(Terminal receiver)
                throws DestinationIsBusyException, DestinationIsOffException, DestinationIsSilentException;

                public abstract void callReceived(Terminal sender) 
                throws DestinationIsBusyException, DestinationIsOffException, DestinationIsSilentException;

                public abstract void callEnded();

                public abstract String status();

                protected void setState(State newState){
                        _state = newState;
                }

                protected Terminal getTerminal(){
                        return Terminal.this;
                }
        }

        public Terminal(Network network,String terminalID, Clients client){
                _network = network;
                _terminalID = terminalID;
                _client = client;
                _state = new IdleState(this);
        }

        public void turnOn() throws AlreadyOnException{_state.turnOnIdle();}
        public void turnOFF()throws AlreadyOffException{_state.turnOFF();}
        public void silence()throws AlreadySilentException{_state.turnOnSilent();}

        public void callStarted(Terminal receiver) throws DestinationIsBusyException, DestinationIsOffException,
        DestinationIsSilentException{_state.callStarted(receiver);}

        public void callReceived(Terminal sender)throws DestinationIsBusyException, DestinationIsOffException, 
        DestinationIsSilentException{
                _state.callReceived(sender);
        }

        public void callEnded(){_state.callEnded();}

        public Network getNetwork(){return _network;}
        
        public String getTerminalId(){return _terminalID;}
        public Clients getClient(){return _client;}
        public Communications getCurrentComm(){return _ongoingComm;}
        public String status(){return _state.status();}
        public long getPayments(){return (long)_paidBalance;}
        public long getDebts(){return (long)_debt;}
        public boolean getHasOngoingComm(){return _hasOngoingComm;}
        public void hasOngoingComm(){_hasOngoingComm = true;}

        public boolean madeComms(){
                if (_commsSent.size() > 0){
                        _madeComms = true;
                        return _madeComms;
                }
                _madeComms = false;
                return _madeComms;
        }

        public boolean receivedComms(){
                if (_commsReceived.size() > 0){
                        _receivedComms = true;
                        return _receivedComms;
                }
                _receivedComms = false;
                return _receivedComms;
        }

        public Collection<Communications> getCommsSent(){
                return _commsSent.values();
        }

        public Collection<Communications> getCommsReceived(){
                return _commsReceived.values();
        }

        public Communications getComm(int commKey) throws InvalidCommunicationException{
                if (!_commsSent.containsKey(commKey)){
                        throw new InvalidCommunicationException();
                }
                return _commsSent.get(commKey);
        }

        public Communications getOngoingComm(){
                return _ongoingComm;
        }

        public void setOngoingComm(Communications comm){
                hasOngoingComm();
                _ongoingComm = comm;
        }

        public boolean positiveBalance(){return _paidBalance > _debt;}
        
        public boolean beenActive(){
                return madeComms()||receivedComms();
        }
        
        public void setStatus(String status){
                if (status.equals("IDLE")){
                        _state = new IdleState(this);
                }
                if (status.equals("OFF")){
                        _state = new OffState(this);
                }
                if(status.equals("SILENCE")){
                        _state = new SilentState(this);
                }
        }

        public void addFriends(List<Terminal> terminals){
                if (terminals.size() == 1){
                    //_friends.add(terminals.get(0));
                    _friends.put(terminals.get(0).getTerminalId(), terminals.get(0));
                }
                else{
                        for (Terminal x :terminals){
                        _friends.put(x.getTerminalId(), x);
                    }
                }
                _hasFriends = true;
        }

        public void addFriend(Terminal terminal){
                if (!_friends.containsKey(terminal.getTerminalId())){
                        _friends.put(terminal.getTerminalId(), terminal);
                }
        }

        public void removeFriend(Terminal terminal){
                if (_friends.containsKey(terminal.getTerminalId())){
                        _friends.remove(terminal.getTerminalId(), terminal);
                }
        }
        
        public String printFriends(){
                List<String> friendsId = new ArrayList<String>();
                for (Terminal e : _friends.values()){
                    friendsId.add(e.getTerminalId());
                }
                return friendsId.stream().map(Object::toString).collect(Collectors.joining(","));
        }

        public boolean areFriends(Terminal terminal){
                return _friends.containsKey(terminal.getTerminalId());
        }

        public boolean isSender(Communications comm){
                return comm.getSender().getTerminalId().equals(getTerminalId());
        }

        public boolean isReceiver(Communications comm){
                return comm.getReceiver().getTerminalId().equals(getTerminalId());
        }

        @Override
        public String toString(){
                if (_hasFriends){
                        return _terminalID + "|" + _client.getClientKey() + "|" + status() + "|" + (int)_paidBalance + "|" + (int)_debt + "|" + printFriends();
                }
                return _terminalID + "|" + _client.getClientKey() + "|" + status() + "|" + (int)_paidBalance + "|" + (int)_debt;
        }

        /**
         * Checks if this terminal can end the current interactive communication.
         *
         * @return true if this terminal is busy (i.e., it has an active interactive communication) and
         *          it was the originator of this communication.
         **/
        public boolean canEndCurrentCommunication() {
                if (getHasOngoingComm()){
                        if(status().equals("BUSY") && isSender(getCurrentComm())){
                                return true;
                        }
                }
                return false;
        }

        /**
         * Checks if this terminal can start a new communication.
         *
         * @return true if this terminal is neither off neither busy, false otherwise.
         **/
        public boolean canStartCommunication(){
                if (status().equals("OFF") || status().equals("BUSY")){
                        return false;
                }
                return true;
        }

        public abstract boolean canMakeVideo();

        public void addCommSent(Communications commm){
                _commsSent.put(getNetwork().getCommCounter(), commm);
                _madeComms = true;
        }

        public void addCommReceived(Communications comm){
                _commsReceived.put(getNetwork().getCommCounter(), comm);
                _receivedComms = true;
        }

        public void sendText(Terminal receiver, String text) throws DestinationIsOffException{
                if(canStartCommunication()){
                        if(receiver.status().equals("OFF")){
                                throw new DestinationIsOffException(receiver.getTerminalId());
                        }
                        Communications comm = new Text(getNetwork().getCommCounter(),this, receiver, text);
                        addCommSent(comm);
                        comm.cost();
                        _debt += comm.getPrice(); 
                        getClient().sendText(comm.getPrice());
                        receiver.addCommReceived(comm);
                        getNetwork().incrementCommCounter();
                }
        }

        public abstract void startInteractiveComm(Terminal receiver, String type)throws UnsupportedAtOriginException, UnsupportedAtDestinationException,
        DestinationIsBusyException,DestinationIsOffException, DestinationIsSilentException;

        public long endCurrentCommunication(int minutes){
                callEnded();
                getCurrentComm().getReceiver().callEnded();
                getCurrentComm().setDuration(minutes);
                getCurrentComm().finished();
                getCurrentComm().cost();
                double price = getCurrentComm().getPrice();
                _debt += price;
                switch(getCurrentComm().commType()){
                        case "VIDEO" -> getClient().endVideoCall(price);
                        case "VOICE" -> getClient().endCall(price);
                }
                _hasOngoingComm = false;
                return (long)price;
        }

        public void makePayment(Communications comm) throws InvalidCommunicationException{
                if (isSender(comm) && !comm.getPaidStatus() && comm.getStatus().equals("FINISHED")){
                        _paidBalance += comm.getPrice();
                        _debt -= comm.getPrice();
                        comm.paid();
                }
                throw new InvalidCommunicationException();
        }

        public void registerObserver(Observer o){ 
                if (!_observers.contains(o)){
                        _observers.add(o);
                }
        }

        public void removeObserver(Observer o){
                int i = _observers.indexOf(o);
                if (i>= 0){ _observers.remove(i);}
        }

        public void assertObservers(){
                for (Observer o : _observers){
                        Clients client = (Clients) o;
                        if (client.getNotification().equals("NO")){
                                removeObserver(o);
                        }
                }
        }

        public void notifyObservers(String newState, String terminalId){
                assertObservers();
                for (Observer observer : _observers){
                        observer.update(newState, terminalId);
                }
        }
}
