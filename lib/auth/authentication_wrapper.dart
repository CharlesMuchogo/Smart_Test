// ignore_for_file: prefer_const_constructors
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:research/auth/demographic_data.dart';

import '../Presentation/BottomNavigationBar.dart';
import 'Login.dart';

class AuthenticationWrapper extends StatefulWidget {
  const AuthenticationWrapper({Key? key}) : super(key: key);

  @override
  State<AuthenticationWrapper> createState() => _AuthenticationWrapperState();
}

class _AuthenticationWrapperState extends State<AuthenticationWrapper> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return HydratedBloc.storage.read("token") == null
        ? Login():
        HydratedBloc.storage.read("age") == null ?
        DemographicDataPage()
        : BottomNavigation();
  }
}
