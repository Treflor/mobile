import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:treflor/screens/auth/registration_screen.dart';
import 'package:treflor/screens/main_screen.dart';
import 'package:treflor/screens/auth/login_screen.dart';
import 'package:treflor/screens/profile/profile.dart';

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
  return ProfileScreen();
});

var signupHandler = Handler(
    handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  return RegistrationScreen();
});
