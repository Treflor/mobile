class User {
  String email;
  String givenName;
  String familyName;
  String photoUrl;
  String gender;
  DateTime birthday;

  User(
      {this.email,
      this.givenName,
      this.familyName,
      this.photoUrl,
      this.gender,
      this.birthday});

  User.fromJson(dynamic json) {
    email = json["email"];
    givenName = json["given_name"];
    familyName = json['family_name'];
    photoUrl = json['photo'];
    gender = json['gender'];
    birthday = DateTime.fromMillisecondsSinceEpoch(json['birthday']);
  }

  toString() =>
      "email: $email, givenName: $givenName, familyName: $familyName, photoUrl: $photoUrl, gender:$gender, birthday:${birthday.toIso8601String()}";
}
