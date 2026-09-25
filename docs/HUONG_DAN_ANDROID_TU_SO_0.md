# Học Android từ số 0 với “Trưa Nay Ăn Gì”

Tài liệu này dành cho người chưa từng làm ứng dụng Android. Mục tiêu không phải là chép code cho chạy, mà là hiểu đủ để tự dựng lại project, sửa chức năng, tìm lỗi và tiếp tục học các project lớn hơn.

Project dùng **Java + XML Views**, không dùng Jetpack Compose.

## 1. Sau khi học xong bạn làm được gì?

Bạn sẽ biết:

- Tạo một Android project mới bằng Android Studio.
- Hiểu vai trò của Gradle, module `app`, package và SDK version.
- Khai báo Activity trong `AndroidManifest.xml`.
- Tạo giao diện bằng XML và nối View với Java qua `R.id`.
- Dùng resource: string, color, theme, drawable và dark mode.
- Hiểu `Activity`, lifecycle, `Intent`, extras và nút Back.
- Hiển thị danh sách bằng `RecyclerView` + Adapter + ViewHolder.
- Làm thêm, xem, sửa, xóa và tìm kiếm dữ liệu.
- Kiểm tra dữ liệu nhập và hiển thị lỗi đúng chỗ.
- Tạo custom View bằng `Canvas` và animation bằng `ValueAnimator`.
- Chạy app, đọc Logcat, đặt breakpoint và chạy test.
- Biết giới hạn của project mẫu và hướng nâng cấp lên Room/MVVM.

## 2. Bức tranh lớn: một Android app chạy như thế nào?

Trong project này, luồng đơn giản là:

```text
Người dùng chạm icon app
        ↓
Android đọc AndroidManifest.xml
        ↓
Mở MainActivity (MAIN + LAUNCHER)
        ↓
MainActivity nạp activity_main.xml
        ↓
Java tìm các View bằng findViewById(...)
        ↓
MainActivity lấy Food từ FoodStore
        ↓
FoodAdapter biến từng Food thành item_food.xml
        ↓
Người dùng thêm/sửa/quay → mở Activity khác bằng Intent
```

Ba nhóm bạn cần phân biệt:

| Nhóm | Nhiệm vụ | Ví dụ trong project |
|---|---|---|
| Cấu hình | App là gì, build thế nào, màn hình nào được mở | Gradle, `AndroidManifest.xml` |
| Giao diện | Màn hình trông ra sao | `res/layout`, `res/values`, `res/drawable` |
| Logic | App phản ứng và xử lý dữ liệu thế nào | Các file `.java` |

## 3. Chuẩn bị môi trường

Cài Android Studio bản ổn định và để Android Studio cài:

- Android SDK.
- SDK Platform phù hợp với `compileSdk` của project.
- Android SDK Build-Tools.
- Android Emulator nếu chưa có điện thoại Android để test.

Project đang có các mốc:

- `compileSdk 36`: dùng API của Android SDK 36 khi biên dịch.
- `targetSdk 36`: app được thiết kế để tuân theo hành vi của Android 36.
- `minSdk 24`: app chạy từ Android 7.0 (API 24) trở lên.
- Java 11: source và target compatibility là Java 11.

Đừng nhầm:

- **JDK** dùng để biên dịch code Java.
- **Android SDK** chứa API và công cụ Android.
- **Gradle** đọc cấu hình, tải thư viện, biên dịch, test và đóng gói APK.
- **AGP** (Android Gradle Plugin) dạy Gradle cách build Android app.

Android Studio thường có JDK đi kèm. Với người mới, nên dùng JDK mà Android Studio đề xuất thay vì tự đổi nhiều phiên bản cùng lúc.

## 4. Tự khởi tạo project giống project này

Trong Android Studio:

1. Chọn **New Project**.
2. Chọn template **Empty Views Activity**. Không chọn Empty Activity dành cho Compose.
3. Đặt Name là `TruaNayAnGi` hoặc `Trưa Nay Ăn Gì`.
4. Package name: `com.example.truanayangi`.
5. Language: **Java**.
6. Minimum SDK: **API 24**.
7. Nếu được hỏi build configuration language, chọn **Groovy DSL** để khớp các file `.gradle` của project này.
8. Bấm Finish và đợi Gradle Sync hoàn tất.

