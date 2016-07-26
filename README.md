## **Druid Kafka Ingestion**

## Overview
    This is an example of how you can ingest data into the Druid database using Kafka.
    The example is written in Java and assumes one have installed Druid services and Kafka.
    A fast way to acquire this is to install a sandbox that has many of needed services (like hortonworks, mapr, cloudera). 
    Currently Druid is not available on the mentioned sandbox, but you can refer to [Druid Documentation](http://druid.io/docs/0.9.0/tutorials/quickstart.html) for starting it.
    It would be better to configure Druid on the sandbox, because Druid needs Zookeeper service to be present for nodes synchronization.
    The next step assume that druid is present on a mapr sandbox.

### Needed components
Druid
    On the mapr sandbox Zookeeper port is 5181. To start Druid you must configure zookeeper port in config/_common/common.runtime.properties located in the directory where Druid is installed
    Take a look on the configuration files for each of the Druid services and ensure the sandbox have enough memory to start all the services
    Go to http://maprdemo:8081/#/ , where druid is installed to see if it is started
    
Kafka broker
Start Kafka broker by executing the command : ```./bin/kafka-server-start.sh /config/server.properties```
Look on the console if Kakfa was started.
Create a Kafka topic for our custom messages ```./bin/kafka-topics.sh --create --zookeeper localhost:5181 --replication-factor 1 --partitions 1 --topic pageviews```
Pageview will be the name for out topic where data coming from outside will be stored     
    
    
Tranquility Server

```bin/tranquility kafka -configFile ../druid-0.9.1.1/conf-quickstart/tranquility/kafka.json```

dsd

```[KafkaConsumer-1] INFO  c.metamx.emitter.core.LoggingEmitter - Start: started [true]```


_dasdasasd_