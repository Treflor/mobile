import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'package:treflor/state/oauth_bloc.dart';
import 'package:treflor/treflor_app.dart';

import 'state/config_bloc.dart';

void main() => runApp(
      MultiProvider(
        providers: [
          ChangeNotifierProvider<AuthBLoC>.value(
            value: AuthBLoC(),
          ),
          ChangeNotifierProvider<ConfigBLoC>.value(
            value: ConfigBLoC(),
          ),
        ],
        child: TreflorApp(),
      ),
    );
