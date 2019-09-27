class AuthResponse {
  String token;

  AuthResponse(dynamic json) {
    this.token = json['token'];
  }
}
