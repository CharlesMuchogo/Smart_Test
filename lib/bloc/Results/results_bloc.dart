import 'dart:convert';
import 'package:dio/dio.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:research/models/dto/resultsDTO.dart';

import '../../auth/authentication_wrapper.dart';
import '../../functions/api.dart';
import '../../functions/app_functions.dart';


part 'results_event.dart';
part 'results_state.dart';

class ResultsBloc extends HydratedBloc<ResultsEvent, ResultsState> {
  ResultsBloc() : super(const ResultsState()) {
    on<UploadResults>(_onUploadResults);
  }

  void _onUploadResults(UploadResults event, Emitter<ResultsState> emit) async{
    emit(state.copyWith(status: ResultsStatus.loading));
    try {
      Response response = await Api().uploadTest(resultsDTO: event.resultsDTO);
      if (response.statusCode != 200) {
        emit(state.copyWith(status: ResultsStatus.error, message:"Could not submit request"));
      } else {
        emit(state.copyWith(status: ResultsStatus.loaded, message:"Request sent successfully"));
      }
    } on DioException catch (e){
      if(e.response?.statusCode == 401){
        AppFunctions().snackbar(event.resultsDTO.context, "Your session expired please log in again", Colors.red);
        await HydratedBloc.storage.clear();
        Navigator.pushAndRemoveUntil(
            event.resultsDTO.context,
            MaterialPageRoute(
                builder: (BuildContext context) =>
                const AuthenticationWrapper()),
                (Route<dynamic> route) => false);
        emit(state.copyWith(status: ResultsStatus.error,  message:"Your session expired"));

      }else{
        emit(state.copyWith(status: ResultsStatus.error,  message:"An error occurred while submitting results"));
      }

    } catch (e) {
      emit(state.copyWith(status: ResultsStatus.error,  message:"Could not submit request"));
    }
  }

  @override
  ResultsState fromJson(Map<String, dynamic> data) {
    return ResultsState.fromJson(json.encode(data));
  }

  @override
  Map<String, dynamic>? toJson(ResultsState state) {

    return null;
  }

  @override
  void onChange(Change<ResultsState> change) {
    super.onChange(change);
    debugPrint('$change');
  }
}
