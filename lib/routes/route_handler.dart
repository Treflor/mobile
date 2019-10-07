import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:treflor/screens/auth/registration_screen.dart';
import 'package:treflor/screens/main_screen.dart';
import 'package:treflor/screens/auth/login_screen.dart';
import 'package:treflor/screens/settings/about/about.dart';
import 'package:treflor/screens/settings/help/help.dart';

import '../screens/auth/update_rest_screen.dart';
import '../screens/profile/index.dart';

var mainHandler = Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  return MainScreen();
});

var loginHandler = Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  return LoginScreen();
});

var profileHandler = Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  return Provider<ProfileBloc>(
    builder: (context) => ProfileBloc(),
    dispose: (context, bloc) => bloc.dispose(),
    child: ProfileScreen(),
  );
});

var signupHandler = Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  return RegistrationScreen();
});

var updateRestHandler = Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  return UpdateRestScreen();
});

var aboutHandler = Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  return AboutScreen();
});

var helpHandler = Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  return HelpScreen();
});
