# Java Glicko Formula Resource

This is a Java implementation of Glicko's original formula. My Glicko resource will calculate a player's new rating and rating deviation after a series of matches against other players.

[Here](http://www.glicko.net/glicko/glicko.pdf) is Mark Glickman's article about his formula, which explains in some depth why his formula is superior to the Elo system. Glickman has a second, improved formula which he describes in detail [here](http://www.glicko.net/glicko/glicko2.pdf).

This resource is based on Glickman's *first* formula. Resources already exist for his second formula. ([Here](https://github.com/goochjs/glicko2) is one).

## How to Use

Download the jarfile and add it to your build path. New ratings may be calculated on an individual, player-by-player basis, or all at once.

This example uses the same data found in Glickman's article:

```Java
GlickoCompetitor player1 = new GlickoCompetitor("player1", 1500, 200);
GlickoCompetitor player2 = new GlickoCompetitor("player2", 1400, 30);
GlickoCompetitor player3 = new GlickoCompetitor("player3", 1550, 100);
GlickoCompetitor player4 = new GlickoCompetitor("player4", 1700, 300);

GlickoMatchSet matchSet = new GlickoMatchSet();
matchSet.addMatch(new GlickoMatch(player1, player2, GlickoMatchOutcome.WIN)); // player1 beat player2
matchSet.addMatch(new GlickoMatch(player1, player3, GlickoMatchOutcome.LOSS));
matchSet.addMatch(new GlickoMatch(player1, player4, GlickoMatchOutcome.LOSS));
```

Now that we have created players, created a match set, and added matches involving those players, we can determine the new ratings for a specific player (this will NOT modify the player object at all):

```Java
System.out.println(matchSet.getNewRating(player1));
System.out.println(matchSet.getNewRatingDeviation(player1));
```

... or we can update all the player objects at once, and then get the new attributes from the player objects:

```Java
matchSet.assignNewDataToAllCompetitors();

System.out.println(player1.getNewRating());
System.out.println(player1.getNewRatingDeviation());
System.out.println(player2.getNewRating());
System.out.println(player2.getNewRatingDeviation());
```

Once player objects have been updated once, they should not be updated again. Currently, they store values for "original rating" and "new rating" (and similar values for their rating deviations). They do not support subsequent updates. This will change in the future.

Expect improvements in the next few days. I banged this together in one evening with the GOP convention going on in the background.
