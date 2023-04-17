package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnknownTerminalException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Add a friend.
 */
class DoAddFriend extends TerminalCommand {

	DoAddFriend(Network context, Terminal terminal) {
		super(Label.ADD_FRIEND, context, terminal);
		addStringField("terminalId", Prompt.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			_receiver.addFriend(_network.getTerminal(stringField("terminalId")));
		}catch(UnknownTerminalException e){
			throw new UnknownTerminalKeyException(e.getId());
		}
	}
}
