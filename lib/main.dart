// ignore_for_file: prefer_const_constructors_in_immutables, prefer_const_constructors

import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:path_provider/path_provider.dart';
import 'package:research/bloc/Results/results_bloc.dart';

import 'auth/authentication_wrapper.dart';
import 'bloc/Login/login_bloc.dart';


void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  HydratedBloc.storage = await HydratedStorage.build(
    storageDirectory: await getApplicationDocumentsDirectory(),
  );

  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'Research',
        theme: ThemeData(
          // useMaterial3: true,
          primarySwatch: Colors.purple,
        ),
        home: MultiBlocProvider(
          providers: [
            BlocProvider(
              create: (context) => ResultsBloc(),
            ),
            BlocProvider(
              create: (context) => LoginBloc(),
            ),
          ],
          child: AuthenticationWrapper(),
        )
    );
  }
}
