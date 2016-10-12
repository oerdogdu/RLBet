package com.solstice.atoi.rlbet;

/**
 * Created by Atoi on 23.09.2016.
 */
public class Bets {
    private String team1, team2;
    private double x1, x2;

    public Bets(String team1, String team2, double x1, double x2) {
        this.team1 = team1;
        this.team2 = team2;
        this.x1 = x1;
        this.x2 = x2;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }
}
