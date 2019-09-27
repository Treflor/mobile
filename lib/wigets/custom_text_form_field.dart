import 'package:flutter/material.dart';

class CustomTextFormField extends StatelessWidget {
  final String hintText;
  final String labelText;
  final String errorText;
  final bool isObscure;
  final TextCapitalization textCapitalization;
  final TextInputType keyboardType;
  final Function validator;
  final Function onSaved;
  final bool dark;

  CustomTextFormField({
    this.hintText,
    this.errorText,
    this.isObscure = false,
    this.textCapitalization = TextCapitalization.none,
    this.keyboardType = TextInputType.text,
    this.labelText,
    this.validator,
    this.onSaved,
    this.dark = false,
  });

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(8),
      child: TextFormField(
        decoration: new InputDecoration(
          hintText: hintText,
          labelText: labelText,
          errorText: errorText,
          labelStyle: TextStyle(color: dark ? Colors.white54 : Colors.black45),
          filled: true,
          fillColor: dark ? Colors.white12 : Colors.black12,
          border: InputBorder.none,
        ),
        textCapitalization: textCapitalization,
        keyboardType: keyboardType,
        obscureText: isObscure,
        validator: validator,
        onSaved: onSaved,
      ),
    );
  }
}
