## **From Kafka to Druid**

### Overview

This is an example of how you can ingest time series data into the Druid database using Kafka.
A Kakfa producer will push JSON messages on a specific topic that will be ingested into the Druid databases using the Tranquility service that comes with Druid.
A fast way to acquire almost all the needed setup is to install a sandbox that has many of needed services (like hortonworks, mapr, cloudera). 

### Components
#### Druid
Currently Druid is not available on the mentioned sandbox, but you can refer to [Druid Documentation](http://druid.io/docs/0.9.0/tutorials/quickstart.html) for starting it.
It would be better to configure Druid on the sandbox, because Druid needs Zookeeper service to be present for nodes synchronization.
The next step assume that druid is present on a mapr sandbox.
On the mapr sandbox the Zookeeper port is 5181. To start Druid you must configure zookeeper port in 
```config/_common/common.runtime.properties``` located in the directory where Druid is installed
Take a look on the configuration files for each of the Druid services and ensure the sandbox have enough memory to start all the services
Go to http://maprdemo:8081/#/ , where druid is installed to see if it is started
    
####Kafka broker
Start Kafka broker by executing the command: 
```./bin/kafka-server-start.sh /config/server.properties```  
This will start the Kafka server on port 9082. Look on the console if Kakfa was started.
Create a Kafka topic for our custom messages: 
```./bin/kafka-topics.sh --create --zookeeper localhost:5181 --replication-factor 1 --partitions 1 --topic pageviews```
Pageview will be the name for out topic where data coming from outside will be stored. Data that will be send, in form of JSON, will look like this:
     ```{"time": "2016-07-26T15:19:39.304Z", "url": "/foo/bar", "user": "user1", "latencyMs": 32}```
    
    
####Tranquility Server
Tranquility will take our JSON messages from the Kafka topic, parse it and put it into the Druid database.
Make sure that Tranquility is installed on configured on the mapr machine. If not please refer to [Druid Documentation](http://druid.io/docs/0.9.0/tutorials/quickstart.html) for this.
From conf-quickstart/tranquility within druid installation modify the kafka.json file to your needs, based on the JSON message that we want to ingest

```{
     "dataSources" : {
       "pageviews-kafka" : {
         "spec" : {
           "dataSchema" : {
             "dataSource" : "pageviews-kafka",
             "parser" : {
               "type" : "string",
               "parseSpec" : {
                 "timestampSpec" : {
                   "column" : "time",
                   "format" : "auto"
                 },
                 "dimensionsSpec" : {
                   "dimensions" : ["url", "user"],
                   "dimensionExclusions" : [
                     "timestamp",
                     "value"
                   ]
                 },
                 "format" : "json"
               }
             },
             "granularitySpec" : {
               "type" : "uniform",
               "segmentGranularity" : "hour",
               "queryGranularity" : "none"
             },
             "metricsSpec" : [
               {
                 "name" : "views",
                 "type" : "count", 
               },
               {
                 "fieldName" : "latencyMs",
                 "name" : "latencyMs",
                 "type" : "doubleSum"
               }
             ]
           },
           "ioConfig" : {
             "type" : "realtime"
           },
           "tuningConfig" : {
             "type" : "realtime",
             "maxRowsInMemory" : "100000",
             "intermediatePersistPeriod" : "PT10M",
             "windowPeriod" : "PT10M"
           }
         },
         "properties" : {
           "task.partitions" : "1",
           "task.replicants" : "1",
           "topicPattern" : "pageviews"
         }
       }
     },
     "properties" : {
       "zookeeper.connect" : "localhost:5181",
       "druid.discovery.curator.path" : "/druid/discovery",
       "druid.selectors.indexing.serviceName" : "druid/overlord",
       "commit.periodMillis" : "15000",
       "consumer.numThreads" : "2",
       "kafka.zookeeper.connect" : "localhost:5181",
       "kafka.group.id" : "tranquility-kafka"
     }
   }
```

Then start the tranquility server with the above configuration file.
```bin/tranquility kafka -configFile ../druid-0.9.1.1/conf-quickstart/tranquility/kafka.json```
Look for the messages in the console that shows the tranquility was indeed started
```[KafkaConsumer-1] INFO  c.metamx.emitter.core.LoggingEmitter - Start: started [true]```
