package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.UnknownTerminalException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {

        DoSendTextCommunication(Network context, Terminal terminal) {
                super(Label.SEND_TEXT_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
                addStringField("terminalKey", Prompt.terminalKey());
                addStringField("text", Prompt.textMessage());
        }

        @Override
        protected final void execute() throws CommandException {
                try{
                        _receiver.sendText(_network.getTerminal(stringField("terminalKey")),stringField("text"));
                }catch (UnknownTerminalException e){
                        throw new UnknownTerminalKeyException(e.getId());
                }catch (DestinationIsOffException e){
                        _display.popup(Message.destinationIsOff(stringField("terminalKey")));
                }
        }
} 
