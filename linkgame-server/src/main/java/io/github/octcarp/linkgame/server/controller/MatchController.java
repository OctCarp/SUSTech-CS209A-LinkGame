package io.github.octcarp.linkgame.server.controller;

import java.util.*;

public class MatchController {
    Map<String, MatchInfo> matchMap = new HashMap<>();
    Set<String> waitingSet = new HashSet<>();

    public void addMatch(String id1, String id2) {
        MatchInfo matchInfo = new MatchInfo(id1, id2);
        matchMap.put(id1, matchInfo);
        matchMap.put(id2, matchInfo);
    }

    public String disconnectMatch(String id) {
        MatchInfo matchInfo = matchMap.get(id);
        if (matchInfo != null) {
            if (matchInfo.id1.equals(id)) {
                if (matchInfo.dis2) {
                    waitingSet.remove(matchInfo.id1);
                    matchMap.remove(matchInfo.id1);
                    matchMap.remove(matchInfo.id2);
                } else {
                    matchInfo.dis1 = true;
                    waitingSet.add(matchInfo.id2);
                }
            } else if (matchInfo.id2.equals(id)) {
                if (matchInfo.dis1) {
                    waitingSet.remove(matchInfo.id2);
                    matchMap.remove(matchInfo.id1);
                    matchMap.remove(matchInfo.id2);
                } else {
                    matchInfo.dis2 = true;
                    waitingSet.add(matchInfo.id1);
                }
            } else {
                System.err.println("Inner wrong: Wrong match Map");
            }
        } else {
            return "No match found";
        }
        return "Success";
    }

    public String onConnect(String id){
        if (waitingSet.isEmpty()) {
            return "-No match found";
        } else {
            MatchInfo matchInfo = matchMap.get(id);
            if (matchInfo != null) {
                if (matchInfo.dis1) {
                    matchInfo.dis1 = false;
                    return matchInfo.id2;
                } else if (matchInfo.dis2) {
                    matchInfo.dis2 = false;
                    return matchInfo.id1;
                } else {
                    System.err.println("Inner wrong: Wrong match Map");
                }
                return "-Already in match, reconnect";
            } else {
                String id2 = waitingSet.iterator().next();
                waitingSet.remove(id2);
                addMatch(id, id2);
                return id2;
            }
        }
    }
}

class MatchInfo {
    String id1;
    String id2;
    boolean dis1;
    boolean dis2;

    public MatchInfo(String id1, String id2) {
        this.id1 = id1;
        this.id2 = id2;
        dis1 = false;
        dis2 = false;
    }
}