Android Studio có thể sinh phiên bản thư viện khác project mẫu. Điều đó bình thường. Khi đang học, ưu tiên một bộ version build được, không cần cố đổi lên version mới nhất.

Nếu muốn mở project có sẵn, chọn **Open**, trỏ tới thư mục gốc `TruaNayAnGi`, đợi indexing và Gradle Sync xong rồi chọn module `app` để Run.

## 5. Đọc cây thư mục

```text
TruaNayAnGi/
├── settings.gradle                 # tên project, kho thư viện, module được include
├── build.gradle                    # plugin dùng chung ở cấp project
├── gradle/libs.versions.toml       # version catalog: version và tên thư viện
├── gradlew / gradlew.bat           # Gradle Wrapper cho macOS/Linux và Windows
└── app/
    ├── build.gradle                # cấu hình module Android app
    ├── proguard-rules.pro          # luật tối ưu/obfuscate cho release
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/example/truanayangi/
        │   │   ├── MainActivity.java
        │   │   ├── FoodFormActivity.java
        │   │   ├── SpinActivity.java
        │   │   ├── Food.java
        │   │   ├── FoodStore.java
        │   │   ├── FoodAdapter.java
        │   │   ├── WheelView.java
        │   │   ├── PriceFormatter.java
        │   │   └── ScreenInsets.java
        │   └── res/
        │       ├── layout/          # giao diện XML
        │       ├── values/          # string, color, theme sáng
        │       ├── values-night/    # resource thay thế khi dark mode
        │       ├── drawable/        # shape và vector icon
        │       ├── mipmap-*/        # launcher icon
        │       └── xml/             # backup/data extraction rules
        ├── test/                    # unit test chạy trên máy phát triển
        └── androidTest/             # test chạy trên emulator/điện thoại
```

Quy tắc quan trọng: code Java nằm dưới package `com.example.truanayangi`, còn resource không nằm trong package Java nhưng được Android tạo chỉ mục qua lớp `R`.

## 6. Gradle: project được build bằng gì?

### 6.1 `settings.gradle`

File này:

- Đặt tên project là `TruaNayAnGi`.
- Khai báo nơi tìm plugin và dependency: Google Maven, Maven Central, Gradle Plugin Portal.
- Include module `:app`.

Nếu quên `include ':app'`, Gradle không biết module app tồn tại.

### 6.2 `gradle/libs.versions.toml`

Đây là **Version Catalog**. Thay vì viết chuỗi dependency dài nhiều lần, project đặt tên ngắn như:

```toml
recyclerview = { group = "androidx.recyclerview", name = "recyclerview", version.ref = "recyclerview" }
```

Sau đó `app/build.gradle` dùng:

```groovy
implementation libs.recyclerview
```

### 6.3 `app/build.gradle`

Các phần cần hiểu:

```groovy
android {
    namespace 'com.example.truanayangi'

    defaultConfig {
        applicationId "com.example.truanayangi"
        minSdk 24
        targetSdk 36
        versionCode 1
        versionName "1.0"
    }
}
```

- `namespace`: namespace sinh lớp `R` và code Android.
- `applicationId`: định danh app khi cài trên thiết bị/đưa lên store.
- `versionCode`: số nguyên tăng dần ở mỗi bản phát hành.
- `versionName`: version người dùng nhìn thấy.

Dependencies chính:

| Dependency | Dùng để làm gì |
|---|---|
| AppCompat | Activity và khả năng tương thích Android cũ |
| Material | Toolbar, button, card, text field theo Material 3 |
| Activity | API Activity/edge-to-edge hiện đại |
| ConstraintLayout | Bố cục ràng buộc của màn hình chính |
| RecyclerView | Danh sách cuộn hiệu quả |
| JUnit | Unit test |
| AndroidX Test + Espresso | Instrumented/UI test |

Sau khi sửa Gradle, bấm **Sync Project with Gradle Files**. Đừng sửa code liên tục khi Gradle Sync đang lỗi; xử lý lỗi build nền trước.

## 7. `AndroidManifest.xml`: “chứng minh thư” của app

