import static it.utils.TestUtils.testIllegalArgumentException;

import edu.princeton.cs.algs4.StdOut;

public class BaseballEliminationTest {

    public static void main(String[] argv) {
        String[] args = {"tests/teams10.txt"};
        BaseballElimination division = new BaseballElimination(args[0]);

        testIllegalArgumentException(() -> division.wins("unknown"));
        testIllegalArgumentException(() -> division.losses("unknown"));
        testIllegalArgumentException(() -> division.remaining("unknown"));
        testIllegalArgumentException(() -> division.isEliminated("unknown"));
        testIllegalArgumentException(() -> division.certificateOfElimination("unknown"));
        testIllegalArgumentException(() -> division.against("Team0", "unknown"));
        testIllegalArgumentException(() -> division.against("unknown", "Team0"));

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
