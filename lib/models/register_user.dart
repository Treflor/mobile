import 'package:treflor/models/auth_user.dart';

class RegisterUser extends AuthUser {
  String givenName;
  String familyName;
  String password2;
  String base64Image;
  DateTime birthday;
  String gender;

  RegisterUser(this.givenName, this.familyName, email, password, this.password2,
      this.base64Image, this.birthday, this.gender)
      : super(email, password);

  RegisterUser.just() : super.just();

  Map<String, dynamic> toMap() {
    return {
      "given_name": givenName,
      "family_name": familyName,
      "email": email,
      "password": password,
      "password2": password2,
      "birthday": birthday.millisecondsSinceEpoch,
      "gender": gender,
      "photo": base64Image,
    };
  }

  AuthUser toAuthUser() => AuthUser(email, password);
}
