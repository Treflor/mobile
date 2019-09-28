import 'package:flutter/material.dart';

class CustomDropDownFormField extends StatefulWidget {
  final String hintText;
  final String labelText;
  final String errorText;
  final Function validator;
  final Function onSaved;
  final bool dark;
  final Map<String, IconData> items;

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
  _CustomDropDownFormFieldState createState() =>
      _CustomDropDownFormFieldState();
}

class _CustomDropDownFormFieldState extends State<CustomDropDownFormField> {
  dynamic _value;
  IconData _icon;

  onSelect(dynamic selected) {
    setState(() {
      _value = selected;
      _icon = widget.items[selected];
    });
  }

  @override
  void initState() {
    super.initState();
    _value = widget.items.keys.toList()[0];
    _icon = widget.items.values.toList()[0];
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 10),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(8),
        color: widget.dark ? Colors.white12 : Colors.black12,
      ),
      child: Row(
        children: <Widget>[
          Expanded(
            child: DropdownButtonFormField(
              decoration: new InputDecoration(
                hintText: widget.hintText,
                labelText: widget.labelText,
                errorText: widget.errorText,
                labelStyle: TextStyle(
                    color: widget.dark ? Colors.white54 : Colors.black45),
                border: InputBorder.none,
              ),
              validator: widget.validator,
              onSaved: widget.onSaved,
              items: widget.items.keys
                  .map(
                    (item) => DropdownMenuItem(
                      value: item,
                      child: Text(item),
                    ),
                  )
                  .toList(),
              onChanged: onSelect,
              value: _value,
            ),
          ),
          Icon(_icon)
        ],
      ),
    );
  }
}
