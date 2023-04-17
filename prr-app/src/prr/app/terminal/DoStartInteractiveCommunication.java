package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.UnknownTerminalException;
import prr.exceptions.UnsupportedAtDestinationException;
import prr.exceptions.UnsupportedAtOriginException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

	DoStartInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.START_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			String terminalId;
			String commType;
			Form request = new Form();
			terminalId = request.requestString(Prompt.terminalKey());
			do{
				commType = Form.requestString(Prompt.commType());
			}while(!commType.equals("VIDEO") && !commType.equals("VOICE"));
			request.parse();
			_receiver.startInteractiveComm(_network.getTerminal(terminalId), commType);
		} catch(UnsupportedAtOriginException e){
			_display.popup(Message.unsupportedAtOrigin(e.getKey(), e.getType()));
		} catch (UnsupportedAtDestinationException e){
			_display.popup(Message.unsupportedAtDestination(e.getKey(), e.getType()));
		} catch (DestinationIsBusyException e){
			_display.popup(Message.destinationIsBusy(e.getKey()));
		} catch (DestinationIsOffException e){
			_display.popup(Message.destinationIsOff(e.getKey()));
		} catch (DestinationIsSilentException e){
			_display.popup(Message.destinationIsSilent(e.getKey()));
		}catch (UnknownTerminalException e){
			throw new UnknownTerminalKeyException(e.getId());
		}
	}
}
