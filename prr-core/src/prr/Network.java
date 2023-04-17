package prr;

import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import prr.exceptions.UnavailableFileException;
import prr.exceptions.UnrecognizedEntryException;
import prr.exceptions.AlreadyOffException;
import prr.exceptions.AlreadyOnException;
import prr.exceptions.AlreadySilentException;
import prr.exceptions.DuplicateClientException;
import prr.exceptions.DuplicateTerminalException;
import prr.exceptions.UnknownClientException;
import prr.exceptions.UnknownTerminalException;
import prr.exceptions.InvalidTerminalException;

import prr.clients.Clients;
import prr.communications.Communications;
import prr.terminals.Terminal;
import prr.terminals.BasicTerminal;
import prr.terminals.FancyTerminal;
import prr.clients.Clients.DebtComparator;

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

	private int _communicationsCounter = 1;
	private double _payments = 0.0;
	private double _debt = 0.0;

	private Map<String, Clients> _clients = new TreeMap<>(new CollatorWrapper());

	private Map<String, Terminal> _terminals = new TreeMap<>();

	public int getCommCounter(){return _communicationsCounter;}
	public void incrementCommCounter(){_communicationsCounter++;}
    
	public void registerClient(String clientkey, String name, int clientId) throws DuplicateClientException{
		if (_clients.containsKey(clientkey)){
			throw new DuplicateClientException(clientkey,name);
		}
		Clients client = new Clients(name, clientkey, clientId);
		_clients.put(clientkey, client);
	}

	public Clients getClient(String clientKey) throws UnknownClientException{
		if (!_clients.containsKey(clientKey)){
			throw new UnknownClientException(clientKey);
		}
		return _clients.get(clientKey);
	}

	public void registerTerminalwStatus(Network network,String terminalId, String terminalType, String clientKey, String status) 
		throws DuplicateTerminalException, InvalidTerminalException, UnknownClientException{
		if (status.equals("ON")){
			status = "IDLE";
		}
		try{
			Long.parseLong(terminalId);
		} catch(NumberFormatException nfe){
			throw new InvalidTerminalException(terminalId);
		}
		if (terminalId.length() != 6){
			throw new InvalidTerminalException(terminalId);
		}
		if (_terminals.containsKey(terminalId)){
			throw new DuplicateTerminalException(terminalId);
		}
		if (terminalType.equals("BASIC")){
			BasicTerminal basic = new BasicTerminal(network,terminalId, getClient(clientKey));
			basic.setStatus(status);
			_terminals.put(terminalId, basic);
			_clients.get(clientKey).addTerminal(basic);
		}
		if (terminalType.equals("FANCY")){
			FancyTerminal fancy = new FancyTerminal(network,terminalId, getClient(clientKey));
			fancy.setStatus(status);
			_terminals.put(terminalId, fancy);
			_clients.get(clientKey).addTerminal(fancy);
		}
	}

	public void registerTerminal(String terminalId, String terminalType, String clientKey) 
		throws DuplicateTerminalException, InvalidTerminalException, UnknownClientException{
		String status = "IDLE";
		registerTerminalwStatus(this,terminalId, terminalType, clientKey, status);
	}

	public Terminal getTerminal(String terminalId) throws UnknownTerminalException{
		if (!_terminals.containsKey(terminalId)){
			throw new UnknownTerminalException(terminalId);
		}
		return _terminals.get(terminalId);
	}

	public Collection<Clients> getClients(){
		return Collections.unmodifiableCollection(_clients.values());
	}

	//FIXME maybe add TreeMap, maybe not beacause might be ordered already
	public Collection<Clients> getClientsWithoutDebt(){
		List<Clients> clients = new ArrayList<Clients>();
		for (Clients x : _clients.values()){
			if (x.getDebts() == 0){
				clients.add(x);
			}
		}
		return Collections.unmodifiableCollection(clients);
	}

	public Collection<Clients> getClientsWithDebt(){
		Map<Clients, Clients> map = new TreeMap<>(new DebtComparator());
		for (Clients x : _clients.values()){
			if (x.getDebts() > 0){
				map.put(x, x);
			}
		}
		return Collections.unmodifiableCollection(map.values());
	}

	public Collection<Terminal> getTerminals(){
		return Collections.unmodifiableCollection(_terminals.values());
	}

	public List<Terminal> arrangeInput(String s) throws UnknownTerminalException{
		String[] elements = s.split(",");
		List<String> fixed = Arrays.asList(elements);
		ArrayList<String> friends = new ArrayList<String>(fixed);
		List<Terminal> terminalsAux = new ArrayList<>();
		for (String e : friends){
			terminalsAux.add(getTerminal(e));
		}
		return terminalsAux;
	}

	public Collection<Terminal> getInactiveTerminals(){
		List<Terminal> inactive = new ArrayList<Terminal>();
		for (Terminal e : _terminals.values()){
			if (!e.beenActive()){
				inactive.add(e);
			}
		}
		return Collections.unmodifiableCollection(inactive);
	}

	public Collection<Terminal> getPositiveTerminals(){
		List<Terminal> positive = new ArrayList<Terminal>();
		for (Terminal x : _terminals.values()){
			if (x.positiveBalance()){
				positive.add(x);
			}
		}
		return Collections.unmodifiableCollection(positive);
	}

	public Collection<Communications> getAllComms(){
		List<Communications> allComms = new ArrayList<Communications>();
		for (Terminal x : _terminals.values()){
			for (Communications y : x.getCommsSent()){
				allComms.add(y);
			}
		}
		return Collections.unmodifiableCollection(allComms);
	}

	public Collection<Communications> getCommsSentByClient(Clients client) throws UnknownClientException{
		List<Communications> comms = new ArrayList<Communications>();
		for (Terminal x : getClient(client.getClientKey()).getTerminals()){
			for (Communications y : x.getCommsSent()){
				comms.add(y);
			}
		}
		return Collections.unmodifiableCollection(comms);
	}

	public Collection<Communications> getCommsReceivedByClient(Clients client) throws UnknownClientException{
		List<Communications> comms = new ArrayList<Communications>();
		for (Terminal x : getClient(client.getClientKey()).getTerminals()){
			for (Communications y : x.getCommsReceived()){
				comms.add(y);
			}
		}
		return Collections.unmodifiableCollection(comms);
	}

	public long getPayments(){
		for (Clients x : _clients.values()){
			_payments += x.getPayments();
		}
		return (long) _payments;
	}

	public long getDebts(){
		for (Clients x : _clients.values()){
			_debt += x.getDebts();
		}
		return (long) _debt;
	}

	

	public void registerEntry(String... fields)throws UnrecognizedEntryException, DuplicateClientException, 
		DuplicateTerminalException, UnknownTerminalException, InvalidTerminalException,UnknownClientException{
		switch(fields[0]){
			case "CLIENT" -> registerClient(fields[1], fields[2], Integer.parseInt(fields[3]));
			case "BASIC" -> registerTerminalwStatus(this,fields[1], fields[0], fields[2], fields[3]);
			case "FANCY" -> registerTerminalwStatus(this,fields[1], fields[0], fields[2], fields[3]);
			case "FRIENDS" -> _terminals.get(fields[1]).addFriends(arrangeInput(fields[2]));
			default -> throw new UnrecognizedEntryException(fields[0]);
		} 
	}

	/**
	 * Read text input file and create corresponding domain entities.
	 * 
	 * @param filename name of the text input file
         * @throws UnrecognizedEntryException if some entry is not correct
	 * @throws IOException if there is an IO erro while processing the text file
	 */
	void importFile(String filename) throws UnrecognizedEntryException, IOException, UnavailableFileException  {
		try (BufferedReader in = new BufferedReader(new FileReader(filename))){
			String line;
			while ((line = in.readLine())!=null){
				String[] fields = line.split("\\|");
				try{
					registerEntry(fields);
				} catch(UnrecognizedEntryException | DuplicateClientException | DuplicateTerminalException 
				| UnknownTerminalException | InvalidTerminalException | UnknownClientException e){
					e.printStackTrace();
				}
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}

}