Manifest của project khai báo một `<application>` và ba `<activity>`.

```xml
<activity
    android:name=".MainActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

Ý nghĩa:

- `.MainActivity` là cách viết ngắn của `com.example.truanayangi.MainActivity`.
- `MAIN` + `LAUNCHER` biến Activity này thành điểm mở khi chạm icon app.
- `exported="true"` cho phép launcher của hệ thống mở Activity.
- `FoodFormActivity` và `SpinActivity` có `exported="false"`, chỉ dùng nội bộ app.
- Theme, tên app, icon và backup rules được đặt ở `<application>`.

Project không cần quyền Internet, camera, vị trí hay bộ nhớ, nên không có `<uses-permission>`. Chỉ thêm permission khi chức năng thật sự cần và phải xử lý runtime permission với các quyền nguy hiểm.

Khi tạo một Activity Java mới thủ công, nhớ khai báo nó trong Manifest, nếu không `startActivity(...)` có thể gây `ActivityNotFoundException`.

## 8. Resource và lớp `R`

Android biên dịch các file trong `res/` thành ID. Ví dụ:

```xml
android:id="@+id/addFoodButton"
android:text="@string/add_food"
android:textColor="@color/text_primary"
```

Trong Java:

```java
findViewById(R.id.addFoodButton);
getString(R.string.add_food);
```

- `@+id/...`: tạo ID mới.
- `@id/...`: tham chiếu ID đã có.
- `@string/...`: lấy text từ `strings.xml`.
- `@color/...`: lấy màu từ `colors.xml`.
- `@drawable/...`: lấy vector icon hoặc shape.
- `@style/...`: áp dụng style.
- `?attr/...`: đọc thuộc tính từ theme hiện tại.

Không hard-code text trực tiếp trong layout nếu text đó hiện cho người dùng. Đặt text vào `strings.xml` giúp dịch ngôn ngữ, tái sử dụng và kiểm tra dễ hơn.

### Dark mode hoạt động ra sao?

`res/values/colors.xml` là bộ màu mặc định. Khi thiết bị ở dark mode, Android ưu tiên resource cùng tên trong `res/values-night/colors.xml`. Layout vẫn dùng `@color/text_primary`, nhưng giá trị tự thay đổi.

Theme kế thừa `Theme.Material3.DayNight.NoActionBar`; `NoActionBar` được chọn vì mỗi màn hình tự đặt `MaterialToolbar` trong layout.

## 9. XML layout từ cơ bản đến project này

Mỗi View thường cần:

```xml
android:layout_width="..."
android:layout_height="..."
```

Giá trị thường gặp:

- `match_parent`: chiếm toàn bộ kích thước cha cho phép.
- `wrap_content`: vừa đủ nội dung.
- `0dp` trong ConstraintLayout: kích thước do constraint quyết định.
- `0dp` + `layout_weight` trong LinearLayout: chia phần không gian còn lại.
- `dp`: kích thước/spacing độc lập mật độ màn hình.
- `sp`: cỡ chữ có tôn trọng thiết lập font size của người dùng.

### 9.1 `activity_main.xml`

Màn hình chính dùng `ConstraintLayout` gồm:

1. `MaterialToolbar` ở trên.
2. `TextInputLayout` + `TextInputEditText` để tìm kiếm.
3. `RecyclerView` chiếm vùng giữa.
4. `emptyView` nằm cùng vùng với danh sách nhưng mặc định ẩn.
5. `actionBar` chứa nút Thêm món và Quay chọn món ở đáy.

Điểm quan trọng của ConstraintLayout: mỗi View phải có đủ ràng buộc ngang và dọc. `RecyclerView` dùng `0dp × 0dp`, nên nó lấp đúng vùng giữa ô tìm kiếm và action bar.

### 9.2 `item_food.xml`

Đây không phải màn hình riêng. Nó là khuôn cho **một dòng** trong RecyclerView:

- `MaterialCardView` làm card.
- Tên và giá ở hàng đầu.
- Loại món có background `bg_category.xml`.
- Mô tả tối đa hai dòng.
- Hai nút Sửa/Xóa phát sự kiện về Activity qua listener.

### 9.3 `activity_food_form.xml`

Form dùng `LinearLayout` dọc và `NestedScrollView`, nhờ vậy màn hình nhỏ hoặc khi bàn phím mở vẫn có thể cuộn.

`TextInputLayout` là phần vỏ có label/error/suffix; `TextInputEditText` là ô nhập thật. Code đặt lỗi lên phần vỏ:

```java
nameLayout.setError(getString(R.string.name_required));
```

### 9.4 `activity_spin.xml`

Layout nhúng custom View bằng tên class đầy đủ:

```xml
<com.example.truanayangi.WheelView
    android:id="@+id/wheelView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

