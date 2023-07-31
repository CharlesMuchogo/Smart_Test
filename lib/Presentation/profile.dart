// ignore_for_file: public_member_api_docs, sort_constructors_first
// ignore_for_file: prefer_const_constructors

import 'dart:async';
import 'dart:io';

import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:image_cropper/image_cropper.dart';
import 'package:image_picker/image_picker.dart';
import 'package:research/auth/authentication_wrapper.dart';

import '../bloc/Login/login_bloc.dart';
import '../functions/constants.dart';
import 'AccountDetails.dart';

class Profile extends StatefulWidget {
  @override
  State<Profile> createState() => _ProfileState();

  const Profile({super.key});
}

class _ProfileState extends State<Profile> {
  String password = "";
  var dropdownValue = null;

  var imageFile;
  var profileBloc;

  var imageUrl;
  String dummyProfile =
      "https://firebasestorage.googleapis.com/v0/b/dietx-cbb19.appspot.com/o/mainfood%2Fpasta%26beef.jpeg?alt=media&token=f1b36807-0d06-454e-81b8-0e4210085780";

  String firstname = HydratedBloc.storage.read("firstname");
  String email = HydratedBloc.storage.read("email");
  String id = HydratedBloc.storage.read("id");
  String middleName = HydratedBloc.storage.read("lastName");
  String phonenumber = HydratedBloc.storage.read("phonenumber");
  String profile_photo = HydratedBloc.storage.read("profile_photo") ??
      "https://firebasestorage.googleapis.com/v0/b/dietx-cbb19.appspot.com/o/mainfood%2Fpasta%26beef.jpeg?alt=media&token=f1b36807-0d06-454e-81b8-0e4210085780";

  late String currentDefaultSystemLocale;
  int selectedLangIndex = 0;

  _pickImage(String imagename, ImageSource source) async {
    var pictureFile =
        await ImagePicker.platform.getImageFromSource(source: source);

    final firebasestorage = FirebaseStorage.instance;

    if (pictureFile!.path.isNotEmpty) {
      showDialog(
          context: context,
          builder: (context) {
            return Center(
              child: CircularProgressIndicator(),
            );
          });
      var croppedImage = await ImageCropper.platform.cropImage(
        sourcePath: pictureFile.path,
        aspectRatio: CropAspectRatio(ratioX: 1, ratioY: 1),
        uiSettings: [
          AndroidUiSettings(
              toolbarColor: Colors.purple,
              toolbarWidgetColor: Colors.white,
              initAspectRatio: CropAspectRatioPreset.original,
              lockAspectRatio: false)
        ],
        compressQuality: 100,
        maxHeight: 700,
        maxWidth: 700,
        compressFormat: ImageCompressFormat.jpg,
      );
      setState(() {
        imageFile = File(croppedImage!.path);
      });
      //Upload to Firebase
      var snapshot = await firebasestorage
          .ref()
          .child('images/{$imagename}')
          .putFile(imageFile);
      var downloadUrl = await snapshot.ref.getDownloadURL();
      setState(() {
        imageUrl = downloadUrl;
      });

      profileBloc.add(updateProfile(
          id: id,
          email: email,
          first_name: firstname,
          middle_name: middleName,
          phone_number: phonenumber,
          password: password,
          profile_photo: imageUrl));

      Navigator.pop(context);
    } else {
      print('No Image Path Received');
    }
  }

  Future imagepickerdialogue(BuildContext context, String imageName) {
    return showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text("Choose the image source"),
          content: SingleChildScrollView(
            child: ListBody(
              children: [
                InkWell(
                  onTap: () {
                    Navigator.of(context)
                        .pop(_pickImage(imageName, ImageSource.gallery));
                  },
                  child: Text("Gallery"),
                ),
                Padding(padding: EdgeInsets.all(8)),
                InkWell(
                  onTap: () {
                    Navigator.of(context)
                        .pop(_pickImage(imageName, ImageSource.camera));
                  },
                  child: Text("Camera"),
                )
              ],
            ),
          ),
        );
      },
    );
  }

  Widget displayImage(String profileUrl) {
    return CircleAvatar(
      backgroundColor: Colors.grey,
      backgroundImage: NetworkImage(profileUrl),
      radius: 80, //Image.file(imageFile),
      //radius: 70,
    );
  }

  Widget profilepictureview(String url) {
    return Image.network(url);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Profile"),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.only(top: 20, left: 10, right: 10),
        child: BlocProvider(
          create: (context) => LoginBloc(),
          child: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                InkWell(
                  onTap: () {
                    showDialog(
                        context: context,
                        builder: (context) {
                          return AlertDialog(
                            backgroundColor: Colors.grey[600],
                            content: profilepictureview(profile_photo),
                          );
                        });
                  },
                  child: BlocBuilder<LoginBloc, LoginState>(
                    builder: (context, state) {
                      profileBloc = BlocProvider.of<LoginBloc>(context);
                      firstname = HydratedBloc.storage.read("firstname");
                      email = HydratedBloc.storage.read("email");
                      id = HydratedBloc.storage.read("id");
                      middleName = HydratedBloc.storage.read("lastName");
                      phonenumber = HydratedBloc.storage.read("phonenumber");
                      profile_photo = HydratedBloc.storage
                              .read("profile_photo") ??
                          "https://www.pngitem.com/pimgs/m/30-307416_profile-icon-png-image-free-download-searchpng-employee.png";

                      return Stack(
                        children: [
                          displayImage(profile_photo),
                          Container(
                            margin: EdgeInsets.only(
                                left: MediaQuery.of(context).size.width * 0.28,
                                top:
                                    MediaQuery.of(context).size.height * 0.125),
                            child: CircleAvatar(
                              backgroundColor: Colors.teal,
                              radius: 25,
                              child: InkWell(
                                onTap: () {
                                  imagepickerdialogue(context, email);
                                },
                                child: Icon(
                                  Icons.camera_alt_outlined,
                                  size: 25,
                                  color: Colors.white,
                                ),
                              ),
                            ),
                          )
                        ],
                      );
                    },
                  ),
                ),
                SizedBox(
                  height: 20,
                ),
                Text(
                  "$firstname $middleName",
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
                SizedBox(
                  height: 10,
                ),
                Text(email),
                SizedBox(
                  height: 20,
                ),
                Divider(),
                AccountDetails(
                  title: "Email",
                  subtitle: email,
                ),
                AccountDetails(
                    title: "Name", subtitle: "$firstname $middleName"),
                AccountDetails(title: "PhoneNumber", subtitle: phonenumber),
                SizedBox(
                  height: 20,
                ),
                ListTile(
                  leading: Icon(Icons.settings_power),
                  title: Text("Log Out"),
                  onTap: () async {
                    AppConstants().alertDialog(
                        context: context,
                        content: Text("Are you sure you want to log out?"),
                        title: "Log out",
                        onpress: () async {
                          await HydratedBloc.storage.clear();
                          Navigator.pushAndRemoveUntil(
                              context,
                              MaterialPageRoute(
                                  builder: (BuildContext context) =>
                                      AuthenticationWrapper()),
                              (Route<dynamic> route) => false);
                        });
                  },
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
