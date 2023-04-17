package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
import prr.exceptions.UnknownTerminalException;
import prr.app.exceptions.UnknownTerminalKeyException;;

/**
 * Remove friend.
 */
class DoRemoveFriend extends TerminalCommand {

	DoRemoveFriend(Network context, Terminal terminal) {
		super(Label.REMOVE_FRIEND, context, terminal);
		addStringField("terminalId", Prompt.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			_receiver.removeFriend(_network.getTerminal(stringField("terminalId")));
		}catch(UnknownTerminalException e){
			throw new UnknownTerminalKeyException(e.getId());
		}
	}
}
