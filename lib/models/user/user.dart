// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';

class User {
      final String? id;
      final String? email; 
      final String? firstName; 
      final String? lastName;
      final String? phoneNumber;
      final String? profile_photo;

  User({
    this.id,
    this.email,
    this.firstName,
    this.lastName,
    this.phoneNumber,
    this.profile_photo,
  });

  

  factory User.fromJson(Map<String, dynamic> map) {
    return User(
      id:  map['ID'] .toString() ,
      email:  map['email'] .toString(),
      firstName:  map['firstName'] .toString(),
      lastName:  map['lastName'] .toString(),
      phoneNumber:  map['phone'] .toString(),
      profile_photo:  map['profilePhoto'].toString(),
    );
  }

 
}
