import 'package:treflor/models/auth_user.dart';

class RegisterUser extends AuthUser {
  String givenName;
  String familyName;
  String password2;

  RegisterUser(this.givenName, this.familyName, email, password, this.password2)
      : super(email, password);

  RegisterUser.just() : super.just();

  Map<String, dynamic> toMap() {
    return {
      "givenName": givenName,
      "familyName": familyName,
      "email": email,
      "password": password,
      "password2": password2,
    };
  }
}
