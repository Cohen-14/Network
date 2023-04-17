package prr.exceptions;

public class DuplicateTerminalException extends Exception{
    
    private static final long serialVersionUID = 202208091753L;

    private String _terminalId;

    public DuplicateTerminalException(String terminalId){
        _terminalId = terminalId;
    }

    public String getTerminalId(){
        return _terminalId;
    }
}
