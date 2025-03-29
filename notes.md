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