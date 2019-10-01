import 'dart:async';
import 'package:bloc/bloc.dart';
import './bloc.dart';

class JourneyBloc extends Bloc<JourneyEvent, JourneyState> {
  JourneyBloc() {
    dispatch(LoadJourneyEvent());
  }

  @override
  JourneyState get initialState => InitialJourneyState();

  @override
  Stream<JourneyState> mapEventToState(
    JourneyEvent event,
  ) async* {
    try {
      yield LoadJourneyState();
      yield await event.execute();
    } catch (_) {
      yield currentState;
    }
  }
}
