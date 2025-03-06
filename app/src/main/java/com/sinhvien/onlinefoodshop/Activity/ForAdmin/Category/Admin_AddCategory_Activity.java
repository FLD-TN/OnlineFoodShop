package com.sinhvien.onlinefoodshop.Activity.ForAdmin.Category;

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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
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
import com.sinhvien.onlinefoodshop.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.UUID;

public class Admin_AddCategory_Activity extends AppCompatActivity {
    private static final String TAG = "Admin_AddCategory_Activity";
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final String FOLDER_NAME = "FutureFoodShop_Images";
    private EditText edtCategoryName;
    private Button btnSave, btnPickImage,btnCancel;
    private ApiService apiService;
    private Uri imageUri;
    private GoogleSignInClient googleSignInClient;
    private Drive driveService;
    private String imageUrl,folderParentId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_category);

        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://foodshop-backend-jck5.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnPickImage.setOnClickListener(v -> checkAndRequestPermissions());
        btnSave.setOnClickListener(v -> addCategory());
        btnCancel.setOnClickListener(v -> finish());
    }

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
                runOnUiThread(() -> Toast.makeText(Admin_AddCategory_Activity.this,
                        "Lỗi khi tìm/tạo thư mục: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void uploadImageToDrive(String folderId) {
        try {
            java.io.File file = uriToFile(imageUri);
            String fileName = "category_" + System.currentTimeMillis() + ".png";

            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(folderId));

            FileContent mediaContent = new FileContent("image/png", file);
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, webContentLink")
                    .execute();

            // Cập nhật quyền của file để mọi người có thể xem
            setFilePublic(uploadedFile.getId());

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

    private void setFilePublic(String fileId) {
        try {
            com.google.api.services.drive.model.Permission permission = new com.google.api.services.drive.model.Permission();
            permission.setType("anyone");
            permission.setRole("reader");

            driveService.permissions().create(fileId, permission)
                    .setFields("id")
                    .execute();

            Log.d(TAG, "File permission set to public: " + fileId);
        } catch (Exception e) {
            Log.e(TAG, "Error setting file permission: " + e.getMessage(), e);
        }
    }

    private void addCategory() {
        String categoryId = UUID.randomUUID().toString();
        String categoryName = edtCategoryName.getText().toString().trim();

        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền tên loại sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUrl == null || imageUrl.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn và tải lên hình ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        CategoryModel newCategory = new CategoryModel(categoryName, imageUrl);
        newCategory.setCategoryId(categoryId);

        apiService.addCategory(newCategory).enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Category added successfully: " + categoryName);
                    Toast.makeText(Admin_AddCategory_Activity.this, "Thêm loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newCategory", response.body());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Log.e(TAG, "Add category failed. Code: " + response.code());
                    Toast.makeText(Admin_AddCategory_Activity.this, "Thêm loại sản phẩm thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                Log.e(TAG, "Error adding category: " + t.getMessage());
                Toast.makeText(Admin_AddCategory_Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private java.io.File uriToFile(Uri uri) throws Exception {
        java.io.File file = new java.io.File(getCacheDir(), "temp_image_" + System.currentTimeMillis() + ".png");
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        }
        return file;
    }
}