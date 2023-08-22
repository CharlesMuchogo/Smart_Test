import 'dart:convert';
import 'package:equatable/equatable.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';

import '../../functions/api.dart';


part 'results_event.dart';
part 'results_state.dart';

class ResultsBloc extends HydratedBloc<ResultsEvent, ResultsState> {
  ResultsBloc() : super(const ResultsState()) {
    on<UploadResults>(_onUploadResults);
  }

  void _onUploadResults(UploadResults event, Emitter<ResultsState> emit) async{
    emit(state.copyWith(status: ResultsStatus.loading));
    try {
      Response response = await Api().uploadResults(careOption: event.careOption, context: event.context, results: event.results, partnerResults: event.partnerResults, image: event.image, partnerImage: event.partnerImage);
      if (response.statusCode != 200) {
        emit(state.copyWith(status: ResultsStatus.error, message:"Could not submit request"));
      } else {
        emit(state.copyWith(status: ResultsStatus.loaded, message:"Request sent successfully"));
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
