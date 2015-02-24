events storage (PoC)
--------------------

1. events-storage
2. events-api
3. events-ui

vagrant-coreos
--------------

*Expected result*

1. events-storage (172.17.8.11:27017 - mongodb)
2. events-api (http://172.17.8.21:8080/event)
3. events-ui (http://172.17.8.21:9000)

*Run*

1. Generate new discovery URL: http://discovery.etcd.io/new
2. Update discovery URL, files: environment/vagrant-coreos/user-data-app.yaml, environment/vagrant-coreos/user-data-db.yaml
3. Run: vagrant up (in environment/vagrant-coreos)
4. Run: ssh-add ~/.vagrant.d/insecure_private_key
5. Run: vagrant ssh core-db-01 -- -A
6. in core-db-01
6.1. Run: fleetctl submit services/*
6.2. Run: fleetctl start services/*

Triggered global unit mongodb-ambassador.service start
Triggered global unit registrator.service start
Unit events-api.service launched on 220d62c3.../172.17.8.21
Unit events-ui.service launched on 220d62c3.../172.17.8.21
Unit mongodb.service launched on 2381c95f.../172.17.8.11
(or something similar)

6.3. Run: fleetctl list-units (... wait for all services)

UNIT				        |MACHINE			        |ACTIVE	|SUB
:---------------------------|:--------------------------|:------|:------
events-api.service		    |61278f87.../172.17.8.21	|active	|running
events-ui.service		    |61278f87.../172.17.8.21	|active	|running
mongodb-ambassador.service	|61278f87.../172.17.8.21	|active	|running
mongodb.service			    |3d8d6774.../172.17.8.11	|active	|running
registrator.service		    |3d8d6774.../172.17.8.11	|active	|running
registrator.service		    |61278f87.../172.17.8.21	|active	|running

(if some service has status ACTIVE=failed, check service with fleetctl or systemctl)

7. Run jmeter tests

jmeter -n -t events-api/src/test/jmeter/events.jmx
Creating summariser <summary>
Created the tree successfully using events-api/src/test/jmeter/events.jmx
Starting the test @ Tue Feb 24 21:58:04 CET 2015 (1424811484804)
Waiting for possible shutdown message on port 4445
summary +   9952 in    25s =  402,8/s Avg:   244 Min:    16 Max:  4052 Err:     0 (0,00%) Active: 100 Started: 100 Finished: 0
summary +  10048 in  17,1s =  588,9/s Avg:   162 Min:     5 Max:   429 Err:     0 (0,00%) Active: 0 Started: 100 Finished: 100
summary =  20000 in    42s =  481,3/s Avg:   203 Min:     5 Max:  4052 Err:     0 (0,00%)
Tidying up ...    @ Tue Feb 24 21:58:46 CET 2015 (1424811526849)
... end of run