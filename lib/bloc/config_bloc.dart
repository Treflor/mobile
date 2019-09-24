import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:treflor/config/treflor.dart';

class ConfigBLoC extends ChangeNotifier {
  final SharedPreferences _treflorPref = Treflor.treflorPref;
  bool _darkMode = false;

  ConfigBLoC() {
    darkMode = _treflorPref?.getBool(Treflor.DARK_MODE_KEY) ?? false;
  }

  bool get darkMode => _darkMode;

  set darkMode(bool value) {
    _darkMode = value;
    _treflorPref.setBool(Treflor.DARK_MODE_KEY, value);
    notifyListeners();
  }

  void toggleDarkMode() {
    darkMode = !darkMode;
  }
}
