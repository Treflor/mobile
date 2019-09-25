import 'dart:async';

import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';

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
      child: Center(
        child: positions.isNotEmpty
            ? ListView.builder(
                itemCount: positions.length,
                itemBuilder: (context, index) {
                  return ListTile(
                    title: Text(
                        "latitude: ${positions[index].latitude}, logitude: ${positions[index].longitude}"),
                  );
                },
              )
            : Text("not loaded"),
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
