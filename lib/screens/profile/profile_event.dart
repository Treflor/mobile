import 'package:equatable/equatable.dart';
import 'package:flutter/foundation.dart';
import 'index.dart';
import 'package:treflor/data/repository.dart';

abstract class ProfileEvent extends Equatable {
  const ProfileEvent();
  Future<ProfileState> execute({@required Repository repository});
}

class LocaleProfileEvent extends ProfileEvent {
  @override
  Future<ProfileState> execute({@required Repository repository}) async {
    return repository
        .usersInfoLocal()
        .then((user) => InProfileState(user))
        .catchError((err) => NoProfileState());
  }

  @override
  List<Object> get props => null;
}

class RemoteProfileEvent extends ProfileEvent {
  @override
  Future<ProfileState> execute({@required Repository repository}) async {
    return repository
        .usersInfo()
        .then((user) => InProfileState(user))
        .catchError((err) => NoProfileState());
  }

  @override
  List<Object> get props => null;
}
