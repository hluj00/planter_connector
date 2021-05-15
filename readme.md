# Smart planter - planter_connector

This repository is part of a smart planter project that consists of three parts.
 - [planter_planter](https://github.com/hluj00/planter_planter)
 - [planter_web](https://github.com/hluj00/planter_web)
 - [planter_connector](https://github.com/hluj00/planter_connector)

planter_connector is a MQTT client for the planter_web app

## Configuration

planter_connector should connect to the same MySQL database as planter_web
planter_connector should connect to the same MQTT broker as planter_planter

both MySQL and MQTT connection can be setup in src/main/java/application/Application.java
## Installation


make sure to configure MySQL and MQTT connections

project can be compiled with maven by command
```bash
mvn clean compile assembly:single
```
jar file can be used as a linux sercice defined by file planter_connector.service
```bash
cp planter_connector.service /etc/systemd/system/ && systemctl enable planter_connector.service
```
and run by
```bash
systemctl start planter_connector.service
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)