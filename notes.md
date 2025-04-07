### SQL snippets
```roomsql
SELECT player_id, first_name, last_name, number, position.name as position
FROM skater
JOIN position ON position.position_id = skater.position_primary_id;
```

- Get all teams
```roomsql
SELECT team.city, team.name, division.name as division, conference.name as conference
FROM team
INNER JOIN division ON team."divisionId" = division."divisionId"
INNER JOIN conference ON division."conferenceId" = conference."conferenceId"; 
```

- Get all games
```
SELECT 
    game.*,
    day.date,
    road_team.abbreviation AS road,
    home_team.abbreviation AS home
FROM game
INNER JOIN team AS home_team ON game."homeTeamId" = home_team."teamId"
INNER JOIN team AS road_team ON game."roadTeamId" = road_team."teamId"
INNER JOIN day ON game."dayId" = day."dayId"
WHERE home_team.abbreviation = 'MTL' OR road_team.abbreviation = 'MTL';
```

### DNS
https://stackoverflow.com/questions/8652948/using-port-number-in-windows-host-file
Add `127.0.0.1 fssh` to hosts file (System32/drivers/etc)
```shell
# create
netsh interface portproxy add v4tov4 listenport=80 listenaddress=127.0.0.1 connectport=8080 connectaddress=127.0.0.1
# show
netsh interface portproxy show v4tov4
# delete
netsh interface portproxy delete v4tov4 listenport=80 listenaddress=127.0.0.1
```

### Run
```shell
docker-compose up
```
Adminer:
localhost:8081
Username: postgres
Password: hunter1
Database: fsshockey

### League structure
West
1 - Seattle, Vancouver, Calgary, Edmonton
2 - Colorado, Dallas, Salt Lake City, Nashville
3 - San Jose, Los Angeles, Anaheim, Las Vegas
4 - Chicago, Winnipeg, St. Louis, Minnesota

East
1 - Columbus, Carolina, Florida, Tampa Bay
2 - Pittsburgh, Philadelphia, New Jersey, Washington
3 - Ottawa, Montreal, Boston, Rangers
4 - Detroit, Toronto, Buffalo, Islanders

Schedule
- 82 games total
- 18 games vs division opponents (3 home, 3 away against each)
- 48 games vs non-division same conference opponents (2 home, 2 away against each)
- 16 games vs other conference opponents 
  - One game against each
  - Home against two of the divisions, road against the other two
  - Rotates based on year, four arrangements that cycle

### Player creation and team drafting
- Create enough players for complete rosters
- Generate stats randomly, skills probably based on some bell curve
- Create a draft order, 1-32, and let teams pick players based on ratings
- Teams should get populated relatively evenly with teams owning higher draft picks ending up stronger

### Game simulation: state and events
- Game exists in a certain state (stoppage, home team possession in neutral zone, etc.)
- Each second, an event can occur which alters the state
- This can include changing zone, shot, giveaway, takeaway, penalty, etc.
- Some events can only occur in certain states
  - For example, if a team possesses the puck in their own zone, they can't score a goal
  - They need to advance the puck to the neutral zone, then offensive zone, before registering a shot attempt and goal
- Event probabilities are calculated using the overall stats of players on the ice for each team

### DB Structure for game events and player stats
- Have a GameEvent table for each event that occurs in a game (shot, goal, penalty, hit, block, etc.)
  - Use this for box scores
- Have a GamePlayerStat table. Each player that appears in a game will get an entry in this table
  - FKs gameId, playerId
  - Skaters start off with 0s (hits, goals, etc.)
  - If a player gets a hit, for example, update entry with that player to have +1 hits
  - Then on the roster page can fetch that entry from the table for stats to render
- Have a PlayerSeasonStatAggregate table 
  - Basically sum of a player's counting stats in a season
  - Can this be calculated dynamically? ie instead of me having to update hits from 50 to 53 after a game, calculate hits with a select statement from GameEvent