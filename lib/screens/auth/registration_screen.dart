import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';

import '../../bloc/oauth_bloc.dart';

class RegistrationScreen extends StatefulWidget {
  static const String route = '/registration';
  @override
  _RegistrationScreenState createState() => _RegistrationScreenState();
}

class _RegistrationScreenState extends State<RegistrationScreen> {
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _familyNameController = TextEditingController();
  final TextEditingController _givenNameController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    AuthBLoC authBLoC = Provider.of<AuthBLoC>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text("Registration"),
      ),
      body: SingleChildScrollView(
        child: Container(
          padding: EdgeInsets.all(10),
          child: Column(
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
                controller: _passwordController,
                decoration: InputDecoration(
                  labelText: "Password",
                  prefixIcon: Icon(FontAwesomeIcons.pencilAlt),
                ),
              ),
              TextField(
                controller: _givenNameController,
                decoration: InputDecoration(
                  labelText: "First Name",
                  prefixIcon: Icon(FontAwesomeIcons.pencilAlt),
                ),
              ),
              TextField(
                controller: _familyNameController,
                decoration: InputDecoration(
                  labelText: "Last name",
                  prefixIcon: Icon(FontAwesomeIcons.pencilAlt),
                ),
              ),
              RaisedButton(
                child: Row(
                  children: <Widget>[
                    Spacer(),
                    Icon(FontAwesomeIcons.userPlus),
                    SizedBox(width: 20),
                    Text("Register"),
                    Spacer(),
                  ],
                ),
                onPressed: () => authBLoC
                    .signUp(
                      _emailController.text,
                      _passwordController.text,
                      '',
                      _familyNameController.text,
                      _givenNameController.text,
                    )
                    .then((_) => Navigator.pop(context)),
              )
            ],
          ),
        ),
      ),
    );
  }
}
