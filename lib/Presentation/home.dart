// ignore_for_file: prefer_const_constructors

import 'package:chewie/chewie.dart';
import 'package:flutter/material.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:page_transition/page_transition.dart';
import 'package:research/Presentation/profile.dart';
import 'package:research/Presentation/testpage.dart';
import 'package:video_player/video_player.dart';

import '../functions/constants.dart';

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
                                )));
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
                  videoUrl: 'assets/video/instructions.mp4',
                ),
                SizedBox(
                  height: 10,
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(INSTRUCTIONS),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Image.asset( 'assets/images/pic1.png'),
                ),

                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Image.asset( 'assets/images/pic1.png'),
                ),

              ],
            ),
          ),
        ),
      ),
    );
  }
}

class VideoPlayerScreen extends StatefulWidget {
  final String videoUrl;

  VideoPlayerScreen({required this.videoUrl});

  @override
  _VideoPlayerScreenState createState() => _VideoPlayerScreenState();
}

class _VideoPlayerScreenState extends State<VideoPlayerScreen> {
  late VideoPlayerController _videoPlayerController;
  late ChewieController _chewieController;

  @override
  void initState() {
    super.initState();
    _videoPlayerController = VideoPlayerController.asset(widget.videoUrl);
    _chewieController = ChewieController(
      videoPlayerController: _videoPlayerController,
      autoPlay: false,
      looping: false,
    );
  }

  @override
  void dispose() {
    _videoPlayerController.dispose();
    _chewieController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width,
      height: 220,
      child: Chewie(
        controller: _chewieController,
      ),
    );
  }
}