Nếu đổi package của `WheelView`, phải đổi tên này trong XML.

## 10. Java tối thiểu cần biết trước khi đọc Activity

Các khái niệm Java xuất hiện trong project:

- `class`: khuôn tạo đối tượng, ví dụ `Food`.
- field: dữ liệu giữ trong object, ví dụ `name`, `price`.
- constructor: khởi tạo object bằng `new Food(...)`.
- method: hành vi, ví dụ `matchesQuery(...)`.
- `private`: chỉ class đó dùng trực tiếp.
- `public`: nơi khác có thể gọi.
- `static`: thuộc về class thay vì một instance, ví dụ `FoodStore.getFoods()`.
- `final`: tham chiếu/giá trị không được gán lại sau khi khởi tạo.
- `List<Food>`: danh sách chỉ chứa `Food`.
- interface/callback: cách Adapter báo sự kiện Sửa/Xóa cho Activity.
- lambda: cách viết ngắn callback, ví dụ `view -> saveFood()`.
- `null`: không có object; phải kiểm tra trước khi dùng.
- exception: lỗi khi chạy; form bắt `NumberFormatException` khi giá không phải số.

Khi mới học, đọc theo thứ tự: model → store → layout item → adapter → Activity. Đọc Activity trước Adapter thường khiến luồng danh sách khó hiểu hơn.

## 11. Model và nơi giữ dữ liệu

### 11.1 `Food.java`

`Food` là model của một món ăn:

```text
id + name + price + category + description
```

`id` giúp xác định chính xác món cần sửa/xóa. Tên món không phù hợp làm ID vì hai món có thể trùng tên.

Giá dùng `long`, không dùng `double`, vì giá ở đây là số nguyên đồng Việt Nam. `PriceFormatter` chỉ chịu trách nhiệm đổi `45000` thành chuỗi hiển thị kiểu `45.000 đ`.

### 11.2 `FoodStore.java`

`FoodStore` giữ một `static ArrayList<Food>` và có các thao tác:

- `getFoods()` trả về bản sao danh sách.
- `getFoodById(id)` tìm một món.
- `addFood(food)` tạo ID rồi thêm.
- `updateFood(food)` thay object cùng ID.
- `deleteFood(id)` xóa object cùng ID.

Đây là nơi lưu **trong bộ nhớ tiến trình**, không phải database. Khi process app bị hệ thống đóng hoặc app khởi động lại hoàn toàn, danh sách quay về sáu món mẫu. Đây là chủ ý để project nhập môn ngắn gọn.

Việc `getFoods()` trả `new ArrayList<>(foods)` tránh cho màn hình bên ngoài sửa trực tiếp danh sách nội bộ.

## 12. RecyclerView: danh sách được tạo như thế nào?

RecyclerView cần ba mảnh:

```text
RecyclerView (khung danh sách)
    + LayoutManager (xếp item theo chiều nào)
    + Adapter (lấy dữ liệu và tạo/bind từng item)
```

Trong `MainActivity`:

```java
foodList.setLayoutManager(new LinearLayoutManager(this));
foodList.setAdapter(foodAdapter);
```

Trong `FoodAdapter`:

- `onCreateViewHolder`: inflate `item_food.xml` thành View.
- `FoodViewHolder`: giữ tham chiếu các View con để tái sử dụng.
- `onBindViewHolder`: lấy `Food` ở vị trí hiện tại và gọi `bind`.
- `getItemCount`: cho RecyclerView biết có bao nhiêu item.

Adapter không tự xóa/sửa dữ liệu. Nó gọi `FoodActionListener`; `MainActivity` mới quyết định mở form hay hiện dialog xác nhận. Cách tách này giúp Adapter chỉ lo hiển thị.

