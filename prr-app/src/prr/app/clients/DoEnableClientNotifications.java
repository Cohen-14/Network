package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.UnknownClientException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Enable client notifications.
 */
class DoEnableClientNotifications extends Command<Network> {

	DoEnableClientNotifications(Network receiver) {
		super(Label.ENABLE_CLIENT_NOTIFICATIONS, receiver);
		addStringField("clientKey", Prompt.key());
		//FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			if (_receiver.getClient(stringField("clientKey")).getAllowNotif()){
				_display.popup(Message.clientNotificationsAlreadyEnabled());
			}
			_receiver.getClient(stringField("clientKey")).enableNotifications();
		}catch(UnknownClientException e){
			throw new UnknownClientKeyException(e.getClientKey());
		}
	}
}
