import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/screens/start/jpurney_bloc/bloc.dart';
import 'package:treflor/screens/start/my_location_bloc/index.dart';
import 'package:treflor/models/journey.dart';
import 'package:flutter/services.dart' show rootBundle;
import 'package:treflor/state/config_state.dart';

class StartScreen extends StatefulWidget {
  @override
  _StartScreenState createState() => _StartScreenState();
}

class _StartScreenState extends State<StartScreen> {
  MyLocationBloc _myLocationBloc = MyLocationBloc();
  GoogleMapController _mapController;

  void _changeMapTheme(mode) async {
    if (mode) {
      _mapController.setMapStyle(
          await rootBundle.loadString("assets/jsons/map_dark.json"));
    } else {
      _mapController.setMapStyle("[]");
    }
  }

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
            onMapCreated: (GoogleMapController controller) {
              _mapController = controller;
              _changeMapTheme(Provider.of<ConfigState>(context).darkMode);
            },
            initialCameraPosition: CameraPosition(
              target: LatLng(7.658992, 80.6479298),
              zoom: 10,
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
                      color: Colors.black,
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
                      color: Colors.black,
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
                    color: Colors.black,
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
