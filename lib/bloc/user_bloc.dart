import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../config/treflor.dart';

class UserBLoC extends ChangeNotifier {
  final SharedPreferences _treflorPref = Treflor.treflorPref;
  dynamic _user;

  UserBLoC() {
    user = _treflorPref?.getBool(Treflor.DARK_MODE_KEY) ?? false;
  }

  dynamic get user => _user;

  set user(dynamic user) {
    _user = user;
    _treflorPref.setString(Treflor.USER_KEY, user.toString());
    notifyListeners();
  }

  void toggleDarkMode() {}
}
