import 'package:json_annotation/json_annotation.dart';

part 'UpdateUserDTO.g.dart';

@JsonSerializable()
class UpdateUserDTO {
  final String age;
  final String educationLevel;
  final bool testedBefore;
  final String gender;

  UpdateUserDTO({
    required this.age,
    required this.educationLevel,
    required this.testedBefore,
    required this.gender,
  });

  factory UpdateUserDTO.fromJson(Map<String, dynamic> json) => _$UpdateUserDTOFromJson(json);
  Map<String, dynamic> toJson() => _$UpdateUserDTOToJson(this);
}
