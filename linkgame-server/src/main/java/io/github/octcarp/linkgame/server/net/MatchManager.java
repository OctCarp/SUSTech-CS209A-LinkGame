package io.github.octcarp.linkgame.server.net;

import io.github.octcarp.linkgame.common.module.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchManager {
    private static MatchManager instance;

    private List<Match> matches;

    private MatchManager() {
        matches = new ArrayList<>();
    }

    public static MatchManager getInstance() {
        if (instance == null) {
            instance = new MatchManager();
        }
        return instance;
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public void removeMatch(Match match) {
        matches.remove(match);
    }

    public List<Match> getMatches() {
        return matches;
    }
}
