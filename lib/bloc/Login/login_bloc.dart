import 'dart:convert';
import 'dart:developer';

import 'package:dio/dio.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:research/models/dto/UpdateUserDTO.dart';
import 'package:research/models/dto/login_response_dto.dart';

import '../../functions/api.dart';

part 'login_event.dart';
part 'login_state.dart';

class LoginBloc extends HydratedBloc<LoginEvent, LoginState> {
  LoginBloc() : super(const LoginState()) {
    on<GetLogin>(_onGetLogin);
    on<LoginInitial>(_onGetLoginStatus);
    on<updateProfile>(_onUpdateProfile);
    on<UpdateDetails>(_onUpdateDetails);
    on<Signup>(onSignup);
    on<CheckAuthentication>(onCheckLogin);
  }

  void _onUpdateProfile(updateProfile event, Emitter<LoginState> emit) async {
    emit(state.copyWith(status: LoginStatus.loading));
    try {
      Response loginDetails = await Api().updateProfile(
          id: event.id,
          email: event.email,
          first_name: event.first_name,
          middle_name: event.middle_name,
          phone_number: event.phone_number,
          password: event.password,
          profile_photo: event.profile_photo);

      var jsonBody = loginDetails.data;

      if (loginDetails.statusCode != 200) {
        emit(state.copyWith(
            status: LoginStatus.failed, message: jsonBody["message"]));
      } else {
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.fromJson(jsonBody);
        User user = loginResponseDTO.user;

        await HydratedBloc.storage.write("token", jsonBody['user']);
        await HydratedBloc.storage.write("firstname", user.firstName);
        await HydratedBloc.storage.write("email", user.email);
        await HydratedBloc.storage.write("id", user.ID);
        await HydratedBloc.storage.write("lastName", user.lastName);
        await HydratedBloc.storage.write("phonenumber", user.phone);
        await HydratedBloc.storage.write("status", true);
        await HydratedBloc.storage.write("profile_photo", user.profilePhoto);

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

  void onCheckLogin(CheckAuthentication event, Emitter<LoginState> emit) async {
    if (state.status == LoginStatus.initial) {
      emit(state.copyWith(status: LoginStatus.loading));
    }

    try {
      print("Checking status");
      http.Response response =
          await Api().checkAuthenticationStatus(context: event.context);
    } catch (e) {
      emit(state.copyWith(status: LoginStatus.error));
    }
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
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.fromJson(jsonBody);
        User user = loginResponseDTO.user;

        await HydratedBloc.storage.write("token", jsonBody['token']);
        await HydratedBloc.storage.write("firstname", user.firstName);
        await HydratedBloc.storage.write("email", user.email);
        await HydratedBloc.storage.write("id", user.ID);
        await HydratedBloc.storage.write("lastName", user.lastName);
        await HydratedBloc.storage.write("phonenumber", user.phone);
        await HydratedBloc.storage.write("status", true);
        await HydratedBloc.storage.write("profile_photo", user.profilePhoto);

        emit(state.copyWith(
            status: LoginStatus.success,
            message: jsonBody["message"],
            loggedIn: true));
      }
    } on DioException catch (e) {
      emit(state.copyWith(
          status: LoginStatus.error, message: e.response?.data["message"]));
    } catch (e) {
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

      if (loginDetails.statusCode != 201) {
        emit(state.copyWith(
            status: LoginStatus.failed, message: jsonBody["message"]));
      } else {
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.fromJson(jsonBody);
        User user = loginResponseDTO.user;



        await HydratedBloc.storage.write("token", jsonBody['token']);
        await HydratedBloc.storage.write("firstname", user.firstName);
        await HydratedBloc.storage.write("email", user.email);
        await HydratedBloc.storage.write("id", user.ID);
        await HydratedBloc.storage.write("lastName", user.lastName);
        await HydratedBloc.storage.write("phonenumber", user.phone);
        await HydratedBloc.storage.write("status", true);
        await HydratedBloc.storage.write("profile_photo", user.profilePhoto);
        await HydratedBloc.storage.write("user", user.toJson());

        emit(state.copyWith(
            status: LoginStatus.success,
            message: jsonBody["message"],
            loggedIn: true));
      }
    } on DioException catch (e) {
      emit(state.copyWith(
          status: LoginStatus.error, message: e.response?.data["message"]));
    } catch (e) {
      emit(state.copyWith(status: LoginStatus.error, message: e.toString()));
    }
  }

  void _onUpdateDetails(UpdateDetails event, Emitter<LoginState> emit) async {
    emit(state.copyWith(status: LoginStatus.loading));

    try {
      Response response = await Api().updateDetails(
        userDTO: event.userDTO,
      );

      var jsonBody = response.data;

      if (response.statusCode != 200) {
      } else {
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.fromJson(jsonBody);
        User user = loginResponseDTO.user;

        await HydratedBloc.storage.write("age", user.age);
        await HydratedBloc.storage.write("gender", user.gender);
        await HydratedBloc.storage.write("testedBefore", user.testedBefore);
        await HydratedBloc.storage.write("educationLevel", user.educationLevel);

        emit(
          state.copyWith(
            status: LoginStatus.success,
            message: loginResponseDTO.message,
          ),
        );
      }
    } on DioException catch (e) {
      emit(state.copyWith(
          status: LoginStatus.failed, message: e.response?.data["message"]));
    } catch (e) {
      emit(state.copyWith(status: LoginStatus.failed, message: e.toString()));
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
