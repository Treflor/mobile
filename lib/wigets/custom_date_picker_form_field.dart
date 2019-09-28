import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:intl/intl.dart';

class CustomDatePickerFormField extends FormField<DateTime> {
  CustomDatePickerFormField({
    FormFieldSetter<DateTime> onSaved,
    FormFieldValidator<DateTime> validator,
    DateTime initialValue,
    @required BuildContext context,
    @required bool dark,
    @required DateFormat format,
  }) : super(
            initialValue: initialValue,
            onSaved: onSaved,
            validator: validator,
            builder: (FormFieldState<DateTime> state) {
              TextEditingController controller = TextEditingController();
              var date = state.value ?? DateTime.now();
              controller.text = format.format(date);
              return InkWell(
                child: Container(
                  padding: EdgeInsets.only(left: 10, right: 10),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(8),
                    color: dark ? Colors.white12 : Colors.black12,
                  ),
                  child: Row(
                    children: <Widget>[
                      Expanded(
                        child: TextField(
                          enabled: false,
                          controller: controller,
                          decoration: InputDecoration(
                            labelText: "Birth Day",
                            labelStyle: TextStyle(
                                color: dark ? Colors.white54 : Colors.black45),
                            border: InputBorder.none,
                          ),
                        ),
                      ),
                      Icon(FontAwesomeIcons.calendarAlt),
                    ],
                  ),
                ),
                onTap: () async {
                  var pickedDate = await showDatePicker(
                    context: context,
                    initialDate: date,
                    firstDate: DateTime(1900),
                    lastDate: date,
                  );
                  state.didChange(pickedDate);
                },
              );
            });
}
