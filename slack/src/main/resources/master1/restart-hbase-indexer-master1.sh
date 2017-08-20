#!/bin/bash

# It will be executed by the api, the main task of the script is to execute the restart command in the particular screen window
echo "Restarting Hbase-indexer on master1..."

# Since we are restarting, so first we will execute CTRL+C on screen window to make sure everything is clean before restarting
# $'\003' sends CTRL+C on screen
screen -S 2750.pts-0.ip-10-90-162-79 -p 3 -X stuff $'\003'

#Execute main command, the below line needs to be modified with the actual command, `echo -ne '\015'` sends Enter
screen -S 2750.pts-0.ip-10-90-162-79 -X stuff 'command'`echo -ne '\015'`

exit 0