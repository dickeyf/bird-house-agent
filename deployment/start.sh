#!/bin/bash
export spring_cloud_stream_bindings_heartbeat_destination="dickeycloud/birdhouse/agentHb/v1/2"
export spring_cloud_stream_bindings_previews_destination="dickeycloud/birdhouse/previews/v1/2"
export spring_cloud_stream_bindings_picture_destination="dickeycloud/birdhouse/picture/v1/2"
export spring_cloud_stream_solace_bindings_requests_consumer_queueAdditionalSubscriptions="dickeycloud/birdhouse/requests/v1/2"
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5000 -jar `ls /home/pi/birdhouseagent/*jar`
