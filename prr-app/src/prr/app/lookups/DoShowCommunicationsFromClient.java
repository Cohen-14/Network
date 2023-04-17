package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.exceptions.UnknownClientKeyException;
//FIXME add more imports if needed
import prr.exceptions.UnknownClientException;

/**
 * Show communications from a client.
 */
class DoShowCommunicationsFromClient extends Command<Network> {

	DoShowCommunicationsFromClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_FROM_CLIENT, receiver);
		addStringField("clientKey", Prompt.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			_display.popup(_receiver.getCommsSentByClient(_receiver.getClient(stringField("clientKey"))));
		}catch(UnknownClientException e){
			throw new UnknownClientKeyException(e.getClientKey());
		}
	}
}
