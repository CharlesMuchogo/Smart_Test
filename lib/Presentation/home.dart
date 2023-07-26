// ignore_for_file: prefer_const_constructors

import 'package:flutter/material.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:page_transition/page_transition.dart';
import 'package:research/Presentation/profile.dart';
import 'package:research/Presentation/testpage.dart';


class Homepage extends StatelessWidget {
  Homepage({super.key});

  String firstname = HydratedBloc.storage.read("firstname") ?? "";

  @override
  Widget build(BuildContext context) {
    var hour = DateTime.now().hour;
    String greeting() {
      hour = DateTime.now().hour;

      if (hour < 12) {
        return 'Good Morning';
      }
      if (hour < 17) {
        return 'Good Afternoon';
      }
      return 'Good Evening';
    }

    return Scaffold(
      appBar: AppBar(
        title: Text("Home"),
        centerTitle: true,
        elevation: 0,
        actions:  [
          Padding(
            padding: EdgeInsets.only(right: 20),
            child: GestureDetector(
              onTap: (){
                Navigator.push(context, PageTransition(type: PageTransitionType.rightToLeft, child:  Profile()));
                },
              child: CircleAvatar(
                  radius: 20,
                  backgroundColor: Colors.white,
                  child: Icon(Icons.person, color: Colors.black,)),
            ),
          )
        ],
      ),
      body: Container(
        color: Colors.white,
        child: Center(
            child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            Padding(
              padding: const EdgeInsets.only(top: 30, bottom: 30),
              child: Text(
                "${greeting()} $firstname",
                style: Theme.of(context).textTheme.titleLarge,
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 30, bottom: 50),
              child: Text(
                "Please select the test type",
                style: Theme.of(context).textTheme.bodyMedium,
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                GestureDetector(
                  onTap: (){
                    Navigator.push(context, PageTransition(type: PageTransitionType.fade, child: TestPage()));

                  },
                  child: Column(
                    children: [
                      Container(
                        height: 100,
                        width: 100,
                        color: Colors.purple.shade100,
                        alignment: Alignment.center,
                        child: Icon(
                          Icons.person,
                          size: 50,
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text("Individual"),
                      )
                    ],
                  ),
                ),
                GestureDetector(
                  onTap: (){
                    Navigator.push(context, PageTransition(type: PageTransitionType.fade, child: TestPage()));
                  },
                  child: Column(
                    children: [
                      Container(
                        height: 100,
                        width: 100,
                        color: Colors.purple.shade100,
                        alignment: Alignment.center,
                        child: Icon(
                          Icons.people,
                          size: 50,
                        ),
                      ),

                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text("Couple"),
                      )
                    ],
                  ),
                ),
              ],
            )
          ],
        ),),
      ),
    );
  }
}
