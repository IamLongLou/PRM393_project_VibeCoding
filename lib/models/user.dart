class User {
  final String username;
  final String fullName;
  final String role;
  final String? email;
  final String? phone;

  User({
    required this.username,
    required this.fullName,
    required this.role,
    this.email,
    this.phone,
  });

  Map<String, dynamic> toMap() {
    return {
      'username': username,
      'fullName': fullName,
      'role': role,
      'email': email,
      'phone': phone,
    };
  }

  factory User.fromMap(Map<String, dynamic> map) {
    return User(
      username: map['username'] ?? '',
      fullName: map['fullName'] ?? '',
      role: map['role'] ?? 'user',
      email: map['email'],
      phone: map['phone'],
    );
  }

  User copyWith({
    String? username,
    String? fullName,
    String? role,
    String? email,
    String? phone,
  }) {
    return User(
      username: username ?? this.username,
      fullName: fullName ?? this.fullName,
      role: role ?? this.role,
      email: email ?? this.email,
      phone: phone ?? this.phone,
    );
  }
}
