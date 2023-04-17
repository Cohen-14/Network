package prr.exceptions;

public class UnknownTerminalException extends Exception {
    
    private static final long serialVersionUID = 202208091753L;

    private String _terminalId; 

    public UnknownTerminalException(String termianlId){
        _terminalId = termianlId;
    }

    public String getId(){
        return _terminalId;
    }
}
