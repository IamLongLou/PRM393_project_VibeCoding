package com.example.waterbilling.controller;

import com.example.waterbilling.dto.CustomerDto;
import com.example.waterbilling.dto.UpdateReadingRequest;
import com.example.waterbilling.dto.UpdateStatusRequest;
import com.example.waterbilling.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Customers", description = "Quản lý khách hàng, công việc ghi chỉ số và trạng thái thu tiền.")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Lấy danh sách khách hàng",
            description = """
                    Trả về danh sách khách hàng để hiển thị ở màn hình Customer List.
                    Có thể truyền q để tìm theo mã khách hàng hoặc tên khách hàng.
                    Field status trả theo index Flutter: 0=pending, 1=reading, 2=completed.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Danh sách khách hàng.")
    @GetMapping
    public List<CustomerDto> getAll(
            @Parameter(description = "Từ khóa tìm kiếm theo mã hoặc tên khách hàng.", example = "KH001")
            @RequestParam(required = false) String q
    ) {
        return customerService.getAll(q);
    }

    @Operation(summary = "Lấy chi tiết khách hàng", description = "Dùng cho màn hình chi tiết khách hàng trước khi ghi chỉ số hoặc xem lịch sử.")
    @ApiResponse(responseCode = "200", description = "Thông tin khách hàng.")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng.")
    @GetMapping("/{id}")
    public CustomerDto getById(
            @Parameter(description = "ID khách hàng.", example = "1")
            @PathVariable Long id
    ) {
        return customerService.getById(id);
    }

    @Operation(
            summary = "Tạo khách hàng/công việc mới",
            description = """
                    Tạo một khách hàng mới tương ứng với màn hình thêm công việc.
                    currentReading là chỉ số cũ ban đầu, status thường là 0 để đưa vào danh sách chờ xử lý.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Tạo khách hàng thành công.")
    @ApiResponse(responseCode = "400", description = "Mã khách hàng trùng hoặc dữ liệu không hợp lệ.")
    @PostMapping
    public CustomerDto create(@RequestBody CustomerDto dto) {
        return customerService.create(dto);
    }

    @Operation(summary = "Cập nhật thông tin khách hàng", description = "Cập nhật toàn bộ thông tin cơ bản của khách hàng từ màn hình edit customer.")
    @ApiResponse(responseCode = "200", description = "Cập nhật thành công.")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng.")
    @PutMapping("/{id}")
    public CustomerDto update(
            @Parameter(description = "ID khách hàng.", example = "1")
            @PathVariable Long id,
            @RequestBody CustomerDto dto
    ) {
        return customerService.update(id, dto);
    }

    @Operation(
            summary = "Cập nhật trạng thái khách hàng",
            description = "Đổi trạng thái xử lý công việc: 0=pending/chờ, 1=reading/đang ghi chỉ số, 2=completed/hoàn thành."
    )
    @ApiResponse(responseCode = "200", description = "Cập nhật trạng thái thành công.")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng.")
    @PatchMapping("/{id}/status")
    public CustomerDto updateStatus(
            @Parameter(description = "ID khách hàng.", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest body
    ) {
        return customerService.updateStatus(id, body.status());
    }

    @Operation(
            summary = "Cập nhật chỉ số hiện tại",
            description = """
                    Cập nhật currentReading của khách hàng và tự chuyển status sang completed.
                    Endpoint này chỉ cập nhật chỉ số, không tạo hóa đơn. Nếu muốn lập hóa đơn, dùng POST /api/customers/{customerId}/meter-readings.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Cập nhật chỉ số thành công.")
    @ApiResponse(responseCode = "400", description = "newReading nhỏ hơn currentReading hiện tại.")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng.")
    @PatchMapping("/{id}/reading")
    public CustomerDto updateReading(
            @Parameter(description = "ID khách hàng.", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateReadingRequest body
    ) {
        return customerService.updateReading(id, body.newReading());
    }
}
