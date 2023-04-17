package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.UnknownClientException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show specific client: also show previous notifications.
 */
class DoShowClient extends Command<Network> {

	DoShowClient(Network receiver) {
		super(Label.SHOW_CLIENT, receiver);
		addStringField("clientKey", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			if (_receiver.getClient(stringField("clientKey")).getPendingNotifs()){
				_display.popup(_receiver.getClient(stringField("clientKey")));
				_display.popup(_receiver.getClient(stringField("clientKey")).showNotifications());
			}
			else{
				_display.popup(_receiver.getClient(stringField("clientKey")));
			}
		}catch(UnknownClientException e){
			throw new UnknownClientKeyException(e.getClientKey());
		}
	}
}
