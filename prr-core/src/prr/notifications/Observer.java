package prr.notifications;

public interface Observer {
    public void update(String newState, String terminalId);
}
