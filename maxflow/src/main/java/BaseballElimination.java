import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {

    private int nTeams;
    private Map<String, Integer> teamIds;
    private Row[] table;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        nTeams = Integer.parseInt(in.readLine());
        table = new Row[nTeams];
        teamIds = new HashMap<>();
        for (int position = 0; position < nTeams; position++) {
            String record = in.readLine().trim();
            String[] records = record.split("\\s+");
            String team = records[0];
            int wins = Integer.parseInt(records[1]);
            int loss = Integer.parseInt(records[2]);
            int left = Integer.parseInt(records[3]);
            int[] games = new int[nTeams];
            for (int i = 0; i < nTeams; i++) {
                games[i] = Integer.parseInt(records[4 + i]);
            }
            table[position] = new Row(team, position, wins, loss, left, games);
            teamIds.put(team, position);
        }
        findEliminationTeams();
    }

    public int numberOfTeams() {
        return nTeams;
    }

    public Iterable<String> teams() {
        return teamIds.keySet();
    }

    public int wins(String team) {
        return getResults(team).wins;
    }

    public int losses(String team) {
        return getResults(team).loss;
    }

    public int remaining(String team) {
        return getResults(team).left;
    }

    public int against(String team1, String team2) {
        return getResults(team1).games[getTeamId(team2)];
    }

    public boolean isEliminated(String team) {
        return getResults(team).eliminated;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return getResults(team).eliminationSet;
    }

    private void findEliminationTeams() {
        int nOpponents = nTeams - 1;
        // from combination formula nOpponents! / ((nOpponents - 2)! * 2!)
        int teamPairs = nOpponents * (nOpponents - 1) / 2;
        int networkSize = 2 + nOpponents + teamPairs;
        int teamVerticesStartId = teamPairs + 1;

        // table not always sorted by wins - find current leader
        String currentLeader = table[0].team;
        int currentLeaderWins = table[0].wins;
        for (int i = 1; i < nTeams; i++) {
            if (table[i].wins > currentLeaderWins) {
                currentLeaderWins = table[i].wins;
                currentLeader = table[i].team;
            }
        }

        // sort teams according the max points team can get if it wins all the rest games. Stop search
        // as soon as the first non-eliminated team found
        Row[] searchTable = table.clone();
        Arrays.sort(searchTable, Comparator.comparingInt((Row a) -> a.wins + a.left).reversed());
        int eliminatedTeams = 0;
        // At least the team with the max possible points cannot be eliminated
        while (eliminatedTeams < nOpponents) {
            // keep testing row as the last element in a search table to make indexing easier
            Row targetRow = searchTable[nOpponents - eliminatedTeams];
            searchTable[nOpponents - eliminatedTeams] = searchTable[nOpponents];
            searchTable[nOpponents] = targetRow;
            int targetMaxPoints = targetRow.wins + targetRow.left;

            List<String> eliminationSet = new ArrayList<>();
            if (targetMaxPoints < currentLeaderWins) {
                eliminationSet.add(currentLeader);
            } else {
                // It is possible to optimize the process by only updating the edges where
                // the inspected team is involved instead of re-creating a full network each time.
                // The same maxflow algorithm can start from the almost-full network without any problem.
                // The inspected team is always the last in the search table, all vertices keep the index
                // on each run, so the indexing is easy.
                // Classes from the algs4 package do not allow modifications. There are no interfaces
                // and state is kept in final variables.  This makes in very boring to implement
                // full data model from scratch
                FlowNetwork network = new FlowNetwork(networkSize);

                int gameVertexId = 1;
                for (int i = 0; i < nOpponents; i++) {
                    Row row = searchTable[i];
                    for (int j = i + 1; j < nOpponents; j++) {
                        // There are 0 capacity edges that can be removed from the network. However,
                        // this rather minor improvement will make the indexing process much harder.
                        Row opponent = searchTable[j];
                        int gamesLeft = row.games[opponent.position];
                        network.addEdge(new FlowEdge(0, gameVertexId, gamesLeft));
                        network.addEdge(new FlowEdge(gameVertexId, teamVerticesStartId + i,
                                Double.POSITIVE_INFINITY));
                        network.addEdge(new FlowEdge(gameVertexId, teamVerticesStartId + j,
                                Double.POSITIVE_INFINITY));
                        gameVertexId++;
                    }
                    network.addEdge(new FlowEdge(teamVerticesStartId + i, networkSize - 1,
                            targetMaxPoints - row.wins));

                }
                FordFulkerson maxFlow = new FordFulkerson(network, 0, networkSize - 1);
                for (int i = 0; i < nOpponents; i++) {
                    if (maxFlow.inCut(teamVerticesStartId + i)) {
                        eliminationSet.add(searchTable[i].team);
                    }
                }
            }
            if (eliminationSet.isEmpty()) {
                break;
            } else {
                targetRow.eliminated = true;
                targetRow.eliminationSet = eliminationSet;
                eliminatedTeams++;
            }
        }
    }

    private int getTeamId(String team) {
        if (!teamIds.containsKey(team)) {
            throw new IllegalArgumentException(String.format("Unknown team `%s`", team));
        }
        return teamIds.get(team);
    }

    private Row getResults(String team) {
        return table[getTeamId(team)];
    }

    private static class Row {

        final String team;
        // Position in the table. Not place in the rating, cause input not guaranteed to be sorted by wins.
        final int position;
        final int wins;
        final int loss;
        final int left;
        final int[] games;
        boolean eliminated = false;
        List<String> eliminationSet = null;

        Row(String team, int position, int wins, int loss, int left, int[] games) {
            this.team = team;
            this.position = position;
            this.wins = wins;
            this.loss = loss;
            this.left = left;
            this.games = games;
        }
    }
}
