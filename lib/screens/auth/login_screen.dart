import 'package:flutter/material.dart';
import 'package:flutter_auth_buttons/flutter_auth_buttons.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/state/auth_state.dart';
import 'package:treflor/models/auth_user.dart';
import 'package:treflor/wigets/custom_text_form_field.dart';
import 'package:treflor/screens/auth/registration_screen.dart';
import 'package:treflor/routes/application.dart';

class LoginScreen extends StatefulWidget {
  static const String route = '/login';

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  GlobalKey<FormState> _loginFormKey = GlobalKey();

  bool _onProcess = false;
  AuthUser _user = AuthUser.just();

  @override
  Widget build(BuildContext context) {
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
              Form(
                key: _loginFormKey,
                child: Column(
                  children: <Widget>[
                    SizedBox(
                      height: 16,
                    ),
                    CustomTextFormField(
                      labelText: "Email",
                      onSaved: (String value) {
                        _user.email = value;
                      },
                      validator: _validateEmail,
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    CustomTextFormField(
                      labelText: "Password",
                      isObscure: true,
                      onSaved: (String value) {
                        _user.password = value;
                      },
                      validator: _validatePassword,
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    RaisedButton(
                      elevation: 0,
                      color: Theme.of(context).buttonColor,
                      colorBrightness: Brightness.dark,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                      padding: EdgeInsets.symmetric(vertical: 16.0),
                      child:
                          _onProcess ? Text("Signing In...") : Text("Sign In"),
                      onPressed: _onProcess ? null : _onSubmit,
                    ),
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
                onPressed: _signInWithGoogle,
              ),
            ],
          ),
        ),
      ),
    );
  }

  String _validateEmail(String email) {
    if (email.isEmpty) {
      return "Email can't be empty";
    }
    return null;
  }

  String _validatePassword(String password) {
    if (password.isEmpty) {
      return "Password can't be empty";
    }
    return null;
  }

  void _onSubmit() {
    if (_loginFormKey.currentState.validate()) {
      _loginFormKey.currentState.save();

      setState(() {
        _onProcess = true;
      });
      Provider.of<AuthState>(context).signin(_user).then((success) {
        if (success) {
          Application.router.pop(context);
        } else {
          setState(() {
            _onProcess = false;
          });
        }
      }).catchError((err) {
        setState(() {
          _onProcess = false;
        });
        print(err.toString());
        // Scaffold.of(context).showSnackBar(SnackBar(
        //   content: Text("Sign in failed!"),
        // ));
      });
    }
  }

  void _signInWithGoogle() async {
    setState(() {
      _onProcess = true;
    });

    Provider.of<AuthState>(context).signInWithGoogle().then((success) {
      if (success) {
        Application.router.pop(context);
      } else {
        setState(() {
          _onProcess = false;
        });
      }
    }).catchError((err) {
      setState(() {
        _onProcess = false;
      });
      print(err.toString());
    });
  }
}
