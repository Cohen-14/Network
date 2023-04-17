package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.UnknownClientException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Disable client notifications.
 */
class DoDisableClientNotifications extends Command<Network> {

	DoDisableClientNotifications(Network receiver) {
		super(Label.DISABLE_CLIENT_NOTIFICATIONS, receiver);
		addStringField("clientKey", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			if (!_receiver.getClient(stringField("clientKey")).getAllowNotif()){
				_display.popup(Message.clientNotificationsAlreadyEnabled());
			}
			_receiver.getClient(stringField("clientKey")).disableNotifications();
		}catch(UnknownClientException e){
			throw new UnknownClientKeyException(e.getClientKey());
		}
	}
}
