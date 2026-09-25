# Trưa Nay Ăn Gì

Ứng dụng Android nhỏ giúp lưu danh sách món ăn và chọn ngẫu nhiên một món cho bữa trưa. Dự án được viết bằng Java và XML, phù hợp để tham khảo cho môn nhập môn lập trình ứng dụng Android.

## Chức năng

- Xem danh sách món ăn được lưu trên thiết bị
- Thêm, sửa và xóa món ăn
- Tìm kiếm theo tên, giá, loại hoặc mô tả
- Quay vòng quay để chọn ngẫu nhiên một món
- Có sẵn danh sách món mẫu để chạy thử
- Hỗ trợ giao diện sáng và tối

## Thông tin món ăn

Mỗi món gồm tên, giá, loại món và mô tả. Giá được lưu dưới dạng số nguyên và hiển thị theo định dạng tiền Việt Nam.

## Công nghệ

- Java 11
- Android XML Layout
- ArrayList lưu dữ liệu trong bộ nhớ
- Material 3
- RecyclerView
- `ValueAnimator` và Canvas cho vòng quay

## Cấu trúc chính

- `MainActivity`: danh sách, tìm kiếm và thao tác xóa
- `FoodFormActivity`: thêm hoặc sửa món ăn
- `SpinActivity`: chọn món ngẫu nhiên
- `FoodStore`: giữ danh sách và xử lý thêm, sửa, xóa
- `FoodAdapter`: hiển thị danh sách món ăn
- `WheelView`: vẽ và chạy hoạt ảnh vòng quay

## Cách chạy

1. Mở project bằng Android Studio.
2. Chờ Gradle đồng bộ các thư viện.
3. Chọn máy ảo hoặc thiết bị Android từ phiên bản 7.0 trở lên.
4. Chạy module `app`.

Có thể kiểm tra project bằng lệnh:

```bash
./gradlew test assembleDebug
```

Ứng dụng tạo sẵn sáu món Việt Nam để có thể thử ngay chức năng tìm kiếm và vòng quay. Danh sách sẽ trở về dữ liệu mẫu khi tiến trình ứng dụng được khởi động lại.

## Học project từ số 0

Nếu bạn mới bắt đầu Android, hãy đọc [Hướng dẫn Android từ số 0 với project này](docs/HUONG_DAN_ANDROID_TU_SO_0.md). Tài liệu đi từ tạo project **Empty Views Activity**, Gradle, `AndroidManifest.xml`, giao diện XML, Java, `RecyclerView`, CRUD, `Intent`, vòng đời Activity, custom View, animation, test đến các bài tập nâng cấp.
