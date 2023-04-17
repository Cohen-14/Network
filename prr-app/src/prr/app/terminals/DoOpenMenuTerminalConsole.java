package prr.app.terminals;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.app.terminal.Menu;
import prr.exceptions.UnknownTerminalException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Open a specific terminal's menu.
 */
class DoOpenMenuTerminalConsole extends Command<Network> {

	DoOpenMenuTerminalConsole(Network receiver) {
		super(Label.OPEN_MENU_TERMINAL, receiver);
		addStringField("terminalId", Prompt.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			Terminal terminal = _receiver.getTerminal(stringField("terminalId"));
			Menu menu = new Menu(_receiver, terminal);
			menu.open();
		} catch(UnknownTerminalException e){
			throw new UnknownTerminalKeyException(e.getId());
		}
	}
}