## 13. Activity, lifecycle và điều hướng

### 13.1 `onCreate`

`onCreate` chạy khi Activity được tạo. Thứ tự cơ bản:

```java
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
View view = findViewById(R.id.someView);
view.setOnClickListener(...);
```

Phải gọi `setContentView` trước `findViewById`, vì trước đó Activity chưa có cây View của layout.

### 13.2 Mở màn hình bằng Intent

Mở màn hình vòng quay:

```java
startActivity(new Intent(this, SpinActivity.class));
```

Mở form sửa và gửi ID:

```java
Intent intent = new Intent(this, FoodFormActivity.class);
intent.putExtra(FoodFormActivity.EXTRA_FOOD_ID, foodId);
startActivity(intent);
```

Form nhận lại bằng:

```java
foodId = getIntent().getLongExtra(EXTRA_FOOD_ID, -1);
```

Giá trị `-1` có nghĩa “không có ID”, nên form chuyển sang chế độ thêm mới. Khi bấm Back hoặc lưu xong, `finish()` đóng form và trở về Activity trước.

### 13.3 Tại sao `MainActivity` nạp lại ở `onResume`?

`onResume` chạy cả lần đầu lẫn khi quay về từ form. Vì vậy sau khi thêm/sửa món và `finish()`, danh sách chính tự gọi `loadFoods()` để hiện dữ liệu mới.

### 13.4 Giữ trạng thái khi xoay màn hình

`SpinActivity` lưu `selectedFoodId` trong `onSaveInstanceState`. Khi Activity được tạo lại sau configuration change, nó tìm món theo ID và khôi phục kết quả.

Đây chỉ là state giao diện nhỏ. Dữ liệu dài hạn nên nằm trong database/repository, không nhét toàn bộ vào `Bundle`.

## 14. CRUD và kiểm tra form

CRUD là:

- **Create**: thêm món.
- **Read**: đọc/hiển thị danh sách.
- **Update**: sửa món theo ID.
- **Delete**: xóa món theo ID.

`FoodFormActivity` dùng cùng một layout cho Create và Update:

- Có `foodId >= 0`: nạp dữ liệu cũ, tiêu đề “Sửa món ăn”, gọi `updateFood`.
- Không có ID: form rỗng, tiêu đề “Thêm món”, gọi `addFood`.

Luồng validate trong `saveFood()`:

1. Xóa lỗi cũ.
2. Đọc và `trim()` text.
3. Kiểm tra tên và loại không rỗng.
4. Parse giá bằng `Long.parseLong`.
5. Không cho giá âm.
6. Nếu bất kỳ điều kiện nào sai, dừng lại và giữ form.
7. Nếu hợp lệ, thêm/sửa, báo `Toast`, rồi `finish()`.

Không nên chỉ kiểm tra `inputType="number"` trong XML. XML giúp bàn phím phù hợp hơn, nhưng dữ liệu vẫn cần validate trong Java.

## 15. Tìm kiếm không dấu và theo giá

`MainActivity` gắn `TextWatcher` vào ô tìm kiếm. Mỗi khi text đổi, `filterFoods(...)` chạy và gửi danh sách kết quả sang Adapter.

`Food.matchesQuery(...)`:

- Chuẩn hóa Unicode bằng `Normalizer`.
- Bỏ dấu tiếng Việt và đổi `đ` thành `d`.
- Chuyển chữ thường.
- Ghép tên, loại, mô tả, giá thành vùng tìm kiếm.
- Với truy vấn số, bỏ dấu chấm/phẩy/khoảng trắng để `50.000` vẫn khớp `50000`.

Tách logic tìm kiếm vào model giúp viết unit test không cần emulator.

## 16. Custom View và animation vòng quay

`WheelView` là phần nâng cao; hãy học sau khi danh sách và form đã chạy.

Custom View kế thừa `View` và override `onDraw(Canvas canvas)`:

- `Canvas`: bề mặt để vẽ.
- `Paint`: màu, style, cỡ chữ.
- `RectF`: khung hình tròn.
- `drawArc`: vẽ từng lát.
- `drawText`: vẽ tên món.
- `Path`: vẽ kim chỉ.

