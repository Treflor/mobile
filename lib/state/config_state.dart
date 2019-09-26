import 'package:flutter/material.dart';

import '../data/repository.dart';

class ConfigState extends ChangeNotifier {
  final Repository _repository = Repository();

  bool _darkMode = false;

  ConfigState() {
    _repository.getDarkMode().then((mode) {
      this._darkMode = mode;
      notifyListeners();
    });
  }

  bool get darkMode => _darkMode;

  Future<void> toggleDarkMode() {
    return _repository.toggleDarkMode(_darkMode).then((mode) {
      _darkMode = mode;
      notifyListeners();
    });
  }
}
