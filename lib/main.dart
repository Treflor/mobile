import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:treflor/bloc/oauth_bloc.dart';
import 'package:treflor/screens/login_screen.dart';

void main() => runApp(App());

class App extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider<OauthBLoC>.value(
          value: OauthBLoC(),
        )
      ],
      child: TreflorApp(),
    );
  }
}

class TreflorApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(title: "Treflor", home: LoginScreen());
  }
}
