import 'dart:async';

import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

class StartScreen extends StatefulWidget {
  @override
  _StartScreenState createState() => _StartScreenState();
}

class _StartScreenState extends State<StartScreen> {
  List<Position> positions = [];

  @override
  void initState() {
    super.initState();
    initLocation();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Stack(
        children: <Widget>[
          GoogleMap(
            initialCameraPosition: CameraPosition(
              target: LatLng(7.458992, 80.2479298),
              zoom: 20,
            ),
          )
        ],
      ),
    );
  }

  void initLocation() async {
    var geolocator = Geolocator();
    var locationOptions =
        LocationOptions(accuracy: LocationAccuracy.high, distanceFilter: 10);

    StreamSubscription<Position> positionStream = geolocator
        .getPositionStream(locationOptions)
        .listen((Position position) {
      print(position.toString());
      print("from subscription");
      setState(() {
        this.positions.add(position);
      });
    });

    print("position.toString()");
    Position position = await Geolocator()
        .getLastKnownPosition(desiredAccuracy: LocationAccuracy.high);
    print(position.toString());
    setState(() {
      this.positions.add(position);
    });
    position = await Geolocator()
        .getCurrentPosition(desiredAccuracy: LocationAccuracy.high);
    print(position.toString());
    setState(() {
      this.positions.add(position);
    });
  }
}
