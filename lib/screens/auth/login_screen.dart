import 'dart:math' as math;
import 'package:flutter/material.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> with SingleTickerProviderStateMixin {
  final txtUser = TextEditingController();
  final txtPass = TextEditingController();
  bool _obscureText = true;
  
  late AnimationController _controller;

  @override
  void initState() {
    super.initState();
    // Tạo hiệu ứng chuyển động liên tục cho background
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(seconds: 15),
    )..repeat();
  }

  @override
  void dispose() {
    _controller.dispose();
    txtUser.dispose();
    txtPass.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          // 1. Lớp nền Gradient màu xanh sâu thẳm
          Container(
            decoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
                colors: [
                  Theme.of(context).primaryColor,
                  const Color(0xFF003366),
                ],
              ),
            ),
          ),

          // 2. Hiệu ứng "Giọt nước/Bong bóng" trôi nổi phía sau
          AnimatedBuilder(
            animation: _controller,
            builder: (context, child) {
              return CustomPaint(
                painter: WaterParticlePainter(_controller.value),
                size: Size.infinite,
              );
            },
          ),

          // 3. Nội dung chính: Form đăng nhập
          Center(
            child: SingleChildScrollView(
              child: Padding(
                padding: const EdgeInsets.all(30),
                child: Card(
                  elevation: 15,
                  shadowColor: Colors.black.withOpacity(0.4),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 40),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        // Biểu tượng nước hiện đại
                        Container(
                          padding: const EdgeInsets.all(20),
                          decoration: BoxDecoration(
                            color: Colors.blue[50],
                            shape: BoxShape.circle,
                          ),
                          child: Icon(
                            Icons.water_drop_rounded,
                            size: 60,
                            color: Theme.of(context).primaryColor,
                          ),
                        ),
                        const SizedBox(height: 20),
                        const Text(
                          "HỆ THỐNG GHI SỐ",
                          style: TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                            letterSpacing: 1.5,
                          ),
                        ),
                        const Text(
                          "Vui lòng đăng nhập để tiếp tục",
                          style: TextStyle(color: Colors.grey, fontSize: 13),
                        ),
                        const SizedBox(height: 40),
                        
                        // Ô nhập tên đăng nhập
                        TextField(
                          controller: txtUser,
                          decoration: InputDecoration(
                            labelText: "Tên đăng nhập",
                            prefixIcon: const Icon(Icons.person_outline_rounded),
                            filled: true,
                            fillColor: Colors.grey[50],
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(16),
                              borderSide: BorderSide.none,
                            ),
                          ),
                        ),
                        const SizedBox(height: 20),
                        
                        // Ô nhập mật khẩu
                        TextField(
                          controller: txtPass,
                          obscureText: _obscureText,
                          decoration: InputDecoration(
                            labelText: "Mật khẩu",
                            prefixIcon: const Icon(Icons.lock_outline_rounded),
                            suffixIcon: IconButton(
                              icon: Icon(_obscureText ? Icons.visibility : Icons.visibility_off),
                              onPressed: () => setState(() => _obscureText = !_obscureText),
                            ),
                            filled: true,
                            fillColor: Colors.grey[50],
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(16),
                              borderSide: BorderSide.none,
                            ),
                          ),
                        ),
                        const SizedBox(height: 30),
                        
                        // Nút Đăng nhập
                        SizedBox(
                          width: double.infinity,
                          height: 55,
                          child: ElevatedButton(
                            onPressed: () {
                              Navigator.pushReplacementNamed(context, "/home");
                            },
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Theme.of(context).primaryColor,
                              foregroundColor: Colors.white,
                              elevation: 4,
                              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                            ),
                            child: const Text(
                              "ĐĂNG NHẬP",
                              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                            ),
                          ),
                        ),
                        const SizedBox(height: 15),
                        TextButton(
                          onPressed: () {},
                          child: const Text("Hỗ trợ kỹ thuật?", style: TextStyle(color: Colors.blueGrey)),
                        )
                      ],
                    ),
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

/// Lớp vẽ các hạt nước chuyển động sinh động
class WaterParticlePainter extends CustomPainter {
  final double animationValue;
  WaterParticlePainter(this.animationValue);

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = Colors.white.withOpacity(0.08) // Hơi trong suốt
      ..style = PaintingStyle.fill;

    // Tạo ra 15 hạt nước chuyển động theo các quỹ đạo khác nhau
    for (int i = 0; i < 15; i++) {
      // Tính toán vị trí X (có lắc lư theo hàm sin)
      double x = (math.sin(i * 1.5 + animationValue * 2 * math.pi) * 30) + (size.width * (i / 15));
      
      // Tính toán vị trí Y (trôi từ dưới lên trên và lặp lại)
      double y = (size.height + 100) - ((animationValue + (i * 0.1)) % 1.0 * (size.height + 200));
      
      // Kích thước hạt ngẫu nhiên
      double radius = 10.0 + (i % 6) * 15.0;
      
      canvas.drawCircle(Offset(x, y), radius, paint);
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => true;
}
