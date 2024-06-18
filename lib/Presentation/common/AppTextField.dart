import 'package:flutter/material.dart';

class AppTextField extends StatefulWidget {
  const AppTextField(
      {super.key,
      this.hint,
      this.icon = null,
      this.isPassword = false,
      this.obscureText = false,
      required this.label,
      required this.textInputType,
      required this.onSaved,
      required this.controller,
      this.onHidePassword});

  final String? hint;
  final String label;
  final bool isPassword;
  final bool obscureText;
  final TextInputType textInputType;
  final Icon? icon;
  final VoidCallback? onSaved;
  final VoidCallback? onHidePassword;
  final TextEditingController controller;

  @override
  State<AppTextField> createState() => _AppTextFieldState();
}

class _AppTextFieldState extends State<AppTextField> {
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          widget.label,
          style: Theme.of(context).textTheme.titleMedium,
        ),
        SizedBox(
          height: 10,
        ),
        TextFormField(
          controller: widget.controller,
          keyboardType: widget.textInputType,
          decoration: InputDecoration(
            hintText: widget.hint,
            prefixIcon: widget.icon,
            suffixIcon: widget.isPassword
                ? IconButton(
                    onPressed: widget.onHidePassword,
                    icon: Icon(widget.obscureText
                        ? Icons.visibility
                        : Icons.visibility_off))
                : null,
            hintStyle: TextStyle(color: Colors.grey.shade400),
            border:
                OutlineInputBorder(borderRadius: BorderRadius.circular(8.0)),
            fillColor: Colors.black,
          ),
          obscureText: widget.obscureText,
          style: TextStyle(color: Colors.black),
          validator: (val) =>
              val!.isEmpty ? "${widget.label} cannot be empty" : null,
          onSaved: (val) => widget.onSaved,
        ),
        SizedBox(
          height: 20,
        ),
      ],
    );
  }
}
