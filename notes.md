### SQL snippets
```roomsql
SELECT player_id, first_name, last_name, number, position.name as position
FROM skater
JOIN position ON position.position_id = skater.position_primary_id;
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