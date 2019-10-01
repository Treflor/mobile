import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:treflor/models/journey.dart';

class JourneyProvider {
  static const JOURNEY_STATE = "journey-state";

  //just initialize for now
  Future<void> start({@required Journey journey}) {
    return SharedPreferences.getInstance().then((pref) {
      // save the journey details
    });
  }

  Future<void> end() {
    return SharedPreferences.getInstance().then((pref) {
      // delete the journey details
    });
  }

  Future<void> make() {
    return SharedPreferences.getInstance().then((pref) {
      pref.setBool(JOURNEY_STATE, true);
    });
  }

  Future<void> unmake() {
    return SharedPreferences.getInstance().then((pref) {
      pref.setBool(JOURNEY_STATE, null);
    });
  }

  Future<bool> getState() {
    return SharedPreferences.getInstance().then((pref) {
      return pref.getBool(JOURNEY_STATE) ?? false;
    });
  }

  Future<Journey> getJourney() {
    return SharedPreferences.getInstance().then((pref) {
      Journey journey = Journey();
      //read journey
      if (pref.getBool(JOURNEY_STATE) == null) return null;
      return journey;
    });
  }
}
