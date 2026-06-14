package com.example.waterbilling.dto;

import com.example.waterbilling.entity.CollectionStatus;
import com.example.waterbilling.entity.Customer;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Thông tin khách hàng/công việc ghi chỉ số nước.")
public record CustomerDto(
        @Schema(description = "ID khách hàng do SQL Server tự sinh.", example = "1")
        Long id,
        @Schema(description = "Mã khách hàng duy nhất, dùng để tìm kiếm và hiển thị trong app.", example = "KH001")
        String code,
        @Schema(description = "Họ tên khách hàng.", example = "Lưu Bị")
        String name,
        @Schema(description = "Địa chỉ sử dụng nước.", example = "12-A Phố Huế, Hai Bà Trưng, Hà Nội")
        String address,
        @Schema(description = "Số điện thoại liên hệ.", example = "0912345001")
        String phone,
        @Schema(description = "Chỉ số nước hiện tại/chỉ số cũ trước lần ghi mới.", example = "125")
        Integer currentReading,
        @Schema(description = "Trạng thái theo Flutter enum: 0=pending, 1=reading, 2=completed.", example = "0")
        Integer status
) {
    public static CustomerDto from(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getCode(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getCurrentReading(),
                customer.getStatus().toFlutterIndex()
        );
    }

    public CollectionStatus statusEnum() {
        return CollectionStatus.fromFlutterIndex(status);
    }
}
