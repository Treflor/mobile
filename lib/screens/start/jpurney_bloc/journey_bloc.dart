import 'dart:async';
import 'package:bloc/bloc.dart';
import './bloc.dart';

class JourneyBloc extends Bloc<JourneyEvent, JourneyState> {
  @override
  JourneyState get initialState => InitialJourneyState();

  @override
  Stream<JourneyState> mapEventToState(
    JourneyEvent event,
  ) async* {
    // TODO: Add Logic
  }
}
