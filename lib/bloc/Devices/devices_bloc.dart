import 'dart:convert';
import 'dart:developer';
import 'package:dio/dio.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter/foundation.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';

import '../../functions/api.dart';


part 'devices_event.dart';
part 'devices_state.dart';

class DevicesBloc extends HydratedBloc<DevicesEvent, DevicesState> {
  DevicesBloc() : super(const DevicesState()) {
    on<SetDeviceLocation>(_onSetDeviceLocation);
    on<RequestAdminPermission>(_onRequestAdminPrivilage);

  }



  void _onSetDeviceLocation(SetDeviceLocation event, Emitter<DevicesState> emit) async{
    emit(state.copyWith(status: DevicesStatus.loading));
    try {
      Response response = await Api().setDeviceLocation(device_id: event.user_id, max_distance: event.max_distance, origin_longitude: event.origin_longitude, origin_latitude: event.origin_latitude);
    if (response.statusCode != 200) {
        emit(state.copyWith(status: DevicesStatus.error, message:"Could not update the device"));
      } else {

        emit(state.copyWith(status: DevicesStatus.loaded, message:"Device updates successfully" ));
      }
    } catch (e) {
      emit(state.copyWith(status: DevicesStatus.error,  message:"Could not update the device"));
    }
  }


  void _onRequestAdminPrivilage(RequestAdminPermission event, Emitter<DevicesState> emit) async{
    emit(state.copyWith(status: DevicesStatus.loading));
    try {
      Response response = await Api().RequestAdminPrivilage();
      if (response.statusCode != 200) {
        emit(state.copyWith(status: DevicesStatus.error, message:"Could not submit request"));
      } else {

        emit(state.copyWith(status: DevicesStatus.loaded, message:"Request sent successfully"));
      }
    } catch (e) {
      emit(state.copyWith(status: DevicesStatus.error,  message:"Could not submit request"));
    }
  }








  @override
  DevicesState fromJson(Map<String, dynamic> data) {
    return DevicesState.fromJson(json.encode(data));
  }

  @override
  Map<String, dynamic>? toJson(DevicesState state) {

    return null;
  }

  @override
  void onChange(Change<DevicesState> change) {
    super.onChange(change);
    debugPrint('$change');
  }
}
