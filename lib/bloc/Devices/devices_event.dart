// ignore_for_file: public_member_api_docs, sort_constructors_first
part of 'devices_bloc.dart';

abstract class DevicesEvent extends Equatable {
  const DevicesEvent();

  @override
  List<Object> get props => [];
}

class DevicesInitial extends DevicesEvent {}

class Signup extends DevicesEvent {
  final String password;
  final String email;
  final String middleName;
  final String firstName;

  final String phoneNumber;

  const Signup(
      {required this.password,
      required this.email,
      required this.middleName,
      required this.firstName,

      required  this.phoneNumber});
}

class RequestAdminPermission extends DevicesEvent{}
class SetDeviceLocation extends DevicesEvent {
  final double  max_distance;
  final String  user_id;
  final double  origin_longitude;
  final double  origin_latitude;

  SetDeviceLocation({required this.max_distance, required this.user_id, required this.origin_longitude,
    required this.origin_latitude});
}

class GetDevices extends DevicesEvent{

}

