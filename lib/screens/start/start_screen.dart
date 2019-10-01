import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
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
        if (state is OnMyLocationState) {
          return _mapScreen(state);
        } else {
          return Container(
            child: Center(
              child: CircularProgressIndicator(),
            ),
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

  Widget _mapScreen(OnMyLocationState state) {
    return Container(
      child: Stack(
        children: <Widget>[
          GoogleMap(
            
            initialCameraPosition: CameraPosition(
              target: LatLng(7.658992, 80.6479298),
              zoom: 15,
            ),
            markers: state.markers,
          ),
          Positioned(
            right: 10,
            bottom: 10,
            child: FloatingActionButton(
              mini: true,
              backgroundColor: Colors.white,
              onPressed: null,
              child: Icon(
                FontAwesomeIcons.hiking,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
