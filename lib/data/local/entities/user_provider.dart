import 'package:treflor/models/user.dart';
import 'package:shared_preferences/shared_preferences.dart';

class UserProvider {
  Future<void> insert(User user) {
    return SharedPreferences.getInstance().then((pref) {
      pref.setString("user-email", user.email);
      pref.setString("user-given-name", user.givenName);
      pref.setString("user-family-name", user.familyName);
      pref.setString("user-photo-url", user.photoUrl);
      pref.setString("user-gender", user.gender);
      pref.setInt("user-birthday", user.birthday.millisecondsSinceEpoch);
    });
  }

  Future<User> getUser() {
    return SharedPreferences.getInstance().then((pref) {
      User user = User();
      user.email = pref.getString("user-email");
      user.givenName = pref.getString("user-given-name");
      user.familyName = pref.getString("user-family-name");
      user.photoUrl = pref.getString("user-photo-url");
      user.gender = pref.getString("user-gender");
      user.birthday =
          DateTime.fromMillisecondsSinceEpoch(pref.getInt("user-birthday"));
      if (user.email == null) return null;
      return user;
    });
  }

  Future<void> delete() {
    return SharedPreferences.getInstance().then((pref) {
      pref.setString("user-email", null);
      pref.setString("user-given-name", null);
      pref.setString("user-family-name", null);
      pref.setString("user-photo-url", null);
      pref.setString("user-gender", null);
      pref.setInt("user-birthday", null);
    });
  }
}
