import 'package:flutter/material.dart';

class SyncScreen extends StatelessWidget {
  const SyncScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Đồng bộ dữ liệu"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.sync, size: 100, color: Colors.blue),
            const SizedBox(height: 20),
            const Text(
              "Tất cả dữ liệu đã được đồng bộ",
              style: TextStyle(fontSize: 18),
            ),
            const SizedBox(height: 40),
            ElevatedButton.icon(
              onPressed: () {
                // Thêm logic đồng bộ ở đây
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text("Đang đồng bộ...")),
                );
              },
              icon: const Icon(Icons.refresh),
              label: const Text("Bắt đầu đồng bộ"),
            ),
          ],
        ),
      ),
    );
  }
}
