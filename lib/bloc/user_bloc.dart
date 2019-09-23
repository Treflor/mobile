import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../config/treflor.dart';

class UserBLoC extends ChangeNotifier {
  SharedPreferences _treflorPref = Treflor.treflorPref;
  String _jwt = '';
  Dio _dio = Dio();

  UserBLoC() {}
  refreshJWT() {
    print("object");
    if (_treflorPref != null) {
      var token = _treflorPref.getString(Treflor.JWT_TOKEN_KEY);
      if (token != null && token.isNotEmpty) {
        _jwt = token;
      } else {
        _jwt = '';
      }
    }
    _dio.interceptors.add(
      InterceptorsWrapper(onRequest: (RequestOptions options) {
        options.headers["Authorization"] = _jwt;
        return options;
      }),
    );
  }
}
