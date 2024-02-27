// ignore_for_file: prefer_const_constructors, avoid_single_cascade_in_expression_statements, prefer_const_literals_to_create_immutables

import 'dart:developer';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:research/auth/signup.dart';
import '../Presentation/BottomNavigationBar.dart';
import '../bloc/Login/login_bloc.dart';

class Login extends StatefulWidget {
  const Login({Key? key}) : super(key: key);

  @override
  State<Login> createState() => _LoginState();
}

class _LoginState extends State<Login> {
  late String password, email;
  final formKey = GlobalKey<FormState>();
  bool _obscureText = true;


  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => LoginBloc(),
      child: Scaffold(
        body: Container(
          padding: EdgeInsets.all(20),
          child: Center(
            child: Form(
              key: formKey,
              child: SingleChildScrollView(
                child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text(
                        "Sign In",
                        style: Theme.of(context)
                            .textTheme
                            .headlineMedium!
                            .copyWith(fontWeight: FontWeight.bold),
                      ),
                      SizedBox(
                        height: 50,
                      ),
                      TextFormField(
                        keyboardType: TextInputType.emailAddress,
                        decoration: InputDecoration(
                          labelText: "Email",
                          labelStyle: TextStyle(color: Colors.grey),
                          border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(15.0)),
                          fillColor: Colors.black,
                        ),
                        style: TextStyle(color: Colors.black),
                        validator: (val) => !val!.contains("@")
                            ? "Enter a valid email address"
                            : null,
                        onChanged: (value) {
                          email = value;
                        },
                        onSaved: (val) => email = val!,
                      ),
                      Padding(padding: EdgeInsets.all(15)),
                      TextFormField(
                        keyboardType: TextInputType.visiblePassword,

                        decoration: InputDecoration(
                          labelText:"Password",
                          labelStyle: TextStyle(color: Colors.grey),
                          suffixIcon: GestureDetector(
                              onTap: () {
                                setState(() {
                                  _obscureText = !_obscureText;
                                });
                              },
                              child: _obscureText == true
                                  ? Icon(Icons.visibility)
                                  : Icon(Icons.visibility_off)),
                          border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(15.0)),
                          fillColor: Colors.black,
                        ),
                        style: TextStyle(color: Colors.black),
                        //controller: password,
                        validator: (val) =>
                            val!.length < 6 ? "Password is too short" : null,
                        onChanged: (value) {
                          password = value;
                        },
                        onSaved: (val) => password = val!,
                        obscureText: _obscureText,
                      ),
                      Padding(
                        padding: EdgeInsets.all(15),
                      ),
                      BlocConsumer<LoginBloc, LoginState>(
                        listener: (context, state) {
                          if (state.status == LoginStatus.success) {
                            Navigator.pushReplacement(
                                context,
                                MaterialPageRoute(
                                    builder: (BuildContext context) =>
                                        BottomNavigation() ));
                          }

                          if (state.status == LoginStatus.failed) {
                            final snackBar = SnackBar(
                              content: Text(state.message),
                              backgroundColor: (Colors.red),
                              action: SnackBarAction(
                                label: '',
                                onPressed: () {},
                              ),
                            );
                            ScaffoldMessenger.of(context)
                                .showSnackBar(snackBar);
                          }

                          if (state.status == LoginStatus.error) {
                            final snackBar = SnackBar(
                              content: Text(
                                  "Login failed! Try again"),
                              backgroundColor: (Colors.red),
                              action: SnackBarAction(
                                label: '',
                                onPressed: () {},
                              ),
                            );
                            ScaffoldMessenger.of(context)
                                .showSnackBar(snackBar);
                          }
                        },
                        builder: (context, state) {
                          return BlocBuilder<LoginBloc, LoginState>(
                            builder: (context, state) {
                              if (state.status == LoginStatus.loading) {
                                return Center(
                                  child: CircularProgressIndicator(),
                                );
                              }

                              return GestureDetector(
                                onTap: () async {
                                  if (!formKey.currentState!.validate()) {
                                    return;
                                  }

                                  log(email + password);
                                  context.read<LoginBloc>().add(
                                        GetLogin(
                                          email: email.trim().toLowerCase(),
                                          password: password,
                                        ),
                                      );
                                },
                                child: Container(
                                  alignment: Alignment.center,
                                  height: 50,
                                  width: double.infinity,
                                  decoration: BoxDecoration(
                                      color: Theme.of(context).primaryColor,
                                      borderRadius: BorderRadius.all(
                                  Radius.circular(5.0) //
                                ),
                                  ),
                                  child: Text(
                                      "Log In",
                                    style: TextStyle(fontSize: 18, color: Colors.white),
                                  ),
                                ),
                              );
                            },
                          );
                        },
                      ),
                      Padding(padding: EdgeInsets.all(20)),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(
                            "Don't have an account? ",
                            style: Theme.of(context)
                                .textTheme
                                .bodyMedium!
                                .copyWith(
                                fontSize: 19,
                                ),
                          ),
                          GestureDetector(
                            onTap: () {
                              Navigator.of(context).push(MaterialPageRoute(
                                  builder: (context) => SignUp()));
                            },
                            child: Text(
                              "Sign up",
                              style: Theme.of(context)
                                  .textTheme
                                  .bodyMedium!
                                  .copyWith(
                                      fontSize: 19,
                                      color: Theme.of(context).primaryColor),
                            ),
                         ),
                        ],
                      ),
                      Padding(padding: EdgeInsets.all(20)),
                    ]),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
