import 'package:json_annotation/json_annotation.dart';

part 'login_response_dto.g.dart';

@JsonSerializable()
class LoginResponseDTO {
  final String message;
  final User user;

  LoginResponseDTO({
    required this.message,
    required this.user,
  });

  factory LoginResponseDTO.fromJson(Map<String, dynamic> json) => _$LoginResponseDTOFromJson(json);
  Map<String, dynamic> toJson() => _$LoginResponseDTOToJson(this);
}

@JsonSerializable()
class User {
  final int ID;
  final String firstName;
  final String lastName;
  final String phone;
  final String email;
  final String password;
  final String profilePhoto;
  final String age;
  final String educationLevel;
  final bool testedBefore;
  final String gender;

  User({
    required this.ID,
    required this.firstName,
    required this.lastName,
    required this.phone,
    required this.email,
    required this.password,
    required this.profilePhoto,
    required this.age,
    required this.educationLevel,
    required this.testedBefore,
    required this.gender,
  });

  factory User.fromJson(Map<String, dynamic> json) => _$UserFromJson(json);
  Map<String, dynamic> toJson() => _$UserToJson(this);
}
