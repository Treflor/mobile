import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'package:treflor/state/config_bloc.dart';
import 'package:treflor/state/oauth_bloc.dart';
import 'package:treflor/config/treflor.dart';
import 'package:treflor/screens/auth/login_screen.dart';
import 'package:treflor/screens/main_screen.dart';
import 'package:treflor/screens/splash_screen.dart';
import 'package:treflor/screens/auth/registration_screen.dart';

import 'screens/profile/profile.dart';
import 'package:treflor/treflor_app.dart';

Future<void> main() async {
  Treflor.treflorPref = await SharedPreferences.getInstance();
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider<AuthBLoC>.value(
          value: AuthBLoC(),
        ),
        ChangeNotifierProvider<ConfigBLoC>.value(
          value: ConfigBLoC(),
        ),
      ],
      child: TreflorApp(),
    ),
  );
}
