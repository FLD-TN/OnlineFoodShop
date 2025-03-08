package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Product;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.sinhvien.onlinefoodshop.Model.CategoryModel;
import com.sinhvien.onlinefoodshop.Model.ProductModel;
import com.sinhvien.onlinefoodshop.R;
import com.sinhvien.onlinefoodshop.Utils.Utils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Admin_AddProduct_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_AddProduct_Activity";
    private EditText edtProductName, edtProductPrice, edtDescription;
    private Spinner spinnerCategory;
    private Button btnSave, btnCancel,btnPickImage;
    private ApiService apiService;
    private Uri imageUri;
    private GoogleSignInClient googleSignInClient;
    private Drive driveService;
    private String imageUrl,folderParentId;
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final String FOLDER_NAME = "FutureFoodShop_Images";
    private List<CategoryModel> categoryList = new ArrayList<>();

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

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            signInToGoogleDrive();
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            signInToGoogleDrive();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                signInToGoogleDrive();
            } else {
                Toast.makeText(this, "Cần quyền truy cập ảnh để tiếp tục", Toast.LENGTH_SHORT).show();
            }
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

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account;
            if (completedTask != null && completedTask.isSuccessful()) {
                account = completedTask.getResult();
            } else {
                account = GoogleSignIn.getLastSignedInAccount(this);
            }

            if (account == null) {
                Toast.makeText(this, "Không thể lấy thông tin tài khoản Google", Toast.LENGTH_SHORT).show();
                return;
            }

            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
            credential.setSelectedAccount(account.getAccount());

            driveService = new Drive.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName("FutureFoodShop")
                    .build();

            pickImage();
        } catch (Exception e) {
            Log.e(TAG, "Sign-in failed: " + e.getMessage());
            Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

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
                // Tìm thư mục FutureFoodShop_Images
                FileList folderList = driveService.files().list()
                        .setQ("name='" + FOLDER_NAME + "' and mimeType='application/vnd.google-apps.folder' and trashed=false")
                        .setSpaces("drive")
                        .setFields("files(id, name)")
                        .execute();

                // Lấy ID của thư mục hoặc tạo thư mục mới nếu chưa tồn tại
                if (folderList.getFiles() != null && !folderList.getFiles().isEmpty()) {
                    folderParentId = folderList.getFiles().get(0).getId();
                    Log.d(TAG, "Found folder: " + FOLDER_NAME + " with ID: " + folderParentId);
                } else {
                    // Tạo thư mục mới nếu chưa tồn tại
                    Log.d(TAG, "Folder not found, creating new one: " + FOLDER_NAME);
                    File folderMetadata = new File();
                    folderMetadata.setName(FOLDER_NAME);
                    folderMetadata.setMimeType("application/vnd.google-apps.folder");

                    File folder = driveService.files().create(folderMetadata)
                            .setFields("id")
                            .execute();
                    folderParentId = folder.getId();
                    Log.d(TAG, "Created folder: " + FOLDER_NAME + " with ID: " + folderParentId);
                }

                // Bây giờ upload ảnh vào thư mục đã tìm/tạo
                uploadImageToDrive(folderParentId);

            } catch (Exception e) {
                Log.e(TAG, "Error finding/creating folder: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(Admin_AddProduct_Activity.this,
                        "Lỗi khi tìm/tạo thư mục: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void uploadImageToDrive(String folderId) {
        try {
            java.io.File file = Utils.uriToFile(this,imageUri);
            String fileName = "product_" + System.currentTimeMillis() + ".png";

            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(folderId));

            FileContent mediaContent = new FileContent("image/png", file);
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, webContentLink")
                    .execute();

            // Cập nhật quyền của file để mọi người có thể xem
            Utils.setFilePublic(driveService, uploadedFile.getId());

                imageUrl = "https://drive.google.com/uc?export=download&id=" + uploadedFile.getId();
            runOnUiThread(() -> {
                Toast.makeText(this, "Upload thành công", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Uploaded image URL: " + imageUrl);
            });
        } catch (Exception e) {
            Log.e(TAG, "Upload error: " + e.getMessage(), e);
            runOnUiThread(() -> Toast.makeText(this, "Upload thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        edtProductName = findViewById(R.id.edtProductName);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        edtDescription = findViewById(R.id.edtDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://foodshop-backend-jck5.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        Utils.loadCategories(this,apiService,spinnerCategory);
        btnPickImage.setOnClickListener(v -> checkAndRequestPermissions());
        btnSave.setOnClickListener(v -> addProduct());
        btnCancel.setOnClickListener(v -> finish());
    }


    private void addProduct() {
        String productID = UUID.randomUUID().toString(); // Tạo ID ngẫu nhiên
        String productName = edtProductName.getText().toString().trim();
        String priceStr = edtProductPrice.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem() != null ? spinnerCategory.getSelectedItem().toString() : ""; // Lấy danh mục từ Spinner
        btnPickImage = findViewById(R.id.btnPickImage);

        if (productName.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền tên và giá sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUrl == null || imageUrl.isEmpty()) {
            Picasso.get()
                    .load(R.drawable.placeholder_image);
        }

        double productPrice;
        try {
            productPrice = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá sản phẩm phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        ProductModel newProduct = new ProductModel(productName, productPrice, description, category,imageUrl);
        newProduct.setProductID(productID);



        apiService.addProduct(newProduct).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Product added successfully: " + productName);
                    Toast.makeText(Admin_AddProduct_Activity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newProduct", response.body());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Log.e(TAG, "Add product failed. Code: " + response.code());
                    Toast.makeText(Admin_AddProduct_Activity.this, "Thêm sản phẩm thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Log.e(TAG, "Error adding product: " + t.getMessage());
                Toast.makeText(Admin_AddProduct_Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}