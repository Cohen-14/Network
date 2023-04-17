package prr.exceptions;

public class InvalidTerminalException extends Exception{

    private static final long serialVersionUID = 202208091753L;

    private String _terminalId;

    public InvalidTerminalException(String terminalId){
        _terminalId = terminalId;
    }

    public String getTerminalId(){
        return _terminalId;
    }
}
