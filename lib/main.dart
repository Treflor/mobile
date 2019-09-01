import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:treflor/bloc/oauth_bloc.dart';
import 'package:treflor/screens/login_screen.dart';
import 'package:treflor/screens/main_screen.dart';
import 'package:treflor/screens/splash_screen.dart';

void main() => runApp(App());

class App extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider<AuthBLoC>.value(
          value: AuthBLoC(),
        )
      ],
      child: TreflorApp(),
    );
  }
}

SplashScreen _splashScreen;
MainScreen _mainScreen;
LoginScreen _loginScreen;

Widget _screen(AuthState state) {
  switch (state) {
    case AuthState.Loading:
      if (_splashScreen == null) _splashScreen = SplashScreen();
      return _splashScreen;
    case AuthState.Authorized:
      if (_mainScreen == null) _mainScreen = MainScreen();
      return _mainScreen;
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
    print(authBLoC.authState);
    return MaterialApp(
      title: "Treflor",
      home: _screen(authBLoC.authState),
    );
  }
}
