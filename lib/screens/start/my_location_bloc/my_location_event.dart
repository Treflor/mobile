import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'index.dart';

abstract class MyLocationEvent extends Equatable {
  const MyLocationEvent();
  Future<MyLocationState> execute();
}

class OnMyLocationEvent extends MyLocationEvent {
  final Position position;
  OnMyLocationEvent({@required this.position});
  @override
  List<Object> get props => [position];

  @override
  Future<MyLocationState> execute(
      {@required MyLocationState currentState}) async {
        print(position.toString());
    Marker myLocationMarker = Marker(
        markerId: MarkerId("my-location"),
        position: LatLng(position.latitude, position.longitude),
        icon: await BitmapDescriptor.fromAssetImage(
            ImageConfiguration(size: Size(20, 20)), "assets/icons/user.png"));
    if (currentState is OnMyLocationState) {
      currentState.markers.add(myLocationMarker);
      return currentState;
    }

    Set<Marker> markers = Set();
    markers.add(myLocationMarker);
    return OnMyLocationState(markers: markers);
  }
}
