import 'package:flutter/material.dart';
import 'package:http/http.dart';

import '../config/treflor.dart';

class OauthBLoC extends ChangeNotifier {
  void signIn(String email, String password) async {
    Response response = await post(OAuthAPIs.SIGNIN_API, body: {
      "email": email,
      "password": password,
    });
    print(response.body);
  }
}
