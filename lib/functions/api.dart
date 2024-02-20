// ignore_for_file: non_constant_identifier_names, unused_local_variable

import 'dart:convert';
import 'dart:developer';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:http/http.dart' as http;
import 'package:research/models/dto/resultsDTO.dart';
import '../auth/authentication_wrapper.dart';
import 'package:http_parser/http_parser.dart';
import 'app_functions.dart';
import 'constants.dart';

class Api {
  final dio = Dio();
  Future<Response> login({
    required String email,
    required String password,
  }) async {
    const url = "$BASEURL/api/login";
    var data = {
      "email": email,
      "password": password,
    };
    Response response = await dio.post(
      url,
      data: data,
    );
    return response;
  }

  Future<Response> updateProfile({
required String  id,
required String  email,
required String  first_name,
required String  middle_name,
required String  phone_number,
required String  password,
required String  profile_photo,
  }) async {
    String user_id = HydratedBloc.storage.read("id") ;
    const url = "$BASEURL/api/user";
    log(url);
    var data ={
      "id":int.parse(id),
      "email":email,
      "first_name":first_name,
      "middle_name":middle_name,
      "phone_number":phone_number,
      "profile_photo":profile_photo
    };

    Response response = await dio.post(
      url,
      data: data,
    );

    return response;
  }

  Future<Response> uploadTest(
      {required ResultsDTO resultsDTO }) async {
  final data = FormData.fromMap({
      "results": resultsDTO.results,
      "partner_results": resultsDTO.partnerResults,
      "care_option": resultsDTO.careOption,
      if (resultsDTO.resultsPhoto != null)
        "user_photo": await MultipartFile.fromFile(
          resultsDTO.resultsPhoto!.path,
          contentType: MediaType('image', 'png'),
        ),
      if (resultsDTO.partnerResultsPhoto != null)
        "partner_photo": await MultipartFile.fromFile(
          resultsDTO.partnerResultsPhoto!.path,
          contentType: MediaType('image', 'png'),
        ),
    });

  var dio = Dio();

  return dio.post("$BASEURL/api/mobile/upload",
      options: Options(headers: {
        "Authorization": HydratedBloc.storage.read("token"),
        "Accept": "application/json",
      }),
      data: data);

  }

  Future<http.Response>  uploadResults(
      {required results,
        required partnerResults,
        required careOption,
        required image,
        required partnerImage,
        required BuildContext context
      }) async {
    const url = "$BASEURL/api/mobile/upload";
    var data = {
      "results":results,
      "partnerResults":partnerResults,
      "image":image,
      "partnerImage":partnerImage,
      "care_option":careOption

    };

    http.Response response = await http.post(Uri.parse(url),
        body: jsonEncode(data),
        headers: {
    "Authorization": HydratedBloc.storage.read("token")
    } );


    if(response.statusCode == 401){
      AppFunctions().snackbar(context, "Your session expired please log in again", Colors.red);
      await HydratedBloc.storage.clear();
      Navigator.pushAndRemoveUntil(
          context,
          MaterialPageRoute(
              builder: (BuildContext context) =>
                  const AuthenticationWrapper()),
              (Route<dynamic> route) => false);
    }

    return response;
  }


  Future<http.Response>  checkAuthenticationStatus(
      {
        required BuildContext context
      }) async {
    const url = "$BASEURL/api/mobile/check_authentication_status";


    http.Response response = await http.post(Uri.parse(url),
        headers: {
    "Authorization": HydratedBloc.storage.read("token") ?? "notAuthenticated"
    } );


    if(response.statusCode == 401){
      AppFunctions().snackbar(context, "Your session expired please log in again", Colors.red);
      await HydratedBloc.storage.clear();
      Navigator.pushAndRemoveUntil(
          context,
          MaterialPageRoute(
              builder: (BuildContext context) =>
                  const AuthenticationWrapper()),
              (Route<dynamic> route) => false);
    }

    return response;
  }

  Future<Response> signup({
    required String password,
    required String email,
    required String middleName,
    required String firstName,
    required String phoneNumber,
  }) async {
    const url = "$BASEURL/api/user/register";

    var data = {
      "email": email,
      "firstName": firstName,
      "lastName": middleName,
      "phone": phoneNumber,
      "password": password,
      "profilePhoto":"https://firebasestorage.googleapis.com/v0/b/matibabu-1254d.appspot.com/o/images%2F%7Bcharlesmuchogo07%40gmail.com%7D?alt=media&token=63d32dda-9250-4569-ba04-d5dd010a41d4"
    };



    Response response = await dio.post(
      url,
      data: data,
    );
    return response;
  }
}


