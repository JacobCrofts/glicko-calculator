# Java Glicko Formula Resource:
*mass-calculate player ratings in a rated tournament such as chess*

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

// the outcome is relative to the first argument (so: player1 beats player2)
matchSet.addMatch(new GlickoMatch(player1, player2, GlickoMatchOutcome.WIN));

// now player1 loses to player3 and player4
matchSet.addMatch(new GlickoMatch(player1, player3, GlickoMatchOutcome.LOSS));
matchSet.addMatch(new GlickoMatch(player1, player4, GlickoMatchOutcome.LOSS));
```

Now that we have created players, created a match set, and added matches involving those players, we can determine the new rating and rating deviation for a specific player. These static methods provide information without updating the player objects:

```Java
System.out.println(GlickoCalculator.getNewRating(player1));
System.out.println(GlickoCalculator.getNewRatingDeviation(player1));
```

We can also update all the player objects at once, and then get the new attributes from the player objects:

```Java
matchSet.assignNewDataToAllCompetitors();

System.out.println(player1.getRating());
System.out.println(player1.getRatingDeviation());
System.out.println(player2.getRating());
System.out.println(player2.getRatingDeviation());
```

Old player objects may be added to a new GlickoMatchSet along with new matches and updated again. Once their ratings and rating deviations have been recalculated, however, their old rating/deviation data will be inaccessible.

# Runtime Concerns

GlickoMatchSet#assignNewDataToAllCompetitors becomes more and more expensive depending primarily on how many matches you have. When evaluating realistic, real-world situations, it should finish in tens of milliseconds or less. However, a tournament with one million matches took around 670ms to run on my system, and any tournament with over 10,000,000 matches may cause time outs. You may want to run this task asynchronously if performance is a concern.

# Expected Improvements

In future updates, I will provide an API and documentation for determining C, a constant in Glickman's formula. Different games should have different values of C depending on how long it takes a player to get "out of practice" at the game.
