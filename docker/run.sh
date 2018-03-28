#!/bin/sh

echo "********************************************************"
echo "Waiting for the eureka server to start on port $DISCOVERY_SERVICE_PORT"
echo "********************************************************"
while ! `nc -z discovery-service $DISCOVERY_SERVICE_PORT`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the config server to start on port $CONFIG_SERVICE_PORT"
echo "********************************************************"
while ! `nc -z config-service $CONFIG_SERVICE_PORT`; do sleep 3; done
echo "******* Config Server has started"

echo "********************************************************"
echo "Waiting for the kafka server to start on port $KAFKA_SERVER_PORT"
echo "********************************************************"
while ! `nc -z kafka-server $KAFKA_SERVER_PORT`; do sleep 10; done
echo "******* Kafka Server has started"

echo "********************************************************"
echo "Starting Server with Configuration Service via Eureka :  $DISCOVERY_SERVICE_URI:$SERVER_PORT"
echo "Using Kafka Server: $KAFKA_SERVER_URI"
echo "Using ZK    Server: $ZOOKEEPER_SERVER_URI"
echo "USing Profile: $PROFILE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$DISCOVERY_SERVICE_URI             \
     -Dspring.cloud.config.uri=$CONFIG_SERVICE_URI                          \
     -Dspring.cloud.stream.kafka.binder.zkNodes=$KAFKA_SERVER_URI          \
     -Dspring.cloud.stream.kafka.binder.brokers=$ZOOKEEPER_SERVER_URI             \
     -Dspring.profiles.active=$PROFILE  \
     -DSPRING_PROFILES_ACTIVE=$PROFILE        \
     -jar /usr/local/target/@project.build.finalName@.jar