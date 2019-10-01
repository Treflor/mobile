import 'package:flutter/material.dart';

class CustomDropDownFormField extends StatelessWidget {
  final String hintText;
  final String labelText;
  final String errorText;
  final Function validator;
  final Function onSaved;
  final bool dark;
  final List<String> items;

  CustomDropDownFormField({
    this.hintText,
    this.errorText,
    this.labelText,
    this.validator,
    this.onSaved,
    this.dark = false,
    @required this.items,
  });

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(8),
      child: DropdownButtonFormField<String>(
        decoration: new InputDecoration(
          hintText: hintText,
          labelText: labelText,
          errorText: errorText,
          labelStyle: TextStyle(color: dark ? Colors.white54 : Colors.black45),
          filled: true,
          fillColor: dark ? Colors.white12 : Colors.black12,
          border: InputBorder.none,
        ),
        validator: validator,
        onSaved: onSaved,
        items: items
            .map(
              (item) => DropdownMenuItem(
                value: item,
                child: Text(item),
              ),
            )
            .toList(),
      ),
    );
  }
}
