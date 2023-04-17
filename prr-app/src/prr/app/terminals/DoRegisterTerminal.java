package prr.app.terminals;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.DuplicateTerminalKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.DuplicateTerminalException;
import prr.exceptions.InvalidTerminalException;
import prr.exceptions.UnknownClientException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

	DoRegisterTerminal(Network receiver) {
		super(Label.REGISTER_TERMINAL, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			String terminalId;
			String terminalType;
			String clientKey;
			Form request = new Form();
			terminalId = request.requestString(Prompt.terminalKey());	
			do{
				terminalType = Form.requestString(Prompt.terminalType());
			}while (!terminalType.equals("BASIC") && !terminalType.equals("FANCY"));
			clientKey = request.requestString(Prompt.clientKey());
			request.parse();
			_receiver.registerTerminal(terminalId, terminalType, clientKey);
		} catch(DuplicateTerminalException e){
			throw new DuplicateTerminalKeyException(e.getTerminalId());
		} catch(UnknownClientException e){
			throw new UnknownClientKeyException(e.getClientKey());
		} catch(InvalidTerminalException e){
			throw new InvalidTerminalKeyException(e.getTerminalId());
		}
	}
}
