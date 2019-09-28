class TResponse {
  bool success;

  TResponse(dynamic json) {
    this.success = json['success'];
  }
}
