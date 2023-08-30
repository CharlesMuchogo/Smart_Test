// ignore_for_file: prefer_const_constructors

import 'dart:async';
import 'dart:io';

import 'package:chewie/chewie.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:page_transition/page_transition.dart';
import 'package:path_provider/path_provider.dart';
import 'package:research/Presentation/profile.dart';
import 'package:research/Presentation/testpage.dart';
import 'package:video_player/video_player.dart';

import '../bloc/Login/login_bloc.dart';
import '../functions/constants.dart';
import 'documentation.dart';

class Homepage extends StatefulWidget {
  Homepage({super.key});

  @override
  State<Homepage> createState() => _HomepageState();
}

class _HomepageState extends State<Homepage> {
  String firstname = HydratedBloc.storage.read("firstname") ?? "";
  VideoPlayerController? _videoPlayerController;
  ChewieController? _chewieController;


  @override
  void initState() {

    super.initState();
    _videoPlayerController = VideoPlayerController.asset('assets/video/instructions.mp4');
    _chewieController = ChewieController(
      videoPlayerController: _videoPlayerController!,
      autoPlay: false,
      looping: false,
    );


  }

  @override
  void deactivate() {
    _videoPlayerController?.pause(); // Pause the video
    _chewieController?.dispose(); // Dispose of the Chewie controller
    super.deactivate();
  }

  @override
  void dispose() {
    _videoPlayerController?.dispose();
    _chewieController?.dispose();
    super.dispose();
  }


  @override
  Widget build(BuildContext context) {
    context.read<LoginBloc>().add(CheckAuthentication(context: context));

    var hour = DateTime.now().hour;
    String greeting() {
      hour = DateTime.now().hour;

      if (hour < 12) {
        return 'Good morning';
      }
      if (hour < 17) {
        return 'Good afternoon';
      }
      return 'Good evening';
    }

    return WillPopScope(
      onWillPop: () async {
        _videoPlayerController?.pause();
        _chewieController?.dispose();
        return true;
      },
      child: Scaffold(
        appBar: AppBar(
          title: Text("Home"),
          centerTitle: true,
          automaticallyImplyLeading: false,
          elevation: 0,
          actions: [
            Padding(
              padding: EdgeInsets.only(right: 20),
              child: GestureDetector(
                onTap: () {
                  Navigator.push(
                      context,
                      PageTransition(
                          type: PageTransitionType.rightToLeft,
                          child: Profile()));
                },
                child: CircleAvatar(
                    radius: 20,
                    backgroundColor: Colors.white,
                    child: Icon(
                      Icons.person,
                      color: Colors.black,
                    )),
              ),
            )
          ],
        ),
        body: Container(
          color: Colors.white,
          child: Center(
            child: SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Padding(
                    padding: const EdgeInsets.only(top: 20, bottom: 10),
                    child: Text(
                      "${greeting()} $firstname",
                      style: Theme.of(context).textTheme.titleLarge,
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 10, bottom: 30),
                    child: Text(
                      "Please select the test type",
                      style: Theme.of(context).textTheme.bodyMedium,
                    ),
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      GestureDetector(
                        onTap: () {
                          Navigator.push(
                              context,
                              PageTransition(
                                  type: PageTransitionType.fade,
                                  child: TestPage(
                                    couples: false,
                                  ),),);},
                        child: Column(
                          children: [
                            Container(
                              height: 100,
                              width: 100,
                              decoration: BoxDecoration(color: Colors.purple.shade100,
                                  borderRadius: BorderRadius.circular(8)
                              ),
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
                        onTap: () {
                          Navigator.push(
                              context,
                              PageTransition(
                                  type: PageTransitionType.fade,
                                  child: TestPage(
                                    couples: true,
                                  ),),);
                        },
                        child: Column(
                          children: [
                            Container(
                              height: 100,
                              width: 100,
                              decoration: BoxDecoration(color: Colors.purple.shade100,
                                borderRadius: BorderRadius.circular(8)
                              ),
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
                  ),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Text(
                      "Instructions",
                      style: Theme.of(context)
                          .textTheme
                          .titleLarge!
                          .copyWith(decoration: TextDecoration.underline),
                    ),
                  ),
                  SizedBox(
                    height: 10,
                  ),
                  VideoPlayerScreen(
                    chewieController: _chewieController!,
                  ),
                  SizedBox(
                    height: 10,
                  ),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Instruction(),
                  ),

                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class VideoPlayerScreen extends StatefulWidget {
  final ChewieController chewieController;
  VideoPlayerScreen({required this.chewieController});

  @override
  _VideoPlayerScreenState createState() => _VideoPlayerScreenState();
}

class _VideoPlayerScreenState extends State<VideoPlayerScreen> {

  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width,
      height: 220,
      child: Chewie(
        controller: widget.chewieController,
      ),
    );
  }
}