Mỗi lát có góc:

```text
sweepAngle = 360 / số món
```

`ValueAnimator` thay đổi `rotationAngle` từ góc hiện tại đến góc đích. Mỗi frame gọi `invalidate()`, Android vẽ lại View. `DecelerateInterpolator` làm vòng quay chậm dần.

`onDetachedFromWindow()` hủy animator để tránh tiếp tục chạy khi View rời màn hình.

## 17. Insets và vùng an toàn hệ thống

`ScreenInsets.apply(...)` lấy kích thước status bar/navigation bar rồi thêm padding cho root View. Nhờ vậy nội dung không bị camera cutout, thanh trạng thái hoặc thanh điều hướng che.

Đây là chi tiết dễ bỏ sót khi layout trông đúng trên một emulator nhưng sai trên thiết bị khác.

## 18. Lộ trình tự dựng lại app, từng mốc một

Đừng chép toàn bộ project trong một lần. Làm theo các mốc dưới đây và **chạy app sau mỗi mốc**.

### Mốc 1 — Project rỗng build được

1. Tạo Empty Views Activity bằng Java.
2. Chạy màn hình mặc định trên emulator.
3. Kiểm tra `MainActivity` đã có trong Manifest.

Hoàn thành khi: app cài được và mở không crash.

### Mốc 2 — Resource và theme

1. Tạo `strings.xml`, `colors.xml`, `themes.xml`.
2. Dùng `Theme.Material3.DayNight.NoActionBar`.
3. Tạo `values-night/colors.xml` với cùng tên màu.

Hoàn thành khi: app đổi màu đúng giữa light/dark mode.

### Mốc 3 — Giao diện chính tĩnh

1. Tạo toolbar, ô tìm kiếm, RecyclerView và action bar trong `activity_main.xml`.
2. Chỉ nối click listener cho hai nút và hiện Toast thử.

Hoàn thành khi: màn hình không tràn/cắt ở emulator nhỏ và lớn.

### Mốc 4 — Model và dữ liệu mẫu

1. Tạo `Food` với constructor/getter.
2. Tạo `FoodStore` và sáu món mẫu.
3. Viết `PriceFormatter`.

Hoàn thành khi: unit test format giá chạy qua.

### Mốc 5 — RecyclerView

1. Tạo `item_food.xml`.
2. Tạo `FoodAdapter` và `FoodViewHolder`.
3. Gắn `LinearLayoutManager` và Adapter trong `MainActivity`.

Hoàn thành khi: thấy sáu món, cuộn được và không crash.

### Mốc 6 — Thêm và sửa

1. Tạo `activity_food_form.xml`.
2. Tạo `FoodFormActivity` và khai báo trong Manifest.
3. Dùng Intent extra để phân biệt thêm/sửa.
4. Validate tên, loại, giá.
5. Nạp lại danh sách trong `MainActivity.onResume()`.

Hoàn thành khi: thêm món mới và sửa món cũ đều cập nhật danh sách.

### Mốc 7 — Xóa và tìm kiếm

1. Dùng listener từ Adapter về Activity.
2. Hiện `AlertDialog` trước khi xóa.
3. Thêm `TextWatcher` và `matchesQuery`.
4. Hiện empty state khi không có kết quả.

Hoàn thành khi: tìm “com tam” thấy “Cơm tấm”, tìm “50.000” thấy món 50.000đ.

### Mốc 8 — Vòng quay

1. Tạo `SpinActivity`, layout và khai báo Manifest.
2. Ban đầu có thể chọn random rồi hiện text, chưa cần vòng quay.
3. Sau khi logic đúng, tạo `WheelView` và `ValueAnimator`.
4. Lưu kết quả trong `onSaveInstanceState`.

Hoàn thành khi: nút bị disable lúc quay, kết quả khớp lát dừng và vẫn còn sau khi xoay máy.

### Mốc 9 — Chất lượng giao diện

1. Thêm empty states.
2. Dùng string resource thay text hard-code.
3. Thêm `contentDescription` và accessibility live region nơi phù hợp.
4. Áp dụng system bar insets.
5. Test font size lớn và dark mode.

