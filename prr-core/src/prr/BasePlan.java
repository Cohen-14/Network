package prr;

import java.io.Serializable;

import prr.clients.Clients;

public class BasePlan extends Plan implements Serializable {
    
    public BasePlan(Clients client){
        super(client);
    }

    @Override
    public int callCost(int minutes, boolean areFriends){
       int callCost;
       String clientStatus = getClient().status();
       if (clientStatus.equals("PLATINUM") || clientStatus.equals("GOLD")){
        callCost = 10 * minutes;
        if (!areFriends){
            return callCost;
        }
        return callCost/2;
        }
        else{
            callCost = 20 * minutes;
            if (!areFriends){
                return callCost;
            }
            return callCost / 2;
        }
    }

    @Override
    public int videoCost(int minutes, boolean areFriends){
        int callCost;
        String clientStatus = getClient().status();
        if (clientStatus.equals("PLATINUM")){
            callCost = 10 * minutes;
            if (!areFriends){
                return callCost;
            }
            return callCost/2;
        }
        else if (clientStatus.equals("GOLD")){
            callCost = 20 * minutes;
            if (!areFriends){
                return callCost;
            }
            return callCost / 2;
        }
        else{
            callCost = 30 * minutes;
            if (!areFriends){
                return callCost;
            }
            return callCost / 2;
        }
    }

    @Override
    public int textCost(int size){
        int cost = 0;
        String clientStatus = getClient().status();
        if (size < 50){
            switch (clientStatus){
                case "PLATINUM" -> cost = 0;
                default -> cost = 10;
            }
        }
        if (size >= 50 && size<100){
            switch(clientStatus){
                case "PLATINUM" -> cost = 4;
                case "GOLD" -> cost = 10;
                case "NORMAL" -> cost = 16;
            }
        }
        else{
            switch(clientStatus){
                case "PLATINUM" -> cost = 4;
                default -> cost = 2 * size;
            }
        }
        return cost;
    }

    @Override
    public String plan(){
        return "BASE";
    }
}
