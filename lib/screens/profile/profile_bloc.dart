import 'package:bloc/bloc.dart';
import 'package:treflor/data/repository.dart';
import 'index.dart';

class ProfileBloc extends Bloc<ProfileEvent, ProfileState> {
  Repository _repository;
  ProfileBloc() : _repository = Repository() {
    dispatch(LocaleProfileEvent());
    dispatch(RemoteProfileEvent());
  }

  @override
  ProfileState get initialState => NoProfileState();

  @override
  Stream<ProfileState> mapEventToState(
    ProfileEvent event,
  ) async* {
    try {
      if (event is LocaleProfileEvent) {
        yield LoadProfileState();
      }
      yield await event.execute(repository: _repository);
    } catch (e) {
      yield currentState;
    }
  }
}
