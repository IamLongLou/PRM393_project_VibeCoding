import 'package:flutter/material.dart';

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Thông tin cá nhân"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            const CircleAvatar(
              radius: 50,
              child: Icon(Icons.person, size: 50),
            ),
            const SizedBox(height: 20),
            const ListTile(
              leading: Icon(Icons.person),
              title: Text("Họ và tên"),
              subtitle: Text("Nguyễn Văn A"),
            ),
            const ListTile(
              leading: Icon(Icons.email),
              title: Text("Email"),
              subtitle: Text("nguyenvana@example.com"),
            ),
            const ListTile(
              leading: Icon(Icons.phone),
              title: Text("Số điện thoại"),
              subtitle: Text("0123456789"),
            ),
            const Spacer(),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.red,
                minimumSize: const Size(double.infinity, 50),
              ),
              onPressed: () {
                Navigator.pushNamedAndRemoveUntil(context, "/login", (route) => false);
              },
              child: const Text("Đăng xuất", style: TextStyle(color: Colors.white)),
            ),
          ],
        ),
      ),
    );
  }
}