### Mốc 10 — Test và đóng gói

1. Viết unit test cho tìm kiếm/format giá.
2. Chạy `test` và `assembleDebug`.
3. Chạy instrumented test trên emulator.
4. Tìm APK debug trong `app/build/outputs/apk/debug/`.

## 19. Chạy, test và debug

### Trong Android Studio

1. Mở **Device Manager** và tạo máy ảo nếu cần.
2. Chọn thiết bị trên thanh công cụ.
3. Chọn run configuration `app`.
4. Bấm Run.

Để debug:

1. Bấm vào lề trái dòng code để đặt breakpoint.
2. Bấm Debug thay vì Run.
3. Thực hiện thao tác trong app.
4. Xem Variables và Call Stack khi code dừng.

Nếu app crash, mở **Logcat**, lọc theo package `com.example.truanayangi`, tìm `FATAL EXCEPTION` và đọc dòng `Caused by` đầu tiên có đường dẫn tới code của mình.

### Bằng terminal

macOS/Linux:

```bash
./gradlew test assembleDebug
```

Windows:

```bat
gradlew.bat test assembleDebug
```

Chạy instrumented test khi đã có emulator/thiết bị:

```bash
./gradlew connectedAndroidTest
```

Lệnh Gradle Wrapper giúp mọi người dùng đúng Gradle version của project; không cần cài Gradle global.

## 20. Lỗi phổ biến và cách suy nghĩ

### `R` màu đỏ hoặc `Cannot resolve symbol R`

Thường do một file XML sai cú pháp hoặc resource được tham chiếu nhưng chưa tồn tại. Mở Build Output, sửa lỗi XML đầu tiên rồi Rebuild.

### `ActivityNotFoundException`

Activity mới chưa được khai báo trong `AndroidManifest.xml`, hoặc tên package/class sai.

### `NullPointerException` sau `findViewById`

ID không tồn tại trong layout đã truyền cho `setContentView`, hoặc gọi `findViewById` trước `setContentView`.

### Layout không hiện đúng trong ConstraintLayout

Kiểm tra đủ constraint ngang/dọc. `0dp` trong ConstraintLayout có nghĩa “match constraints”, không phải luôn luôn bằng 0 pixel.

### Bấm lưu nhưng app crash khi parse giá

Mọi input của người dùng đều là chuỗi và có thể sai. Bọc parse trong `try/catch`, kiểm tra rỗng và khoảng giá hợp lệ.

### Sửa dữ liệu nhưng quay lại danh sách không đổi

Kiểm tra `onResume()` có nạp lại store và Adapter có được thông báo dữ liệu mới không.

### Dark mode có chữ khó đọc

Đừng hard-code màu hex trực tiếp trong layout. Dùng tên semantic như `text_primary`, rồi định nghĩa cùng tên cho `values` và `values-night`.

### Gradle Sync lỗi

Kiểm tra lần lượt: Internet, JDK Gradle đang dùng, SDK đã cài, version AGP/Gradle tương thích, rồi dependency nào tải lỗi. Sửa lỗi đầu tiên thay vì các lỗi dây chuyền phía sau.

## 21. Test: test cái gì ở đâu?

### Unit test (`app/src/test`)

Chạy nhanh trên JVM, phù hợp với logic không phụ thuộc Android UI:

- Tìm kiếm không phân biệt hoa/thường.
- Tìm kiếm bỏ dấu tiếng Việt.
- Tìm theo giá đã format.
- Format tiền.

### Instrumented test (`app/src/androidTest`)

Chạy trên Android thật/emulator, phù hợp với:

- Kiểm tra package/context.
- Mở Activity.
- Bấm nút, nhập form, kiểm tra text hiển thị.
- Test luồng thêm/sửa/xóa.

Quy tắc thực dụng: logic Java thuần đưa vào unit test; thứ cần framework Android hoặc UI đưa vào instrumented test.

## 22. Giới hạn hiện tại và hướng nâng cấp đúng thứ tự

Project cố ý đơn giản, nên chưa phải kiến trúc production:

