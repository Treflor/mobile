import 'package:flutter/material.dart';
import 'package:flutter_signin_button/flutter_signin_button.dart';
import 'package:provider/provider.dart';
import 'package:treflor/state/auth_state.dart';
import 'package:treflor/models/auth_user.dart';
import 'package:treflor/wigets/custom_text_form_field.dart';
import 'package:treflor/routes/application.dart';
import 'package:treflor/state/config_state.dart';

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
    ConfigState configState = Provider.of<ConfigState>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text("Treflor"),
      ),
      body: SingleChildScrollView(
        child: Container(
          padding: EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              Text(
                "Sign In",
                style: Theme.of(context).textTheme.headline,
              ),
              SizedBox(
                height: 16,
              ),
              ClipOval(
                child: Image.asset(
                  "assets/images/profile.jpg",
                  height: 120,
                  width: 120,
                  fit: BoxFit.cover,
                ),
              ),
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
                        dark: configState.darkMode),
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
                      dark: configState.darkMode,
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    RaisedButton(
                      elevation: 0,
                      color: Theme.of(context).buttonColor,
                      colorBrightness: configState.darkMode
                          ? Brightness.dark
                          : Brightness.light,
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
              SignInButton(
                Buttons.GoogleDark,
                onPressed: () => !_onProcess ? _signInWithGoogle() : null,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
              ),
              FlatButton(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
                padding: EdgeInsets.symmetric(vertical: 16.0, horizontal: 20),
                child: Text("or Create an account?"),
                onPressed: _onCreateAccount,
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
    if (!RegExp(
            r"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?)*$")
        .hasMatch(email)) {
      return "Email not in valid format";
    }
    return null;
  }

  String _validatePassword(String password) {
    if (password.isEmpty) {
      return "Password can't be empty";
    }
    if (password.length < 4) {
      return "Password lenght must be more than 4";
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

  void _onCreateAccount() {
    Application.router.navigateTo(context, "/signup");
  }
}
