class User {
  String email;
  String givenName;
  String familyName;
  String photoUrl;

  User({
    this.email,
    this.givenName,
    this.familyName,
    this.photoUrl,
  });

  User.fromJson(dynamic json) {
    email = json["email"];
    givenName = json["given_name"];
    familyName = json['family_name'];
    photoUrl = json['photo'];
  }
}
