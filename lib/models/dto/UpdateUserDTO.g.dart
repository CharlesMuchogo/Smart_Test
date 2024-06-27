// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'UpdateUserDTO.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UpdateUserDTO _$UpdateUserDTOFromJson(Map<String, dynamic> json) =>
    UpdateUserDTO(
      age: json['age'] as String,
      educationLevel: json['educationLevel'] as String,
      testedBefore: json['testedBefore'] as bool,
      gender: json['gender'] as String,
    );

Map<String, dynamic> _$UpdateUserDTOToJson(UpdateUserDTO instance) =>
    <String, dynamic>{
      'age': instance.age,
      'educationLevel': instance.educationLevel,
      'testedBefore': instance.testedBefore,
      'gender': instance.gender,
    };
