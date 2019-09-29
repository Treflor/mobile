import 'package:equatable/equatable.dart';
import 'package:treflor/models/user.dart';

abstract class ProfileState extends Equatable {
  const ProfileState();
}

class NoProfileState extends ProfileState {
  @override
  List<Object> get props => null;
}

class InProfileState extends ProfileState {
  final User user;
  InProfileState(this.user);
  @override
  List<Object> get props => [user];
}

class LoadProfileState extends ProfileState {
  @override
  List<Object> get props => null;
}
