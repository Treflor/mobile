import 'package:flutter/material.dart';
import 'package:flutter_auth_buttons/flutter_auth_buttons.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/bloc/oauth_bloc.dart';

import 'registration_screen.dart';

class LoginScreen extends StatelessWidget {
  static const String route = '/login';

  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    AuthBLoC authBLoC = Provider.of<AuthBLoC>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text("Trefor"),
      ),
      body: SingleChildScrollView(
        child: Container(
          padding: EdgeInsets.all(10),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              TextField(
                keyboardType: TextInputType.emailAddress,
                controller: _emailController,
                decoration: InputDecoration(
                  labelText: "Email",
                  prefixIcon: Icon(FontAwesomeIcons.pencilAlt),
                ),
              ),
              TextField(
                obscureText: true,
                controller: _passwordController,
                decoration: InputDecoration(
                  labelText: "password",
                  prefixIcon: Icon(
                    FontAwesomeIcons.pencilAlt,
                  ),
                  suffixIcon: Icon(
                    FontAwesomeIcons.eye,
                  ),
                ),
              ),
              RaisedButton(
                onPressed: () => authBLoC.signIn(
                    _emailController.text, _passwordController.text),
                child: Row(
                  children: <Widget>[
                    Spacer(),
                    Icon(FontAwesomeIcons.signInAlt),
                    SizedBox(width: 20),
                    Text("Login"),
                    Spacer(),
                  ],
                ),
              ),
              Divider(),
              RaisedButton(
                onPressed: () =>
                    Navigator.pushNamed(context, RegistrationScreen.route),
                child: Row(
                  children: <Widget>[
                    Spacer(),
                    Icon(FontAwesomeIcons.userPlus),
                    SizedBox(width: 20),
                    Text("Register"),
                    Spacer(),
                  ],
                ),
              ),
              GoogleSignInButton(
                onPressed: () => authBLoC.googleSignIn(),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
