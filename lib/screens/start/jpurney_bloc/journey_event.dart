import 'package:equatable/equatable.dart';
import 'package:flutter/foundation.dart';
import 'package:treflor/models/journey.dart';
import 'package:treflor/data/repository.dart';
import 'bloc.dart';

abstract class JourneyEvent extends Equatable {
  const JourneyEvent();
  Future<JourneyState> execute();
}

class StartJourneyEvent extends JourneyEvent {
  final Journey journey;
  final Repository _repository;
  StartJourneyEvent({@required this.journey}) : _repository = Repository();

  @override
  List<Object> get props => [journey, _repository];

  @override
  Future<JourneyState> execute() {
    return _repository.startJourney(journey: journey).then((_) {
      return OnJourneyState(journey: journey);
    });
  }
}

class LoadJourneyEvent extends JourneyEvent {
  final Repository _repository;
  LoadJourneyEvent() : _repository = Repository();

  @override
  List<Object> get props => [_repository];

  @override
  Future<JourneyState> execute() {
    return _repository.getJourney().then((journey) {
      return journey == null
          ? NoJourneyState()
          : OnJourneyState(journey: journey);
    });
  }
}

class StopJourneyEvent extends JourneyEvent {
  final Journey journey;
  final Repository _repository;
  StopJourneyEvent({@required this.journey}) : _repository = Repository();

  @override
  List<Object> get props => [_repository];

  @override
  Future<JourneyState> execute() {
    return _repository.endJourney(journey: journey).then((_) {
      return NoJourneyState();
    });
  }
}
