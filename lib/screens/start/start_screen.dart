import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/screens/start/jpurney_bloc/bloc.dart';
import 'package:treflor/screens/start/my_location_bloc/index.dart';
import 'package:treflor/models/journey.dart';

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
          return _mapScreen(state, context);
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

  Widget _mapScreen(OnMyLocationState state, BuildContext context) {
    JourneyBloc journeyBloc = Provider.of<JourneyBloc>(context);
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
            child: BlocBuilder(
              bloc: journeyBloc,
              builder: (context, state) {
                if (state is InitialJourneyState || state is LoadJourneyState) {
                  return FloatingActionButton(
                    mini: true,
                    backgroundColor: Colors.white,
                    onPressed: null,
                    child: Icon(
                      FontAwesomeIcons.info,
                    ),
                  );
                }
                if (state is NoJourneyState) {
                  return FloatingActionButton(
                    mini: true,
                    backgroundColor: Colors.white,
                    onPressed: () => journeyBloc.dispatch(
                      StartJourneyEvent(
                        journey: Journey(),
                      ),
                    ),
                    child: Icon(
                      FontAwesomeIcons.hiking,
                    ),
                  );
                }
                return FloatingActionButton(
                  mini: true,
                  backgroundColor: Colors.white,
                  onPressed: () => journeyBloc.dispatch(
                    StopJourneyEvent(
                      journey: Journey(),
                    ),
                  ),
                  child: Icon(
                    FontAwesomeIcons.hourglassEnd,
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
