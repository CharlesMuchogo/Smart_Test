import 'dart:convert';
import 'dart:developer';
import 'package:dio/dio.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter/foundation.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';

import '../../functions/api.dart';
import '../../models/user/user.dart';

part 'login_event.dart';
part 'login_state.dart';

class LoginBloc extends HydratedBloc<LoginEvent, LoginState> {
  LoginBloc() : super(const LoginState()) {
    on<GetLogin>(_onGetLogin);
    on<LoginInitial>(_onGetLoginStatus);
    on<updateProfile>(_onUpdateProfile);
    on<Signup>(onSignup);
  }

  void _onUpdateProfile(updateProfile event, Emitter<LoginState> emit) async {
    emit(state.copyWith(status: LoginStatus.loading));
    try {
      Response loginDetails = await Api().updateProfile(id: event.id, email: event.email, first_name: event.first_name, middle_name: event.middle_name, phone_number: event.phone_number, password: event.password, profile_photo: event.profile_photo);

      var jsonBody = loginDetails.data;

      if (loginDetails.statusCode != 200) {
        emit(state.copyWith(
            status: LoginStatus.failed, message: jsonBody["message"]));
      } else {
        User user = User.fromJson(jsonBody['user']);
        HydratedBloc.storage.write("token", jsonBody['user']);
        HydratedBloc.storage.write("firstname", user.firstName);
        HydratedBloc.storage.write("email", user.email);
        HydratedBloc.storage.write("id", user.id);
        HydratedBloc.storage.write("lastName", user.lastName);
        HydratedBloc.storage.write("phonenumber", user.phoneNumber);
        HydratedBloc.storage.write("status", true);
        HydratedBloc.storage.write("profile_photo", user.profile_photo);

        emit(state.copyWith(
            status: LoginStatus.success,
            message: jsonBody["message"],
            loggedIn: true));
      }
    } catch (e) {
      log(e.toString());
      emit(state.copyWith(status: LoginStatus.error, message: e.toString()));
    }


  }
  void _onGetLoginStatus(LoginInitial event, Emitter<LoginState> emit) {
    emit(state.copyWith(status: LoginStatus.initial));
  }

  void _onGetLogin(GetLogin event, Emitter<LoginState> emit) async {
    emit(state.copyWith(status: LoginStatus.loading));
    try {
      Response loginDetails = await Api().login(
        email: event.email,
        password: event.password,
      );

      var jsonBody = loginDetails.data;

      if (loginDetails.statusCode != 200) {
        emit(state.copyWith(
            status: LoginStatus.failed, message: jsonBody["message"]));
      } else {
        User user = User.fromJson(jsonBody['user']);

        HydratedBloc.storage.write("token", jsonBody['token']);
        HydratedBloc.storage.write("firstname", user.firstName);
        HydratedBloc.storage.write("email", user.email);
        HydratedBloc.storage.write("id", user.id);
        HydratedBloc.storage.write("lastName", user.lastName);
        HydratedBloc.storage.write("phonenumber", user.phoneNumber);
        HydratedBloc.storage.write("status", true);
        HydratedBloc.storage.write("profile_photo", user.profile_photo);

        emit(state.copyWith(
            status: LoginStatus.success,
            message: jsonBody["message"],
            loggedIn: true));
      }
    } catch (e) {
      log(e.toString());
      emit(state.copyWith(status: LoginStatus.error, message: e.toString()));
    }
  }



  void onSignup(Signup event, Emitter<LoginState> emit) async {
    emit(state.copyWith(status: LoginStatus.loading));
    try {
      Response loginDetails = await Api().signup(
          password: event.password,
          email: event.email,
          middleName: event.middleName,
          firstName: event.firstName,
          phoneNumber: event.phoneNumber);

      

      var jsonBody = loginDetails.data;

      if (loginDetails.statusCode != 200) {
        emit(state.copyWith(
            status: LoginStatus.failed, message: jsonBody["message"]));
      } else {
        User user = User.fromJson(jsonBody['user']);

        HydratedBloc.storage.write("token", jsonBody['token']);
        HydratedBloc.storage.write("firstname", user.firstName);
        HydratedBloc.storage.write("email", user.email);
        HydratedBloc.storage.write("id", user.id);
        HydratedBloc.storage.write("lastName", user.lastName);
        HydratedBloc.storage.write("phonenumber", user.phoneNumber);
        HydratedBloc.storage.write("status", true);
        HydratedBloc.storage.write("profile_photo", user.profile_photo);

        emit(state.copyWith(
            status: LoginStatus.success,
            message: jsonBody["message"],
            loggedIn: true));
      }
    } catch (e) {
      log(e.toString());
      emit(state.copyWith(status: LoginStatus.error, message: e.toString()));
    }


  }

  @override
  LoginState fromJson(Map<String, dynamic> data) {
    return LoginState.fromJson(json.encode(data));
  }

  @override
  Map<String, dynamic>? toJson(LoginState state) {
    if (state.status == LoginStatus.loaded ||
        state.status == LoginStatus.success) {
      return state.toMap();
    }
    return null;
  }

  @override
  void onChange(Change<LoginState> change) {
    super.onChange(change);
    debugPrint('$change');
  }
}