1. **FoodStore chỉ ở RAM** → học Room database để dữ liệu tồn tại lâu dài.
2. **Activity đang giữ nhiều logic UI** → học ViewModel và LiveData/StateFlow.
3. **Adapter refresh cả vùng dữ liệu** → học `ListAdapter` + `DiffUtil`.
4. **Thao tác dữ liệu đồng bộ** → học Repository và background thread/coroutine (nếu chuyển Kotlin).
5. **Chưa có dependency injection** → chỉ học Hilt khi app đủ lớn; không cần thêm sớm.
6. **Test UI còn cơ bản** → thêm test cho CRUD và rotation.

Một hướng kiến trúc sau này:

```text
Activity/Fragment
        ↓ quan sát state
ViewModel
        ↓ gọi
Repository
        ↓
Room database / API
```

Hãy nâng cấp từng lớp. Đừng chuyển Room + MVVM + Hilt + Kotlin + Compose cùng một lần, vì khi lỗi sẽ rất khó biết lỗi thuộc phần nào.

## 23. Bài tập thực hành

### Cấp 1 — Củng cố

- Thêm trường “quán ăn” vào `Food` và form.
- Thêm nút “Xóa nội dung tìm kiếm”.
- Đổi bảng màu nhưng vẫn hỗ trợ dark mode.
- Thêm validation: giá tối đa 10.000.000đ.
- Sắp xếp món theo tên hoặc giá.

### Cấp 2 — Tự làm tính năng

- Lọc theo loại món bằng dropdown/chip.
- Đánh dấu món yêu thích.
- Chống chọn lại món vừa quay ở lần kế tiếp.
- Hiện số lượng kết quả tìm kiếm.
- Dùng `ListAdapter` + `DiffUtil`.

### Cấp 3 — Tiến gần production

- Thay `FoodStore` bằng Room.
- Thêm ViewModel để giữ UI state qua configuration change.
- Export/import danh sách món dạng JSON.
- Thêm ảnh món ăn bằng Photo Picker.
- Viết instrumented test cho toàn bộ luồng thêm → sửa → xóa.

Mỗi bài nên làm theo vòng lặp: viết yêu cầu nhỏ → xác định file cần đổi → code → chạy → test case sai → commit.

## 24. Checklist “đã đủ nền tảng để tự làm”

Bạn đã nắm project khi có thể tự trả lời và thực hiện các việc sau mà không chép nguyên file:

- [ ] Giải thích launcher tìm ra `MainActivity` bằng cách nào.
- [ ] Tạo một Activity + XML mới và khai báo Manifest.
- [ ] Giải thích `@+id`, `@string`, `@color`, `dp`, `sp`.
- [ ] Nối một button XML với click listener Java.
- [ ] Gửi một ID qua Intent extra và nhận ở Activity khác.
- [ ] Tạo model và hiển thị danh sách bằng RecyclerView.
- [ ] Giải thích ba method chính của Adapter.
- [ ] Validate input và hiện lỗi mà không làm app crash.
- [ ] Biết vì sao danh sách cần refresh trong `onResume`.
- [ ] Đọc được stack trace cơ bản trong Logcat.
- [ ] Viết và chạy ít nhất một unit test.
- [ ] Giải thích vì sao dữ liệu hiện tại mất khi process bị đóng.
- [ ] Tự thêm một field mới xuyên suốt model → store → form → item.

## 25. Thứ tự đọc code đề xuất

Đọc và chạy song song theo thứ tự này:

1. `app/build.gradle`
2. `app/src/main/AndroidManifest.xml`
3. `res/values/strings.xml`, `colors.xml`, `themes.xml`
4. `res/layout/activity_main.xml`
5. `Food.java`
6. `FoodStore.java`
7. `res/layout/item_food.xml`
8. `FoodAdapter.java`
9. `MainActivity.java`
10. `res/layout/activity_food_form.xml`
11. `FoodFormActivity.java`
12. `res/layout/activity_spin.xml`
13. `SpinActivity.java`
14. `WheelView.java`
15. Các file trong `test` và `androidTest`

Sau mỗi file, tự ghi lại ba câu: file nhận đầu vào gì, làm việc gì, và trả kết quả/sự kiện cho ai. Nếu trả lời được ba câu đó, bạn đang hiểu luồng thay vì chỉ nhận mặt cú pháp.
