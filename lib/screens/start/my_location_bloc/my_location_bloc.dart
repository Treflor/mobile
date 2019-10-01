import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:geolocator/geolocator.dart';
import "index.dart";

class MyLocationBloc extends Bloc<MyLocationEvent, MyLocationState> {
  StreamSubscription<Position> _positionStream;

  MyLocationBloc() {
    var geolocator = Geolocator();
    var locationOptions =
        LocationOptions(accuracy: LocationAccuracy.high, distanceFilter: 10);

    _positionStream = geolocator
        .getPositionStream(locationOptions)
        .listen((Position position) {
      dispatch(OnMyLocationEvent(position: position));
       print("stream dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
       
    });

    Geolocator()
        .getLastKnownPosition(desiredAccuracy: LocationAccuracy.high)
        .then((position) {
      dispatch(OnMyLocationEvent(position: position));
    });
    Geolocator()
        .getCurrentPosition(desiredAccuracy: LocationAccuracy.high)
        .then((position) {
      dispatch(OnMyLocationEvent(position: position));
    });
  }

  @override
  MyLocationState get initialState => InitialMyLocationState();

  @override
  Stream<MyLocationState> mapEventToState(
    MyLocationEvent event,
  ) async* {
    try {
      if (event is OnMyLocationEvent) {
        yield await event.execute(currentState: currentState);
      }
    } catch (e) {
      yield currentState;
    }
  }

  @override
  void dispose() {
    _positionStream.cancel();
    super.dispose();
  }
}
