import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

import 'my_location_bloc/index.dart';

class StartScreen extends StatefulWidget {
  @override
  _StartScreenState createState() => _StartScreenState();
}

class _StartScreenState extends State<StartScreen> {
  MyLocationBloc _myLocationBloc = MyLocationBloc();

  @override
  Widget build(BuildContext context) {
    return BlocBuilder(
      builder: (context, state) {
        print(state);
        print("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        if (state is OnMyLocationState) {
          return Container(
            child: Stack(
              children: <Widget>[
                GoogleMap(
                  initialCameraPosition: CameraPosition(
                    target: LatLng(7.458992, 80.2479298),
                    zoom: 15,
                  ),
                  markers: state.markers,
                ),
              ],
            ),
          );
        } else {
          return Container(
            child: Text("No Map "),
          );
        }
      },
      bloc: _myLocationBloc,
    );
  }

  @override
  void dispose() {
    _myLocationBloc.dispose();
    super.dispose();
  }
}
