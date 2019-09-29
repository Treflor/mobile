import 'package:equatable/equatable.dart';
import 'package:flutter/foundation.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

abstract class MyLocationState extends Equatable {
  const MyLocationState();
}

class NoMyLocationState extends MyLocationState {
  @override
  List<Object> get props => null;
}

class OnMyLocationState extends MyLocationState {
  final Set<Marker> markers;
  OnMyLocationState({@required this.markers});
  @override
  List<Object> get props => [markers];
}
