import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:treflor/bloc/config_bloc.dart';
import 'package:treflor/bloc/oauth_bloc.dart';
import 'package:treflor/config/treflor.dart';
import 'package:treflor/screens/auth/login_screen.dart';
import 'package:treflor/screens/main_screen.dart';
import 'package:treflor/screens/splash_screen.dart';

import 'screens/auth/login_screen.dart';
import 'screens/main_screen.dart';
import 'screens/auth/registration_screen.dart';

Future<void> main() async {
  Treflor.treflorPref = await SharedPreferences.getInstance();
  runApp(App());
}

class App extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider<AuthBLoC>.value(
          value: AuthBLoC(),
        ),
        ChangeNotifierProvider<ConfigBLoC>.value(
          value: ConfigBLoC(),
        ),
      ],
      child: TreflorApp(),
    );
  }
}

LoginScreen _loginScreen;

Widget _screen(AuthState state) {
  switch (state) {
    case AuthState.Loading:
      return SplashScreen();
    case AuthState.Authorized:
      return MainScreen();

    case AuthState.Unauthorized:
      if (_loginScreen == null) _loginScreen = LoginScreen();
      return _loginScreen;
    case AuthState.Error:
      // TODO: create and erro screen when something went wrong
      if (_loginScreen == null) _loginScreen = LoginScreen();
      return _loginScreen;
  }
}

class TreflorApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    AuthBLoC authBLoC = Provider.of<AuthBLoC>(context);
    ConfigBLoC configBLoC = Provider.of<ConfigBLoC>(context);
    return MaterialApp(
      title: "Treflor",
      theme: ThemeData(
        brightness: configBLoC.darkMode ? Brightness.dark : Brightness.light,
      ),
      home: _screen(authBLoC.authState),
      routes: {
        LoginScreen.route: (context) => LoginScreen(),
        RegistrationScreen.route: (context) => RegistrationScreen(),
        MainScreen.route: (context) => MainScreen(),
      },
    );
  }
}
