import 'package:flutter/material.dart';
import 'package:flutter_auth_buttons/flutter_auth_buttons.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/bloc/oauth_bloc.dart';

class LoginScreen extends StatelessWidget {
  TextEditingController _emailController;
  TextEditingController _passwordController;

  LoginScreen() {
    _emailController = TextEditingController();
    _passwordController = TextEditingController();
  }

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
                    Icon(FontAwesomeIcons.doorOpen),
                    SizedBox(width: 20),
                    Text("Login"),
                    Spacer(),
                  ],
                ),
              ),
              Divider(),
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
