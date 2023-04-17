package prr.app.clients;

import prr.Network;
import prr.app.exceptions.DuplicateClientKeyException;
import prr.exceptions.DuplicateClientException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;


/**
 * Register new client.
 */
class DoRegisterClient extends Command<Network> {

	DoRegisterClient(Network receiver) {
		super(Label.REGISTER_CLIENT, receiver);
		addStringField("clientkey",Prompt.key());
		addStringField("name", Prompt.name());
		addIntegerField("taxId", Prompt.taxId());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			_receiver.registerClient(stringField("clientkey"),stringField("name"),integerField("taxId"));
		}catch(DuplicateClientException e){
			throw new DuplicateClientKeyException(e.getClientKey());
		}
	}

}
