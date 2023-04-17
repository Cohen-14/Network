package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.exceptions.UnknownClientException;
import prr.app.exceptions.UnknownClientKeyException;
//FIXME add more imports if needed

/**
 * Show the payments and debts of a client.
 */
class DoShowClientPaymentsAndDebts extends Command<Network> {

	DoShowClientPaymentsAndDebts(Network receiver) {
		super(Label.SHOW_CLIENT_BALANCE, receiver);
		addStringField("clientKey", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			String clientKey = stringField("clientKey");
			long payments = _receiver.getClient(clientKey).getPayments();
			long debts = _receiver.getClient(clientKey).getDebts();
			_display.popup(Message.clientPaymentsAndDebts(clientKey, payments, debts));
		}catch(UnknownClientException e){
			throw new UnknownClientKeyException(e.getClientKey());
		}
	}
}
