import 'package:equatable/equatable.dart';
import 'package:flutter/widgets.dart';
import 'package:treflor/models/journey.dart';

abstract class JourneyState extends Equatable {
  const JourneyState();
}

class InitialJourneyState extends JourneyState {
  @override
  List<Object> get props => [];
}

class NoJourneyState extends JourneyState {
  @override
  List<Object> get props => [];
}

class LoadJourneyState extends JourneyState {
  @override
  List<Object> get props => [];
}

class OnJourneyState extends JourneyState {
  final Journey journey;

  OnJourneyState({@required this.journey});

  @override
  List<Object> get props => [journey];
}
