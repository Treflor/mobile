import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:treflor/state/auth_state.dart';
import 'package:treflor/state/config_state.dart';
import 'package:treflor/treflor_app.dart';
import 'screens/start/jpurney_bloc/bloc.dart';

void main() => runApp(
      MultiProvider(
        providers: [
          ChangeNotifierProvider<AuthState>.value(
            value: AuthState(),
          ),
          ChangeNotifierProvider<ConfigState>.value(
            value: ConfigState(),
          ),
          Provider<JourneyBloc>(
            builder: (context) => JourneyBloc(),
            dispose: (context, bloc) => bloc.dispose(),
          ),
        ],
        child: TreflorApp(),
      ),
    );
