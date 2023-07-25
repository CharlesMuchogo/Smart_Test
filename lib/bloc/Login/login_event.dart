// ignore_for_file: public_member_api_docs, sort_constructors_first
part of 'login_bloc.dart';

abstract class LoginEvent extends Equatable {
  const LoginEvent();

  @override
  List<Object> get props => [];
}

class LoginInitial extends LoginEvent {}

class Signup extends LoginEvent {
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

class updateProfile extends LoginEvent{
final String  id;
final String  email;
final String  first_name;
final String  middle_name;
final String  phone_number;

final String  password;
final String  profile_photo;

updateProfile({required this.id, required this.email, required this.first_name,required  this.middle_name,
  required this.phone_number,  required this.password, required this.profile_photo});
}



class GetLogin extends LoginEvent {
  final String email;

  final String password;

  GetLogin({
    required this.email,
    required this.password,
  });
}
