import 'package:equatable/equatable.dart';

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

class OnJourneyState extends JourneyState {
  @override
  List<Object> get props => [];
}
