package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Product;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.sinhvien.onlinefoodshop.ApiService;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Collections;

public class Admin_EditProduct_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_EditProduct_Activity";
    private EditText edtProductName, edtProductPrice, edtDescription, edtCategory;
    private Button btnSave, btnCancel, btnPickImage, btnDownload;
    private ApiService apiService;
    private ProductModel currentProduct;
    private GoogleSignInClient googleSignInClient;
    private Uri imageUri;
    private Drive driveService;
    private String imageUrl, folderParentId;
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final String FOLDER_NAME = "FutureFoodShop_Images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);

        edtProductName = findViewById(R.id.edtProductName);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtCategory = findViewById(R.id.edtCategory);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnDownload = findViewById(R.id.btnDownload);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://foodshop-backend-jck5.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        Intent intent = getIntent();
        currentProduct = (ProductModel) intent.getSerializableExtra("productDetail");

        if (currentProduct != null && currentProduct.getProductID() != null) {
            edtProductName.setText(currentProduct.getProductName());
            edtProductPrice.setText(String.valueOf(currentProduct.getProductPrice()));
            edtDescription.setText(currentProduct.getDescription());
            edtCategory.setText(currentProduct.getCategory());
        } else {
            Log.e(TAG, "Current product is null or missing productID");
            Toast.makeText(this, "Không tải được thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnPickImage.setOnClickListener(v -> checkAndRequestPermissions());
        btnSave.setOnClickListener(v -> saveProductChanges());
        btnCancel.setOnClickListener(v -> finish());
        btnDownload.setOnClickListener(v -> downloadImage());
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            signInToGoogleDrive();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            signInToGoogleDrive();
        } else {
            Toast.makeText(this, "Cần quyền truy cập ảnh để tiếp tục", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInToGoogleDrive() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(null));
        } else {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        }
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                } else {
                    Toast.makeText(this, "Đăng nhập Google bị hủy", Toast.LENGTH_SHORT).show();
                }
            });

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask != null && completedTask.isSuccessful() ?
                    completedTask.getResult() : GoogleSignIn.getLastSignedInAccount(this);

            if (account == null) {
                Toast.makeText(this, "Không thể lấy thông tin tài khoản Google", Toast.LENGTH_SHORT).show();
                return;
            }

            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
            credential.setSelectedAccount(account.getAccount());

            driveService = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                    .setApplicationName("FutureFoodShop")
                    .build();

            pickImage();
        } catch (Exception e) {
            Log.e(TAG, "Sign-in failed: " + e.getMessage());
            Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    findOrCreateFolderAndUpload();
                } else {
                    Toast.makeText(this, "Chọn ảnh bị hủy", Toast.LENGTH_SHORT).show();
                }
            });

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void findOrCreateFolderAndUpload() {
        if (imageUri == null || driveService == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh và đăng nhập Google", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Đang chuẩn bị tải ảnh lên...", Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            try {
                FileList folderList = driveService.files().list()
                        .setQ("name='" + FOLDER_NAME + "' and mimeType='application/vnd.google-apps.folder' and trashed=false")
                        .setSpaces("drive")
                        .setFields("files(id, name)")
                        .execute();

                if (folderList.getFiles() != null && !folderList.getFiles().isEmpty()) {
                    folderParentId = folderList.getFiles().get(0).getId();
                    Log.d(TAG, "Found folder: " + FOLDER_NAME + " with ID: " + folderParentId);
                } else {
                    File folderMetadata = new File();
                    folderMetadata.setName(FOLDER_NAME);
                    folderMetadata.setMimeType("application/vnd.google-apps.folder");

                    File folder = driveService.files().create(folderMetadata).setFields("id").execute();
                    folderParentId = folder.getId();
                    Log.d(TAG, "Created folder: " + FOLDER_NAME + " with ID: " + folderParentId);
                }

                uploadImageToDrive(folderParentId);
            } catch (Exception e) {
                Log.e(TAG, "Error finding/creating folder: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi tìm/tạo thư mục: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void uploadImageToDrive(String folderId) {
        try {
            java.io.File file = Utils.uriToFile(this, imageUri);
            String fileName = "product_" + System.currentTimeMillis() + ".png";

            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(folderId));

            FileContent mediaContent = new FileContent("image/png", file);
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, webContentLink")
                    .execute();

            Utils.setFilePublic(driveService, uploadedFile.getId());

            imageUrl = "https://drive.google.com/uc?export=download&id=" + uploadedFile.getId();
            runOnUiThread(() -> {
                Toast.makeText(this, "Upload ảnh thành công", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Uploaded image URL: " + imageUrl);
            });
        } catch (Exception e) {
            Log.e(TAG, "Upload error: " + e.getMessage(), e);
            runOnUiThread(() -> Toast.makeText(this, "Upload thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void downloadImage() {
        String url = (imageUrl != null && !imageUrl.isEmpty()) ? imageUrl : currentProduct.getProductImage();
        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "Không có ảnh để tải về", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void saveProductChanges() {
        if (currentProduct == null || currentProduct.getProductID() == null) {
            Toast.makeText(this, "Thông tin sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String productName = edtProductName.getText().toString().trim();
        String priceStr = edtProductPrice.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();

        if (productName.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền tên và giá sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        double productPrice;
        try {
            productPrice = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá sản phẩm phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        currentProduct.setProductName(productName);
        currentProduct.setProductPrice(productPrice);
        currentProduct.setDescription(description);
        currentProduct.setCategory(category);
        // Nếu không chọn ảnh mới (imageUrl null), giữ nguyên ảnh cũ
        currentProduct.setProductImage(imageUrl != null ? imageUrl : currentProduct.getProductImage());

        apiService.updateProduct(currentProduct.getProductID(), currentProduct).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Product updated successfully: " + productName);
                    Toast.makeText(Admin_EditProduct_Activity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedProduct", response.body());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Log.e(TAG, "Update failed. Code: " + response.code());
                    Toast.makeText(Admin_EditProduct_Activity.this, "Cập nhật thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Log.e(TAG, "Error updating product: " + t.getMessage());
                Toast.makeText(Admin_EditProduct_Activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}