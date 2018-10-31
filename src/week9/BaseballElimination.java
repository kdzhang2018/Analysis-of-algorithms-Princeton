package week9;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    
    private final int num;
    private final String[] teams;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;
    
    private Queue<String> R;
    
    public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
    {
        In file = new In(filename);
        num = file.readInt();
        
        teams = new String[num];
        w = new int[num];
        l = new int[num];
        r = new int[num];
        g = new int[num][num];
        
        int i = 0;
        
        while (file.hasNextChar() && i < num) {
            teams[i] = file.readString();
            w[i] = file.readInt();
            l[i] = file.readInt();
            r[i] = file.readInt();
            for (int j = 0; j < num; j++) {
                g[i][j] = file.readInt();
            }
            i++;  
        }
        
        
    }
    public int numberOfTeams()                        // number of teams
    {
        return num;
        
    }
    
    public Iterable<String> teams()                                // all teams
    {
        Queue<String> q = new Queue<String>();
        for (int i = 0; i < num; i++) {
            q.enqueue(teams[i]);
        }
        return q;
        
    }
    private int index(String team) {        // find the index of the team
        int i = 0;
        while (i < num) {
            if (teams[i].equals(team)) 
                break;
            i++;
        }
        if (i == num) throw new IllegalArgumentException();
        return i;
    }
    public int wins(String team)                      // number of wins for given team
    {
        return w[index(team)];
        
    }
    public int losses(String team)                    // number of losses for given team
    {
        return l[index(team)];
        
    }
    public int remaining(String team)                 // number of remaining games for given team
    {
        return r[index(team)];
        
    }
    public int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        return g[index(team1)][index(team2)];
        
    }
    public boolean isEliminated(String team)              // is given team eliminated?
    {
        int x = index(team);
        int sum = w[x] + r[x];
        
        R = new Queue<String>();
        // trivial elimination: w[x] + r[x] < w[i]
        for (int i = 0; i < num; i++) {
            if (sum < w[i]) {
                R.enqueue(teams[i]);
                return true;
            }
        }
        
        // nontrivial elimination: construct a flow network and solve a maxflow problem
        int vNum = num * (num - 1) / 2 + 2; // total number of vertices
        FlowNetwork fn = new FlowNetwork(vNum);
        
        int teamV = (num - 1) * (num -2) /2 + 1; // the first team vertex
        // if i < x: team i corresponds to vertex teamV + i
        // if i > x: team i corresponds to vertex teamV + i -1
        
        int edgeV = 1;
        for (int i = 0; i < num; i++) {
            for (int j = i + 1; j < num; j++) {
                if (i < x && j < x) {
                    // add edge between s and game vertices
                    FlowEdge e = new FlowEdge(0, edgeV, g[i][j]);
                    fn.addEdge(e);
                    // add edges between game vertices and team vertices
                    e = new FlowEdge(edgeV, teamV + i, Double.POSITIVE_INFINITY);
                    fn.addEdge(e);
                    e = new FlowEdge(edgeV, teamV + j, Double.POSITIVE_INFINITY);
                    fn.addEdge(e);
                    edgeV++;
                }
                else if (i < x && j > x) {
                    FlowEdge e = new FlowEdge(0, edgeV, g[i][j]);
                    fn.addEdge(e);
                    e = new FlowEdge(edgeV, teamV + i, Double.POSITIVE_INFINITY);
                    fn.addEdge(e);
                    e = new FlowEdge(edgeV, teamV + j - 1, Double.POSITIVE_INFINITY);
                    fn.addEdge(e);
                    edgeV++;
                }
                else if (i > x && j > x) {
                    FlowEdge e = new FlowEdge(0, edgeV, g[i][j]);
                    fn.addEdge(e);
                    e = new FlowEdge(edgeV, teamV + i - 1, Double.POSITIVE_INFINITY);
                    fn.addEdge(e);
                    e = new FlowEdge(edgeV, teamV + j - 1, Double.POSITIVE_INFINITY);
                    fn.addEdge(e);
                    edgeV++;
                }
            }
        }
        
        // add edges between team vertices and t
        for (int i = 0; i < num; i++) {
            if (i < x) {
                FlowEdge e = new FlowEdge(teamV + i, vNum -1, sum - w[i]);
                fn.addEdge(e);
            }
            else if (i > x) {
                FlowEdge e = new FlowEdge(teamV + i - 1, vNum -1, sum - w[i]);
                fn.addEdge(e);
            }
        }
        
        //StdOut.print(fn);
        
        // maxflow using FordFulkerson
        FordFulkerson ff = new FordFulkerson(fn, 0, vNum - 1);
        //StdOut.print(fn);
        
        // if all edges pointing from s are full, team x is not eliminated
        boolean eliminated = false;
        for (FlowEdge fe: fn.adj(0)) {
            if (fe.capacity() != fe.flow())
                eliminated = true;
        }
        // create certificate of elimination: R
        if (eliminated) {
            for (int i = 0; i < num; i++) {
                if (i < x && ff.inCut(teamV + i)) 
                    R.enqueue(teams[i]);
                if (i > x && ff.inCut(teamV + i - 1))
                    R.enqueue(teams[i]);
            }
        }
        return eliminated;
        
    }
    public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        if (isEliminated(team))
            return R;
        else
            return null;
    }
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        /*StdOut.println(division.numberOfTeams());
        for (String s: division.teams) {
            StdOut.println(s);
        }
        StdOut.println(division.wins("New_York"));
        StdOut.println(division.losses("New_York"));
        StdOut.println(division.remaining("New_York"));
        StdOut.println(division.against("New_York", "Baltimore"));*/
        
        //division.isEliminated("Detroit");
        
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
