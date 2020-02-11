var BirdHouseInterface = function (birdHouseId) {
    'use strict';
    var birdHouseInterface = {};
    birdHouseInterface.session = null;
    birdHouseInterface.state = BirdHouseInterface.States.DISCONNECTED;

    birdHouseInterface.connect = function(brokerUrl, msgVpnName, username, password) {
        try {
            birdHouseInterface.session = solace.SolclientFactory.createSession({
                // solace.SessionProperties
                url:      brokerUrl,
                vpnName:  msgVpnName,
                userName: username,
                password: password,
            });
        } catch (error) {
            console.log(error.toString());
        }
        birdHouseInterface.state = BirdHouseInterface.States.CONNECTING;
        // define session event listeners
        birdHouseInterface.session.on(solace.SessionEventCode.UP_NOTICE, function (sessionEvent) {
            console.log("Connected with success.  Subscribing to dickeycloud/birdhouse/picture/v1/"+birdHouseId);
            //Successfully connected and ready to subscribe.
            birdHouseInterface.session.subscribe(
                solace.SolclientFactory.createTopicDestination("dickeycloud/birdhouse/picture/v1/"+birdHouseId),
                true, // generate confirmation when subscription is added successfully
                0, // correlation key
                10000 // 10 seconds timeout for this operation
            );
            birdHouseInterface.state = BirdHouseInterface.States.SUBSCRIBING;
        });
        birdHouseInterface.session.on(solace.SessionEventCode.CONNECT_FAILED_ERROR, function (sessionEvent) {
            console.log('Connection failed to the message router: ' + sessionEvent.infoStr +
                ' - check correct parameter values and connectivity!');
        });
        birdHouseInterface.session.on(solace.SessionEventCode.DISCONNECTED, function (sessionEvent) {
            console.log('Disconnected.');
            if (birdHouseInterface.session !== null) {
                birdHouseInterface.session.dispose();
                birdHouseInterface.session = null;
            }
        });
        birdHouseInterface.session.on(solace.SessionEventCode.SUBSCRIPTION_ERROR, function (sessionEvent) {
            console.log('Cannot subscribe to topic: ' + sessionEvent.correlationKey);
        });
        birdHouseInterface.session.on(solace.SessionEventCode.SUBSCRIPTION_OK, function (sessionEvent) {
            console.log("Subscribed successfully to dickeycloud/birdhouse/picture/v1/"+birdHouseId);
            console.log("Subscribing to dickeycloud/birdhouse/picture/v1/"+birdHouseId);
            if (sessionEvent.correlationKey == 0) {
                birdHouseInterface.session.subscribe(
                    solace.SolclientFactory.createTopicDestination("dickeycloud/birdhouse/previews/v1/"+birdHouseId),
                    true, // generate confirmation when subscription is added successfully
                    1, // correlation key
                    10000 // 10 seconds timeout for this operation
                );
            } else if (sessionEvent.correlationKey == 1) {
                console.log("Subscribed successfully to dickeycloud/birdhouse/previews/v1/"+birdHouseId);
                console.log("Subscribing to dickeycloud/birdhouse/motion/v1/"+birdHouseId);
                birdHouseInterface.session.subscribe(
                    solace.SolclientFactory.createTopicDestination("dickeycloud/birdhouse/motion/v1/"+birdHouseId),
                    true, // generate confirmation when subscription is added successfully
                    2, // correlation key
                    10000 // 10 seconds timeout for this operation
                );
            } else {
                //All subscriptions are UP.  We are thus ready.
                birdHouseInterface.state = BirdHouseInterface.States.READY;
                console.log("Subscribed successfully to dickeycloud/birdhouse/motion/v1/"+birdHouseId);
                console.log("Birdhouse interface READY.");
            }
        });
        birdHouseInterface.session.on(solace.SessionEventCode.SUBSCRIPTION_OK, function (sessionEvent) {
            console.log("Subscribed successfully to dickeycloud/birdhouse/picture/v1/"+birdHouseId);
            console.log("Subscribing to dickeycloud/birdhouse/picture/v1/"+birdHouseId);
            if (sessionEvent.correlationKey == 0) {
                birdHouseInterface.session.subscribe(
                    solace.SolclientFactory.createTopicDestination("dickeycloud/birdhouse/previews/v1/"+birdHouseId),
                    true, // generate confirmation when subscription is added successfully
                    1, // correlation key
                    10000 // 10 seconds timeout for this operation
                );
            } else {
                //Both subscriptions are UP.  We are thus ready.
                birdHouseInterface.state = BirdHouseInterface.States.READY;
                console.log("Subscribed successfully to dickeycloud/birdhouse/previews/v1/"+birdHouseId);
                console.log("Birdhouse interface READY.");
            }
        });
        // define message event listener
        birdHouseInterface.session.on(solace.SessionEventCode.MESSAGE, function (message) {
            var picture = JSON.parse(message.getBinaryAttachment());

            switch (message.getDestination().getName()) {
                case "dickeycloud/birdhouse/previews/v1/1":
                    var image = document.getElementById("video");
                    image.src = "data:image/jpeg;base64,"+picture.picture;
                    break;
                case "dickeycloud/birdhouse/picture/v1/1":
                    var image = document.getElementById("picture");
                    image.src = "data:image/jpeg;base64,"+picture.picture;
                    break;
                case "dickeycloud/birdhouse/motion/v1/1":
                    var paragraph = document.getElementById("motion");
                    paragraph.innerHTML = picture.timestamp + " stdDev: " + picture.stdDev;
                    break;
            }
        });

        try {
            birdHouseInterface.session.connect();
        } catch (error) {
            console.log(error.toString());
        }
    };

    birdHouseInterface.disconnect = function () {
        console.log('Disconnecting from Solace message router...');
        if (birdHouseInterface.session !== null) {
            try {
                birdHouseInterface.session.disconnect();
            } catch (error) {
                console.log(error.toString());
            }
        } else {
            console.log('Not connected to Solace message router.');
        }
    };

    birdHouseInterface.takePicture = function() {
        if (birdHouseInterface.session !== null) {
            var messagePayload = JSON.stringify({
                requestType: "TakePicture",
                settings: {
                    quality: 100
                }
            });
            var message = solace.SolclientFactory.createMessage();
            message.setDestination(solace.SolclientFactory.createTopicDestination("dickeycloud/birdhouse/requests/v1/1"));
            message.setBinaryAttachment(messagePayload);
            message.setDeliveryMode(solace.MessageDeliveryModeType.DIRECT);
            try {
                birdHouseInterface.session.send(message);
            } catch (error) {
                console.log(error.toString());
            }
        } else {
            console.log('Cannot publish because not connected to Solace message router.');
        }
    };

    return birdHouseInterface;
};

BirdHouseInterface.States = {
    DISCONNECTED: 'Disconnected',
    CONNECTING: 'Connecting',
    SUBSCRIBING: 'Subscribing',
    READY: 'Ready'
};
