package com.sinhvien.onlinefoodshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sinhvien.onlinefoodshop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class TermFragment extends Fragment {

    private ExpandableListView expandableListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms, container, false);

        expandableListView = view.findViewById(R.id.expandableListView);

        // dữ liệu cho ExpandableListView
        List<Map<String, String>> groupList = new ArrayList<>();
        List<List<Map<String, String>>> childList = new ArrayList<>();

        // Tiêu đề các mục chính
        String[] groupTitles = {
                "Bộ Quy Tắc Ứng Xử",
                "Điều khoản dịch vụ",
                "Điều khoản dịch vụ Tài xế khu vực",
                "Chính sách bảo mật"
        };

        // Nội dung chi tiết của từng mục
        String[] childContents = {
                "Quý Tài xế thân mến\n" +
                        "\n" +
                        "Để đảm bảo chất lượng dịch vụ, kèm theo đó, công việc giao hàng của Tài xế không bị gián đoạn, Công ty luôn khuyến khích Quý Tài xế nâng cao chất lượng dịch vụ thông qua tác phong phục vụ lịch sự, ân cần và chuyên nghiệp. Bộ Quy tắc ứng xử tập hợp chi tiết các nội quy – quy định từ công ty để Tài xế nắm thông tin quy định hiện hành và có hướng ứng xử phù hợp trong quá trình hợp tác cùng công ty. Tất cả những hành vi vi phạm quy định đều sẽ bị xử lý.\n" +
                        "\n" +
                        "ĐỐI TƯỢNG ÁP DỤNG: Tất cả Tài xế ShopeeFood thực hiện việc giao - nhận đơn hàng trên toàn quốc.\n" +
                        "\n" +
                        "THỜI GIAN ÁP DỤNG: Từ 27.07.2023 đến khi có thông báo mới\n" +
                        "\n",
                "1. GIỚI THIỆU\n" +
                        "\n" +
                        "1.1 Chào mừng đến với ứng dụng di động ShopeeFood - Đối tác Tài xế dành cho Tài xế (“ShopeeFood - Đối tác Tài xế”). Trước khi sử dụng ShopeeFood - Đối tác Tài xế hoặc tạo tài khoản ShopeeFood - Đối tác Tài xế (“Tài khoản”), Tài xế vui lòng đọc kỹ các Điều Khoản Dịch Vụ dưới đây để hiểu rõ quyền lợi và nghĩa vụ hợp pháp của mình đối với Công ty Cổ phần Foody (“Foody”) và các công ty liên kết của Foody (sau đây được gọi riêng là “Foody”, gọi chung là “chúng tôi”, “của chúng tôi”). “Dịch Vụ” chúng tôi cung cấp hoặc sẵn có bao gồm: (a) các dịch vụ được cung cấp bởi ShopeeFood - Đối tác Tài xế và/hoặc bởi đơn vị do Foody ủy quyền cung cấp dịch vụ có sẵn trên ShopeeFood - Đối tác Tài xế, và (b) tất cả các thông tin, đường dẫn, tính năng, dữ liệu, văn bản, hình ảnh, biểu đồ, âm nhạc, âm thanh, video, tin nhắn, tags, nội dung, chương trình, ứng dụng dịch vụ (bao gồm, nhưng không giới hạn, bất kỳ ứng dụng dịch vụ di động nào) hoặc các tài liệu khác có sẵn trên ShopeeFood - Đối tác Tài xế hoặc các dịch vụ liên quan đến ShopeeFood - Đối tác Tài xế (“Nội dung”). Bất kỳ tính năng mới nào được bổ sung hoặc mở rộng đối với Dịch Vụ đều thuộc phạm vi điều chỉnh của Điều Khoản Dịch Vụ này. Điều Khoản Dịch Vụ này điều chỉnh việc Tài xế sử dụng Dịch Vụ cung cấp bởi ShopeeFood - Đối tác Tài xế.\n" +
                        "\n" +
                        "1.2 Dịch Vụ bao gồm: dịch vụ cho phép người dùng (“Tài xế”) nhận thông tin liên quan đến đơn hàng để cung cấp dịch vụ giao nhận cho Khách hàng của Foody (“Khách hàng”) và các tính năng kèm theo để hỗ trợ Tài xế cung cấp dịch vụ giao nhận.\n" +
                        "\n" +
                        "1.3 Trước khi trở thành Tài xế, người dùng cần đọc và chấp nhận mọi điều khoản và điều kiện được quy định trong, và dẫn chiếu đến, Điều Khoản Dịch Vụ này và Chính Sách Bảo Mật được dẫn chiếu theo đây.\n" +
                        "\n" +
                        "1.4 Foody bảo lưu quyền thay đổi, chỉnh sửa, tạm ngưng hoặc chấm dứt tất cả hoặc bất kỳ phần nào của ShopeeFood - Đối tác Tài xế hoặc Dịch Vụ vào bất cứ thời điểm nào theo quyền tự quyết của mình và/hoặc quy định pháp luật. Foody không chịu trách nhiệm đối với việc thực hiện các Dịch Vụ hoặc tính năng của Dịch Vụ với phiên bản thử nghiệm mà các phiên bản này có thể không đúng hoặc không hoàn toàn giống với phiên bản chính thức. Foody có toàn quyền giới hạn một số tính năng hoặc phạm vi truy cập của Tài xế đối với một phần hoặc toàn bộ ShopeeFood - Đối tác Tài xế hoặc Dịch Vụ của ShopeeFood - Đối tác Tài xế mà không cần thông báo trước hay chịu trách nhiệm.\n" +
                        "\n" +
                        "1.5 ShopeeFood - Đối tác Tài xế bảo lưu quyền từ chối yêu cầu mở Tài khoản hoặc các truy cập của Tài xế tới ShopeeFood - Đối tác Tài xế hoặc Dịch Vụ với bất kỳ lý do nào.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "BẰNG VIỆC SỬ DỤNG DỊCH VỤ HAY TẠO TÀI KHOẢN TẠI SHOPEEFOOD-ĐỐI TÁC TÀI XẾ, TÀI XẾ ĐÃ CHẤP NHẬN KHÔNG HỦY NGANG ĐỐI VỚI VÀ ĐỒNG Ý VỚI NHỮNG ĐIỀU KHOẢN DỊCH VỤ NÀY, BAO GỒM NHỮNG ĐIỀU KHOẢN, ĐIỀU KIỆN, VÀ CHÍNH SÁCH BỔ SUNG ĐƯỢC DẪN CHIẾU THEO ĐÂY VÀ/HOẶC CÓ LIÊN QUAN.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "NẾU TÀI XẾ KHÔNG ĐỒNG Ý VỚI NHỮNG ĐIỀU KHOẢN DỊCH VỤ NÀY, VUI LÒNG KHÔNG SỬ DỤNG DỊCH VỤ HOẶC TRUY CẬP VÀO SHOPEEFOOD - ĐỐI TÁC TÀI XẾ. DỊCH VỤ VÀ BẤT KỲ PHẦN NÀO CỦA SHOPEEFOOD - ĐỐI TÁC TÀI XẾ ĐỀU KHÔNG DÀNH CHO NGƯỜI DƯỚI 18 TUỔI. CHÚNG TÔI SẼ KHÓA BẤT KỲ TÀI KHOẢN NÀO TRÊN SHOPEEFOOD - ĐỐI TÁC TÀI XẾ SỬ DỤNG HOÀN TOÀN BỞI ĐỐI TƯỢNG DƯỚI 18 TUỔI NHƯ VẬY.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "2. QUYỀN RIÊNG TƯ\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Foody coi trọng việc bảo mật thông tin của Tài xế. Để bảo vệ quyền lợi Tài xế, Foody cung cấp Chính Sách Bảo Mật tại ShopeeFood - Đối tác Tài xế để giải thích chi tiết các hoạt động bảo mật của ShopeeFood - Đối tác Tài xế. Vui lòng tham khảo Chính Sách Bảo Mật để biết cách thức Foody thu thập và sử dụng thông tin liên quan đến Tài khoản và/hoặc việc sử dụng Dịch Vụ của Tài xế (“Thông Tin Tài xế”).\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "3. TÀI KHOẢN VÀ BẢO MẬT – TÍNH NĂNG NHẬN DIỆN KHUÔN MẶT (FACE ID)\n" +
                        "\n" +
                        "3.1. Để sử dụng Dịch Vụ, người dùng phải thực hiện các thủ tục đăng ký để trở thành Tài xế của Foody. Khi đã trở thành Tài xế của Foody, Foody sẽ cấp cho Tài xế một tài khoản (“Tài khoản ShopeeFood - Đối tác Tài xế”). Tài khoản ShopeeFood - Đối tác Tài xế có liên kết với số điện thoại do Tài xế cung cấp. Tài xế đăng nhập Tài khoản ShopeeFood - Đối tác Tài xế bằng cách nhập mã xác thực OTP được gửi qua số điện thoại đã liên kết. Tài xế có thể sử dụng Tài khoản ShopeeFood - Đối tác Tài xế để truy cập vào các Dịch Vụ được cung cấp trên ShopeeFood - Đối tác Tài xế.\n" +
                        "\n" +
                        "3.2. Tài xế đồng ý (a) đảm bảo Tài xế sẽ đăng xuất khỏi Tài khoản ShopeeFood - Đối tác Tài xế sau mỗi phiên đăng nhập trên ShopeeFood - Đối tác Tài xế, và (b) thông báo ngay lập tức với Foody nếu phát hiện bất kỳ việc sử dụng trái phép nào đối với Tài khoản ShopeeFood - Đối tác Tài xế của Tài xế. Tài xế phải hoàn toàn chịu trách nhiệm với mọi hoạt động dưới tên đăng nhập và Tài khoản ShopeeFood - Đối tác Tài xế, ngay cả khi những hoạt động hoặc việc sử dụng đó không do Tài xế thực hiện. Foody không chịu trách nhiệm đối với bất kỳ tổn thất hoặc thiệt hại nào phát sinh từ việc sử dụng trái phép nào liên quan đến mật khẩu hoặc từ việc không tuân thủ Điều Khoản này của Tài xế.\n" +
                        "\n" +
                        "3.3. Trong quá trình sử dụng ShopeeFood - Đối tác Tài xế, Tài xế có thể được yêu cầu nhận diện khuôn mặt để xác minh người dùng. Hình chụp khuôn mặt tức thời của Tài xế sẽ được so sánh với hình đăng ký để đảm bảo người dùng hợp lệ. Thất bại trong việc nhận diện khuôn mặt có thể dẫn đến việc Tài xế không thể sử dụng Dịch Vụ tạm thời hay vĩnh viễn tùy theo quyết định của Foody sau khi điều tra vụ việc.\n" +
                        "\n" +
                        "3.4. Tài xế đồng ý rằng Foody có toàn quyền khóa, tạm ngưng, xóa Tài khoản ShopeeFood - Đối tác Tài xế của Tài xế căn cứ theo các quy định, chính sách của Foody. Ngoài ra, việc sử dụng Tài khoản ShopeeFood - Đối tác Tài xế với bất kỳ mục đích bất hợp pháp, lừa đảo, quấy rối, xâm phạm, đe dọa hoặc lạm dụng nào có thể được chuyển đến cơ quan nhà nước có thẩm quyền mà không cần thông báo trước cho Tài xế. Trường hợp có tranh chấp liên quan đến Tài khoản ShopeeFood - Đối tác Tài xế hoặc việc sử dụng của Tài xế đối với Dịch Vụ với bất kỳ lý do gì, Foody có quyền tạm ngưng hoặc vô hiệu hóa Tài khoản ShopeeFood - Đối tác Tài xế như vậy ngay lập tức mà có thể hoặc không cần thông báo.\n" +
                        "\n" +
                        "3.5. Tài xế có thể yêu cầu xóa Tài khoản ShopeeFood - Đối tác Tài xế bằng cách thỏa thuận với Foody về việc chấm dứt hợp tác cung cấp dịch vụ giao nhận và hoàn tất tất cả các thủ tục để chấm dứt hợp tác. Bất kể việc xóa Tài khoản như vậy, Tài xế vẫn phải chịu trách nhiệm và nghĩa vụ đối với bất kỳ việc vận chuyển hàng hóa hay giao dịch nào chưa hoàn thành (phát sinh trước hoặc trong khi Tài khoản bị xóa). Foody không có trách nhiệm và không chịu trách nhiệm đối với bất kỳ tổn thất nào phát sinh từ bất kỳ hành động nào liên quan đến Mục này. Tài xế miễn trừ bất kỳ và mọi khiếu nại liên quan đến bất kỳ hành động nào như vậy của Foody.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "4. SỬ DỤNG DỊCH VỤ\n" +
                        "\n" +
                        "4.1. Quyền được phép sử dụng ShopeeFood - Đối tác Tài xế và Dịch Vụ có hiệu lực cho đến khi bị chấm dứt. Quyền được phép sử dụng sẽ bị chấm dứt theo Điều Khoản Dịch Vụ này hoặc trường hợp Tài xế vi phạm bất cứ điều khoản hoặc điều kiện nào được quy định tại Điều Khoản Dịch Vụ này hoặc các quy định, chính sách khác của Foody. Trong trường hợp đó, Foody có thể chấm dứt việc sử dụng của Tài xế và có thông báo đến Tài xế về vấn đề này.\n" +
                        "\n" +
                        "4.2. Foody trao cho Tài xế quyền phù hợp để truy cập và sử dụng các Dịch Vụ theo các điều khoản và điều kiện được quy định trong Điều Khoản Dịch Vụ này. Tất cả các Nội Dung, thương hiệu, nhãn hiệu dịch vụ, tên thương mại, biểu tượng và tài sản sở hữu trí tuệ khác độc quyền (“Tài Sản Sở Hữu Trí Tuệ”) hiển thị trên ShopeeFood - Đối tác Tài xế đều thuộc sở hữu của Foody và/hoặc bên sở hữu thứ ba, nếu có. Không một bên nào truy cập vào ShopeeFood - Đối tác Tài xế được cấp quyền hoặc cấp phép trực tiếp hoặc gián tiếp để sử dụng hoặc sao chép bất kỳ Tài Sản Sở Hữu Trí Tuệ nào, cũng như không một bên nào truy cập vào ShopeeFood - Đối tác Tài xế được phép truy đòi bất kỳ quyền, quyền sở hữu hoặc lợi ích nào liên quan đến Tài Sản Sở Hữu Trí Tuệ. Bằng cách sử dụng hoặc truy cập Dịch Vụ, Tài xế đồng ý tuân thủ các quy định pháp luật liên quan đến quyền tác giả, thương hiệu, nhãn hiệu hoặc bất cứ quy định pháp luật nào khác bảo vệ Dịch Vụ, ShopeeFood - Đối tác Tài xế và Nội Dung. Tài xế đồng ý không được phép sao chép, phát tán, tái bản, chuyển giao, công bố công khai, thực hiện công khai, sửa đổi, phóng tác, cho thuê, bán, hoặc tạo ra các sản phẩm phái sinh của bất cứ phần nào thuộc Dịch Vụ, ShopeeFood - Đối tác Tài xế và Nội Dung. Tài xế không được nhân bản hoặc chỉnh sửa bất kỳ phần nào hoặc toàn bộ Nội Dung mà chưa nhận được sự chấp thuận bằng văn bản của Foody. Ngoài ra, Tài xế đồng ý rằng Tài xế sẽ không sử dụng bất kỳ phần mềm, chương trình do thám hay bất kỳ thiết bị tự động hoặc phương thức thủ công nào để theo dõi hoặc sao chép Nội Dung khi chưa có sự đồng ý trước bằng văn bản của Foody.\n" +
                        "\n" +
                        "4.3. Tài xế không được phép:\n" +
                        "\n" +
                        "- Truyền tải, công khai bất cứ Nội Dung nào trái pháp luật, có hại, đe dọa, lạm dụng, quấy rối, gây hoang mang, lo lắng, xuyên tạc, phỉ báng, xúc phạm, khiêu dâm, bôi nhọ, xâm phạm quyền riêng tư của người khác, gây căm phẫn, hoặc phân biệt chủng tộc, dân tộc hoặc bất kỳ nội dung không đúng đắn nào khác;\n" +
                        "\n" +
                        "- Vi phạm pháp luật, quyền lợi của Foody và/hoặc bên thứ ba;\n" +
                        "\n" +
                        "- Gây tổn hại cho trẻ vị thành niên dưới bất kỳ hình thức nào;\n" +
                        "\n" +
                        "- Sử dụng Dịch Vụ, Nội Dung để mạo danh bất kỳ cá nhân hoặc tổ chức nào, hoặc bằng cách nào khác xuyên tạc cá nhân hoặc tổ chức;\n" +
                        "\n" +
                        "- Giả mạo các tiêu đề hoặc bằng cách khác ngụy tạo các định dạng nhằm che giấu nguồn gốc của bất kỳ Nội Dung nào được truyền tải thông qua Dịch Vụ;\n" +
                        "\n" +
                        "- Gây ra, chấp nhận hoặc ủy quyền cho việc sửa đổi, cấu thành các sản phẩm phái sinh, hoặc chuyển thể của Dịch Vụ mà không được sự cho phép rõ ràng của Foody;\n" +
                        "\n" +
                        "- Sử dụng Dịch Vụ, Nội Dung nhằm phục vụ lợi ích của bất kỳ bên thứ ba nào và/hoặc bất kỳ hành vi nào không được chấp nhận theo Điều Khoản Dịch Vụ này;\n" +
                        "\n" +
                        "- Sử dụng Dịch Vụ, Nội Dung cho mục đích gian lận, không hợp lý, sai trái, gây hiểu nhầm hoặc gây nhầm lẫn;\n" +
                        "\n" +
                        "- Mở và vận hành nhiều Tài khoản Tài xế khác nhau liên quan đến bất kỳ hành vi nào vi phạm điều khoản hoặc tinh thần của Điều Khoản Dịch Vụ này;\n" +
                        "\n" +
                        "- Truy cập ShopeeFood - Đối tác Tài xế thông qua phần mềm giả lập, thiết bị giả lập, phần mềm tự động hoặc bất kỳ phần mềm, thiết bị hoặc phần cứng nào có chức năng tương tự;\n" +
                        "\n" +
                        "- Thực hiện bất kỳ hành động nào có thể phá hoại hệ thống của ShopeeFood - Đối tác Tài xế và/hoặc bất cứ chức năng nào trên ShopeeFood - Đối tác Tài xế;\n" +
                        "\n" +
                        "- Cố gắng chuyển đổi mã chương trình, đảo ngược kỹ thuật, tháo gỡ hoặc xâm nhập (hack) Dịch Vụ (hoặc bất cứ hợp phần nào theo đó); hoặc phá bỏ hoặc vượt qua bất kỳ công nghệ mã hóa hoặc biện pháp bảo mật nào được Foody áp dụng đối với các Dịch Vụ và/hoặc các dữ liệu được truyền tải, xử lý hoặc lưu trữ bởi Foody;\n" +
                        "\n" +
                        "- Khai thác hoặc thu thập bất kỳ thông tin nào liên quan đến Tài khoản của Tài xế khác, bao gồm nhưng không giới hạn, bất kỳ thông tin hoặc dữ liệu cá nhân nào;\n" +
                        "\n" +
                        "- Truyền tải, công khai bất kỳ Nội Dung nào mà Tài xế không được cho phép theo bất kỳ luật hoặc quan hệ hợp đồng hoặc tín thác nào (chẳng hạn thông tin nội bộ, thông tin độc quyền và thông tin mật được biết hoặc tiết lộ hoặc theo các thỏa thuận bảo mật);\n" +
                        "\n" +
                        "- Truyền tải, công khai bất kỳ Nội Dung nào dẫn đến trường hợp vi phạm các quyền về sáng chế, thương hiệu, bí quyết kinh doanh, bản quyền (quyền tác giả) hoặc bất cứ đặc quyền nào của bất cứ bên nào;\n" +
                        "\n" +
                        "- Truyền tải, công khai bất kỳ quảng cáo, các tài liệu khuyến mại, “thư quấy rối”, “thư rác”, “chuỗi ký tự” không được phép hoặc không hợp pháp, hoặc bất kỳ hình thức lôi kéo không được phép nào khác;\n" +
                        "\n" +
                        "- Truyền tải, công khai bất cứ tài liệu chứa các loại vi-rút, mã độc hoặc bất kỳ các mã, tập tin hoặc chương trình máy tính được thiết kế để trực tiếp hoặc gián tiếp gây cản trở, điều khiển, gián đoạn, phá hỏng hoặc hạn chế các chức năng hoặc tổng thể của bất kỳ phần mềm hoặc phần cứng hoặc dữ liệu hoặc thiết bị viễn thông của máy tính;\n" +
                        "\n" +
                        "- Làm gián đoạn các dòng tương tác thông thường, đẩy nhanh tốc độ “lướt (scroll)” màn hình hơn những Tài xế khác có thể đối với Dịch Vụ, hoặc bằng cách khác thực hiện thao tác gây ảnh hưởng tiêu cực đến khả năng tham gia giao dịch thực của những Tài xế khác;\n" +
                        "\n" +
                        "- Can thiệp, điều khiển hoặc làm gián đoạn Dịch Vụ hoặc máy chủ hoặc hệ thống được liên kết với Dịch Vụ hoặc tới việc sử dụng và trải nghiệm Dịch Vụ của Tài xế khác, hoặc không tuân thủ bất kỳ các yêu cầu, quy trình, chính sách và luật lệ đối với các hệ thống được liên kết với ShopeeFood - Đối tác Tài xế;\n" +
                        "\n" +
                        "- Thực hiện bất kỳ hành động hoặc hành vi nào có thể trực tiếp hoặc gián tiếp phá hủy, vô hiệu hóa, làm quá tải, hoặc làm suy yếu Dịch Vụ hoặc máy chủ hoặc hệ thống liên kết với Dịch Vụ;\n" +
                        "\n" +
                        "- Sử dụng Dịch Vụ để vi phạm pháp luật, quy chế, quy tắc, chỉ thị, hướng dẫn, chính sách áp dụng của địa phương, quốc gia hoặc quốc tế (có hoặc chưa có hiệu lực) một cách có chủ ý hoặc vô ý liên quan đến phòng chống rửa tiền hoặc phòng chống khủng bố;\n" +
                        "\n" +
                        "- Sử dụng Dịch Vụ để vi phạm hoặc phá vỡ bất kỳ hình phạt hay lệnh cấm vận nào được quản lý hay thực thi bởi các cơ quan có liên quan;\n" +
                        "\n" +
                        "- Sử dụng Dịch Vụ để xâm hại tới quyền riêng tư của người khác hoặc để “lén theo dõi” hoặc bằng cách khác quấy rối người khác;\n" +
                        "\n" +
                        "- Xâm phạm các quyền của Foody, bao gồm bất kỳ quyền sở hữu trí tuệ và gây nhầm lẫn cho các quyền đó;\n" +
                        "\n" +
                        "- Sử dụng Dịch vụ để thu thập hoặc lưu trữ dữ liệu cá nhân của Tài xế khác liên quan đến các hành vi và hoạt động bị cấm như đề cập ở trên; và/hoặc\n" +
                        "\n" +
                        "- Liệt kê các hàng hóa xâm phạm bản quyền (quyền tác giả), nhãn hiệu hoặc các quyền sở hữu trí tuệ khác của các bên thứ ba hoặc sử dụng Dịch Vụ theo các cách thức có thể xâm phạm đến quyền sở hữu trí tuệ của các bên khác.\n" +
                        "\n" +
                        "4.4. Tài xế hiểu rằng các nội dung, dù được đăng công khai hoặc truyền tải riêng tư, là hoàn toàn thuộc trách nhiệm của người đã tạo ra nội dung đó. Điều đó nghĩa là Tài xế, không phải Foody, phải hoàn toàn chịu trách nhiệm đối với những nội dung mà Tài xế truyền tải, công khai trên ShopeeFood - Đối tác Tài xế. Tài xế hiểu rằng bằng cách sử dụng ShopeeFood - Đối tác Tài xế, Tài xế có thể gặp phải nội dung mà Tài xế cho là phản cảm, không đúng đắn hoặc chưa phù hợp. Trong giới hạn tối đa được pháp luật cho phép, và trong bất cứ trường hợp nào, Foody sẽ không chịu trách nhiệm dưới bất kỳ hình thức nào đối với bất kỳ nội dung nào, bao gồm, nhưng không giới hạn, bất kỳ lỗi hoặc thiếu sót liên quan đến bất kỳ nội dung nào, hoặc bất kỳ tổn thất hoặc thiệt hại dù như thế nào xuất phát từ việc sử dụng, hoặc dựa trên, bất kỳ Nội Dung nào được truyền tải, công khai trên ShopeeFood - Đối tác Tài xế.\n" +
                        "\n" +
                        "4.5. Tài xế thừa nhận rằng Foody và các bên được chỉ định của mình có toàn quyền (nhưng không có nghĩa vụ) sàng lọc, từ chối, xóa, dừng, tạm dừng, gỡ bỏ hoặc dời bất kỳ Nội Dung có sẵn trên ShopeeFood - Đối tác Tài xế, bao gồm nhưng không giới hạn bất kỳ Nội Dung hoặc thông tin nào Tài xế đã truyền tải, công khai. Không giới hạn ở những điều trên, Foody và các bên được chỉ định của mình có quyền gỡ bỏ bất kỳ Nội Dung nào (i) xâm phạm đến Điều Khoản Dịch Vụ này; (ii) trường hợp Foody nhận được khiếu nại về Nội Dung; (iii) trường hợp Foody nhận được thông báo về vi phạm quyền sở hữu trí tuệ hoặc yêu cầu pháp lý cho việc gỡ bỏ; hoặc (iv) những nguyên nhân khác. Foody có quyền chặn các liên lạc (bao gồm, nhưng không giới hạn, việc cập nhật trạng thái, tán gẫu) về hoặc liên quan đến Dịch Vụ như nỗ lực của chúng tôi nhằm bảo vệ Dịch Vụ hoặc Tài xế của ShopeeFood - Đối tác Tài xế, hoặc bằng cách khác thi hành các điều khoản trong Điều Khoản Dịch Vụ này. Tài xế đồng ý rằng mình phải đánh giá, và chịu mọi rủi ro liên quan đến việc sử dụng bất kỳ Nội Dung nào, bao gồm, nhưng không giới hạn bất kỳ việc dựa vào tính chính xác, đầy đủ, hoặc độ hữu dụng của Nội Dung đó. Liên quan đến vấn đề này, Tài xế thừa nhận rằng mình không phải và trong giới hạn tối đa pháp luật cho phép, không cần dựa vào bất kỳ Nội Dung nào được tạo bởi ShopeeFood - Đối tác Tài xế hoặc gửi cho ShopeeFood - Đối tác Tài xế, bao gồm, nhưng không giới hạn, các thông tin trên các diễn đàn trên ShopeeFood - Đối tác Tài xế (nếu có) hoặc trên các phần khác của ShopeeFood - Đối tác Tài xế.\n" +
                        "\n" +
                        "4.6. Tài xế thừa nhận, chấp thuận và đồng ý rằng Foody có thể truy cập, duy trì và tiết lộ thông tin Tài khoản ShopeeFood - Đối tác Tài xế trong trường hợp pháp luật có yêu cầu hoặc theo lệnh của tòa án hoặc bất kỳ cơ quan chính phủ hoặc cơ quan nhà nước nào có thẩm quyền yêu cầu Foody hoặc theo quan điểm của Foody thì việc duy trì truy cập hoặc tiết lộ đó là cần thiết để: (a) tuân thủ các quy định pháp luật; (b) thực thi Điều Khoản Dịch Vụ này; (c) phản hồi các khiếu nại về việc Nội Dung xâm phạm đến quyền lợi của bên thứ ba; (d) phản hồi các yêu cầu của Tài xế liên quan đến Dịch Vụ, Nội Dung; hoặc (e) bảo vệ các quyền, tài sản hoặc an toàn của Foody, Tài xế và/hoặc cộng đồng.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "5. VI PHẠM ĐIỀU KHOẢN DỊCH VỤ\n" +
                        "\n" +
                        "5.1. Việc vi phạm Điều Khoản Dịch Vụ này có thể dẫn tới một số hành động của Foody, bao gồm, nhưng không giới hạn, bất kỳ hoặc tất cả các hành động sau:\n" +
                        "\n" +
                        "- Đình chỉ và/hoặc chấm dứt Tài khoản ShopeeFood - Đối tác Tài xế theo Bộ Quy tắc ứng xử;\n" +
                        "\n" +
                        "- Áp dụng biện pháp dân sự, bao gồm nhưng không giới hạn, khiếu nại bồi thường thiệt hại và/hoặc áp dụng biện pháp khẩn cấp tạm thời;\n" +
                        "\n" +
                        "- Cáo buộc hình sự.\n" +
                        "\n" +
                        "5.2. Nếu phát hiện bất cứ Tài xế nào có hành vi vi phạm Điều Khoản Dịch Vụ này, bất cứ ai cũng có thể liên hệ ShopeeFood - Đối tác Tài xế tại đây.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "6. SỬ DỤNG VÍ CỦA TÀI XẾ\n" +
                        "\n" +
                        "6.1. ShopeeFood - Đối tác Tài xế cung cấp tính năng Ví của Tài xế (“Ví Tài xế”) bao gồm tài khoản chính và tài khoản ký quỹ:\n" +
                        "\n" +
                        "- Tài khoản chính là một công cụ cho phép ghi nhận, theo dõi thông tin số tiền các giao dịch phát sinh từ việc cung cấp dịch vụ giao nhận của Tài xế thông qua ứng dụng ShopeeFood - Đối tác Tài xế. Các tính năng chính của Tài khoản chính bao gồm: xem số dư, xem lịch sử giao dịch, đặt lệnh rút tiền, nạp tiền.\n" +
                        "\n" +
                        "- Tài khoản ký quỹ là công cụ ghi nhận khoản tiền cọc Tài xế đóng về Công ty. Các tính năng chính của Tài khoản ký quỹ bao gồm: xem số dư, xem lịch sử giao dịch, đặt lệnh nạp tiền.\n" +
                        "\n" +
                        "Trong từng trường hợp và với thông báo trước, Foody có thể cấn trừ từ Ví Tài xế các khoản công nợ còn tồn đọng giữa Tài xế và Foody trong quá trình hợp tác.\n" +
                        "\n" +
                        "Ví Tài xế ShopeeFood - Đối tác Tài xế theo đây được xem là căn cứ xác định công nợ phải thu hoặc công nợ phải trả của Tài xế đối với Foody tại mỗi thời điểm bất kỳ:\n" +
                        "\n" +
                        "- Tài xế phát sinh khoản phải thu từ Foody khi phát sinh thu nhập từ dịch vụ giao hàng và/hoặc tiền hàng Tài xế đã ứng trước trả cho Quán.\n" +
                        "\n" +
                        "- Tài xế phát sinh khoản phải trả cho Foody bao gồm nhưng không giới hạn các khoản tiền hàng đã thu từ Khách hàng, khoản phạt vi phạm trong hoạt động giao hàng và nghĩa vụ thuế (nếu có).\n" +
                        "\n" +
                        "Để làm rõ khái niệm công nợ, công nợ còn phải thu hoặc thanh toán của Tài xế là số dư thể hiện trên Ví Tài xế sau khi đã cấn trừ các khoản phải thu và các khoản phải trả vào thời điểm phát sinh giao dịch trên ứng dụng. Trường hợp số dư trên Ví Tài xế âm được hiểu là Tài xế có nghĩa vụ phải trả đối với Foody và Tài xế sẽ phải tuân thủ theo quy định tại điều 7.5 để bảo đảm hoạt động liên tục.\n" +
                        "\n" +
                        "6.2. Vào từng thời điểm, Ví Tài xế được liên kết với kênh thanh toán của một hoặc nhiều bên thứ ba để thực hiện giao dịch như sau:\n" +
                        "\n" +
                        "- Liên kết tài khoản ngân hàng đối tác của Công ty: Hình thức thanh toán bằng tài khoản ngân hàng chỉ dành cho Tài xế có tài khoản mở tại các ngân hàng có liên kết với tài khoản ngân hàng của Foody.\n" +
                        "\n" +
                        "- Ví điện tử ShopeePay: Ví điện tử ShopeePay được tích hợp sẵn trên ShopeeFood - Đối tác Tài xế được sử dụng để hỗ trợ Tài xế có Ví điện tử ShopeePay có thể dễ dàng chuyển tiền, rút tiền, thực hiện thanh toán đối với các giao dịch phát sinh từ việc cung cấp dịch vụ giao nhận của Tài xế thông qua ShopeeFood - Đối tác Tài xế.\n" +
                        "\n" +
                        "- Chuyển khoản ngân hàng: Mỗi Tài xế sẽ được đối tác của Công ty cung cấp tài khoản ngân hàng định danh có sẵn thông tin trong Ví, mã này chỉ có thể hỗ trợ nạp tiền thông qua hình thức chuyển khoản.\n" +
                        "\n" +
                        "- Thẻ ngân hàng nội địa NAPAS: Hình thức thanh toán qua thẻ nội địa NAPAS dành cho Tài xế sở hữu loại thẻ ATM có in dòng chữ NAPAS phía trên và có đăng ký sử dụng tính năng thanh toán trực tuyến (internet banking).\n" +
                        "\n" +
                        "- Các hình thức thanh toán khác: Theo chính sách của Công ty tại từng thời điểm.\n" +
                        "\n" +
                        "Foody không chịu trách nhiệm cũng như nghĩa vụ nào đối với bất kỳ tổn thất hoặc thiệt hại nào mà Tài xế phải chịu từ lỗi của Tài xế khi sử dụng phương thức thanh toán được liệt kê ở khoản 6.2 ở trên.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "7. DỊCH VỤ GIAO NHẬN\n" +
                        "\n" +
                        "7.1. Dịch vụ giao nhận là dịch vụ được thực hiện bởi Tài xế dưới sự quản lý của Foody để vận chuyển hàng hóa đến Khách hàng. Thỏa thuận cụ thể về việc cung cấp dịch vụ giao nhận giữa Tài xế và Foody được thể hiện trong hợp đồng riêng được ký kết và xác nhận bởi hai bên.\n" +
                        "\n" +
                        "7.2. Thông qua ShopeeFood - Đối tác Tài xế, Tài xế có thể nhận các thông tin liên quan đến đơn hàng để cung cấp dịch vụ giao nhận cho Khách hàng, bao gồm nhưng không giới hạn: thông tin hàng hóa cần giao, địa chỉ lấy hàng, địa chỉ giao hàng, thông tin liên hệ của Khách hàng, thời gian giao hàng, khoảng cách di chuyển từ nơi lấy hàng đến nơi giao hàng, tiền ship đơn hàng, số tiền hàng cần trả quán, số tiền hàng phải thu khách, ghi chú đơn hàng, trạng thái đơn hàng…\n" +
                        "\n" +
                        "7.3. Quy trình giao nhận thông qua ShopeeFood - Đối tác Tài xế:\n" +
                        "\n" +
                        "7.3.1. Quy trình thực hiện đơn Food/Fresh: Tài xế thực hiện theo quy trình được quy định tại đây.\n" +
                        "\n" +
                        "7.3.2. Quy trình thực hiện đơn SPX Instant: Tài xế thực hiện theo quy trình được quy định tại đây.\n" +
                        "\n" +
                        "7.4. Quy định về Đặt Cọc:\n" +
                        "\n" +
                        "7.4.1. Để đảm bảo được thực hiện đơn hàng, Tài xế phải đặt cọc trước một khoản tiền theo quy định, chính sách của Công ty Cổ phần Foody tại từng thời điểm. Khoản tiền đặt cọc này được xem như một khoản bảo đảm cho Tài xế được Foody giao cho thực hiện các đơn hàng (Sau đây gọi là “Khoản Đặt Cọc”). Khoản Đặt Cọc này sẽ được thông báo đến Tài xế thông qua Ứng dụng ShopeeFood - Đối tác Tài xế tại từng thời điểm. Khoản Đặt Cọc này được ghi nhận & hiển thị ở Tài khoản ký quỹ trên ứng dụng ShopeeFood - Đối tác Tài xế.\n" +
                        "\n" +
                        "7.4.2. Tài xế theo đây đồng ý và ủy quyền không hủy ngang cho Foody được toàn quyền cấn trừ Khoản Đặt Cọc này vào các khoản tiền nợ của Tài xế hoặc bất cứ khoản tiền nào mà Tài xế giữ thay cho Foody tại từng thời điểm vào bất kỳ lúc nào mà không cần sự chấp thuận của Tài xế. Tài xế đồng ý rằng các quyền của Foody theo khoản này là độc lập với nghĩa vụ thanh toán của Tài xế theo thỏa thuận giữa Tài xế và Foody.\n" +
                        "\n" +
                        "7.4.3. Khoản Đặt Cọc sẽ được hoàn lại cho Tài xế, không tính lãi, tại thời điểm chấm dứt hợp tác một cách hợp lệ theo hợp đồng đã được ký kết và xác nhận bởi Tài xế và Foody, sau khi cấn trừ tất cả các khoản tiền chưa được thanh toán bởi Tài xế (nếu có). Foody sẽ hoàn trả Khoản Đặt Cọc này trong vòng 15 ngày sau khi Tài xế hoàn tất thanh toán các khoản phải thanh toán cho Foody.\n" +
                        "\n" +
                        "Những nội dung nào không được đề cập trong Quy định này sẽ được quy định trong Điều Khoản Dịch Vụ, các văn bản được ký bởi các bên (nếu có), và các Chính sách của Foody dành cho Tài xế.\n" +
                        "\n" +
                        "7.4.4. Cấn trừ tiền cọc:\n" +
                        "\n" +
                        "Trong trường hợp Tài xế không hoạt động quá 1 (một) năm kể từ ngày gần nhất phát sinh đơn hàng cuối cùng, Foody được quyền cấn trừ Khoản Đặt Cọc với tất cả các khoản tiền chưa được thanh toán bởi Tài xế (nếu có).\n" +
                        "\n" +
                        "7.4.5. Xử lý số dư công nợ sau cấn trừ tiền cọc:\n" +
                        "\n" +
                        "Trong trường hợp Tài xế không hoạt động quá 3 (ba) năm kể từ ngày gần nhất phát sinh đơn hàng cuối cùng, Foody được quyền xử lý số dư công nợ sau khi đã cấn trừ tiền cọc. Cụ thể:\n" +
                        "\n" +
                        "7.4.5.1. Trường hợp Khoản Đặt Cọc đủ để sau khi cấn trừ các khoản tiền chưa được Tài xế thanh toán có số dư dương: Foody sẽ tiến hành liên hệ và hoàn trả khoản tiền còn thừa cho Tài xế.\n" +
                        "\n" +
                        "7.4.5.2. Trường hợp Khoản Đặt Cọc không đủ để cấn trừ các khoản tiền chưa được Tài xế thanh toán: Foody sẽ tiến hành liên hệ và buộc Tài xế có nghĩa vụ thanh toán khoản tiền còn thiếu cho Foody.\n" +
                        "\n" +
                        "7.5. Quy định thời gian hoạt động và việc duy trì số dư dương trên Ví Tài xế:\n" +
                        "\n" +
                        "7.5.1. Để đảm bảo việc nhận đơn hàng không bị gián đoạn, Tài xế ShopeeFood cần đảm bảo tính hoạt động liên tục. Tài khoản Tài xế sẽ bị tạm ngừng khả năng nhận đơn nếu không hoàn thành bất kỳ đơn hàng nào trong 120 ngày gần nhất.\n" +
                        "\n" +
                        "7.5.2. Tài xế cần đảm bảo tổng số dư trên Ví Tài xế luôn là số dương, nghĩa là Tài xế không có công nợ phải trả đối với Foody. Trong trường hợp số dư Ví Tài xế bị âm, Foody sẽ thông báo trên ứng dụng ShopeeFood - Đối tác Tài xế của Tài xế để Tài xế nhận biết và thực hiện các thao tác cần thiết để dịch vụ không gián đoạn.\n" +
                        "\n" +
                        " \n" +
                        "8. PHÍ\n" +
                        "\n" +
                        "8.1. Nếu Điều Khoản Dịch Vụ này, thỏa thuận và/hoặc hợp đồng riêng biệt giữa các bên (nếu có) không có quy định nào khác hoặc các bên không có thỏa thuận nào khác, Tài xế không phải thanh toán bất kỳ khoản phí nào khi sử dụng ShopeeFood - Đối tác Tài xế.\n" +
                        "\n" +
                        "8.2. Nếu có bất kỳ khoản phí nào phải trả, khoản phí đó cũng là đối tượng chịu thuế theo quy định pháp luật có liên quan. Tài xế thừa nhận và đồng ý rằng Foody có thể khấu trừ các khoản thuế, phí phải trả của Tài xế.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "9. PHẢN HỒI\n" +
                        "\n" +
                        "Foody luôn đón nhận những thông tin và phản hồi từ phía Tài xế nhằm giúp Foody cải thiện chất lượng Dịch Vụ. Vui lòng xem thêm quy trình phản hồi của Foody dưới đây:\n" +
                        "\n" +
                        "- Phản hồi có thể được Tài xế gửi trực tiếp trên ShopeeFood - Đối tác Tài xế hoặc gửi qua email của Foody.\n" +
                        "\n" +
                        "- Tất cả các phản hồi ẩn danh đều không được chấp nhận.\n" +
                        "\n" +
                        "- Tài xế liên quan đến phản hồi sẽ được thông báo đầy đủ và được tạo cơ hội cải thiện tình hình.\n" +
                        "\n" +
                        "- Những phản hồi không rõ ràng và mang tính phỉ báng sẽ không được chấp nhận.\n" +
                        "\n" +
                        " \n" +
                        "10.LOẠI TRỪ TRÁCH NHIỆM\n" +
                        "\n" +
                        "10.1. Foody KHÔNG ĐẢM BẢO RẰNG DỊCH VỤ, ShopeeFood - Đối tác Tài xế HOẶC CÁC CHỨC NĂNG ĐƯỢC TÍCH HỢP TRONG ĐÓ LUÔN CÓ SẴN, CÓ THỂ TRUY CẬP, KHÔNG BỊ GIÁN ĐOẠN, ĐÚNG LÚC, AN TOÀN, CHÍNH XÁC, HOÀN THIỆN HOẶC KHÔNG BỊ LỖI, RẰNG CÁC LỖI PHÁT SINH, NẾU CÓ, SẼ ĐƯỢC KHẮC PHỤC, HOẶC RẰNG ShopeeFood - Đối tác Tài xế VÀ/HOẶC MÁY CHỦ SẼ SẴN CÓ NHỮNG CHỨC NĂNG AN TOÀN VỚI VI RÚT, PHẦN MỀM GIÁN ĐIỆP, MÃ ĐỘC HOẶC BẤT CỨ MÃ, LỆNH, CHƯƠNG TRÌNH HOẶC THÀNH TỐ CÓ HẠI NÀO KHÁC.\n" +
                        "\n" +
                        "10.2. TÀI XẾ CẦN THỪA NHẬN RẰNG MỌI RỦI RO PHÁT SINH TỪ HOẶC NGOÀI VIỆC SỬ DỤNG HOẶC VẬN HÀNH CỦA ShopeeFood - Đối tác Tài xế VÀ/HOẶC DỊCH VỤ ShopeeFood - Đối tác Tài xế SẼ THUỘC VỀ TÀI XẾ TRONG GIỚI HẠN TỐI ĐA PHÁP LUẬT CHO PHÉP.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "11. CÁC LOẠI TRỪ VÀ GIỚI HẠN TRÁCH NHIỆM\n" +
                        "\n" +
                        "11.1. TRONG GIỚI HẠN TỐI ĐA PHÁP LUẬT CHO PHÉP, KHÔNG MỘT TRƯỜNG HỢP NÀO RÀNG BUỘC FOODY CHỊU TRÁCH NHIỆM DÙ PHÁT SINH TỪ HỢP ĐỒNG, BẢO HÀNH, LỖI (BAO GỒM, NHƯNG KHÔNG GIỚI HẠN, NHỮNG BẤT CẨN (DÙ CHỦ ĐỘNG, BỊ ĐỘNG HOẶC BỊ QUY GÁN), TRÁCH NHIỆM ĐỐI VỚI SẢN PHẨM, TRÁCH NHIỆM PHÁP LÝ HOẶC TRÁCH NHIỆM KHÁC), HOẶC NGUYÊN NH N KHÁC LIÊN QUAN ĐẾN LUẬT PHÁP, QUYỀN LỢI CHÍNH ĐÁNG, CÁC QUY CHẾ HOẶC CÁC HÌNH THỨC KHÁC ĐỐI VỚI:\n" +
                        "\n" +
                        "(i) TỔN THẤT VỀ SỬ DỤNG; (ii) TỔN THẤT VỀ LỢI NHUẬN; (iii) TỔN THẤT VỀ DOANH THU; (iv) TỔN THẤT VỀ DỮ LIỆU; (v) TỔN THẤT VỀ UY TÍN; HOẶC (vi) KHÔNG THỰC HIỆN ĐƯỢC CÁC BIỆN PHÁP KHẨN CẤP TẠM THỜI, ĐỐI VỚI TỪNG TRƯỜNG HỢP DÙ TRỰC TIẾP HOẶC GIÁN TIẾP; HOẶC\n" +
                        "\n" +
                        "BẤT KỲ THIỆT HẠI GIÁN TIẾP, NGẪU NHIÊN, ĐẶC BIỆT HOẶC MANG TÍNH HỆ QUẢ NÀO (GỒM BẤT KỲ MẤT MÁT NÀO VỀ DỮ LIỆU, GIÁN ĐOẠN DỊCH VỤ, MÁY TÍNH, ĐIỆN THOẠI HOẶC CÁC THIẾT BỊ DI ĐỘNG KHÁC) PHÁT SINH TỪ HOẶC LIÊN QUAN ĐẾN VIỆC SỬ DỤNG HOẶC NGOÀI KHẢ NĂNG SỬ DỤNG ShopeeFood - Đối tác Tài xế HOẶC DỊCH VỤ, BAO GỒM NHƯNG KHÔNG GIỚI HẠN, BẤT KỲ THIỆT HẠI NÀO PHÁT SINH TỪ ĐÓ, NGAY CẢ KHI FOODY ĐÃ ĐƯỢC CHO HAY VỀ KHẢ NĂNG CỦA CÁC THIỆT HẠI ĐÓ HOẶC ĐƯỢC GỢI Ý PHẢI CHỊU TRÁCH NHIỆM.\n" +
                        "\n" +
                        "11.2. TÀI XẾ THỪA NHẬN VÀ ĐỒNG Ý RẰNG QUYỀN DUY NHẤT CỦA TÀI XẾ LIÊN QUAN ĐẾN CÁC SỰ CỐ HOẶC SỰ KHÔNG THỎA MÃN VỚI DỊCH VỤ LÀ YÊU CẦU CHẤM DỨT TÀI KHOẢN CỦA TÀI XẾ VÀ/HOẶC DỪNG SỬ DỤNG DỊCH VỤ.\n" +
                        "\n" +
                        "11.3. BẤT KỂ CÁC ĐIỀU KHOẢN BÊN TRÊN, TRƯỜNG HỢP FOODY, THEO PHÁN QUYẾT CỦA TÒA ÁN CÓ THẨM QUYỀN, PHẢI CHỊU TRÁCH NHIỆM TRONG GIỚI HẠN TỐI ĐA PHÁP LUẬT CHO PHÉP (BAO GỒM ĐỐI VỚI LỖI BẤT CẨN KHÔNG ĐÁNG KỂ) THÌ TRÁCH NHIỆM CỦA FOODY ĐỐI VỚI TÀI XẾ HOẶC BẤT KỲ BÊN THỨ BA NÀO CHỈ GIỚI HẠN TRONG MỨC THẤP HƠN CỦA (A) THU NHẬP CỦA TÀI XẾ THEO ĐƠN HÀNG LIÊN QUAN VÀ GIÁ TRỊ MÀ TÀI XẾ ĐÃ ỨNG TRƯỚC THEO ĐƠN HÀNG LIÊN QUAN (NẾU CÓ) HOẶC (B) KHOẢN TIỀN KHÁC NHƯ XÁC ĐỊNH CỤ THỂ TRONG PHÁN QUYẾT CHUNG THẨM CỦA TÒA ÁN CÓ THẨM QUYỀN.\n" +
                        "\n" +
                        "11.4. KHÔNG CÓ NỘI DUNG NÀO TRONG ĐIỀU KHOẢN DỊCH VỤ NÀY SẼ GIỚI HẠN HOẶC LOẠI TRỪ BẤT KỲ TRÁCH NHIỆM NÀO ĐỐI VỚI THƯƠNG VONG HOẶC TAI NẠN CÁ NH N DO SỰ BẤT CẨN, GIAN LẬN CỦA FOODY HOẶC BẤT KỲ TRÁCH NHIỆM NÀO KHÁC CÓ PHẦN THAM GIA CỦA FOODY MÀ TRÁCH NHIỆM ĐÓ KHÔNG ĐƯỢC HẠN CHẾ VÀ/HOẶC LOẠI TRỪ THEO QUY ĐỊNH PHÁP LUẬT.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "12. LIÊN KẾT HOẶC TÍCH HỢP VỚI NỀN TẢNG HOẶC TRANG WEB CỦA BÊN THỨ BA\n" +
                        "\n" +
                        "12.1. Các đường dẫn của bên thứ ba có trên ShopeeFood - Đối tác Tài xế sẽ có thể khiến Tài xế thoát khỏi ShopeeFood - Đối tác Tài xế. Các đường dẫn này được cung cấp mang tính chất xã giao, và các trang điện tử truy cập từ các đường dẫn này không thuộc sự kiểm soát của Foody dưới bất kỳ hình thức nào và vì thế Tài xế phải tự chịu các rủi ro khi truy cập vào những trang điện tử này. Foody dù bất kỳ hình thức nào, không chịu trách nhiệm đối với những nội dung của bất kỳ trang điện tử nào được dẫn truyền hoặc bất kỳ đường dẫn nào trên những trang điện tử đó. Foody cung cấp các đường dẫn này đơn thuần để tạo sự thuận lợi, và việc có bao gồm đường dẫn bất kỳ sẽ không, dù bất kỳ hình thức nào, hàm ý hoặc thể hiện việc liên kết, xác nhận hoặc bảo trợ của Foody đối với bất kỳ trang điện tử được liên kết nào và/hoặc bất kỳ nội dung nào trong đó.\n" +
                        "\n" +
                        "12.2. ShopeeFood - Đối tác Tài xế và/hoặc Dịch Vụ của Foody có thể được tích hợp trên nền tảng hoặc trang web của bên thứ ba. Khi sử dụng Dịch Vụ trên nền tảng hoặc trang web của bên thứ ba, Tài xế đồng thời đồng ý và sẽ tuân thủ các điều khoản dịch vụ của bên thứ ba đó. Tài xế cũng đồng ý rằng Foody không chịu trách nhiệm về dịch vụ của bên thứ ba và/hoặc tính chính xác của hàng hóa/dịch vụ, bao gồm các chức năng, độ tin cậy, an ninh, chính sách bảo mật hoặc các hoạt động khác của các nền tảng hoặc trang web bên thứ ba đó.\n" +
                        "\n" +
                        "12.3. Khi Tài xế sử dụng Dịch Vụ trên nền tảng hoặc trang web của bên thứ ba, Chúng tôi tuyệt đối không tiết lộ dữ liệu cá nhân, mà Tài xế đã cung cấp cho Foody, cho bên thứ ba trừ khi việc tiết lộ đó là bắt buộc để xử lý yêu cầu của chính Tài xế và trong trường hợp đó, các nền tảng hoặc trang web của bên thứ ba cũng sẽ phải tuân thủ Chính sách bảo mật của chúng tôi.\n" +
                        "\n" +
                        "12.4. Trong trường hợp Tài xế tự cung cấp dữ liệu cá nhân của mình cho các nền tảng hoặc trang web của bên thứ ba trong quá trình sử dụng Dịch vụ của Foody, thông qua các cổng thông tin được phát triển bởi bên thứ ba, nghĩa là Tài xế đồng ý rằng mọi tiết lộ, việc sử dụng, thu thập và/hoặc xử lý dữ liệu cá nhân của Tài xế sẽ phải tuân theo chính sách bảo mật của bên thứ ba đó.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "13. ĐÓNG GÓP CỦA BÊN THỨ BA ĐỐI VỚI DỊCH VỤ VÀ CÁC ĐƯỜNG DẪN BÊN NGOÀI\n" +
                        "\n" +
                        "13.1. Mỗi một bên đóng góp cho Dịch Vụ về dữ liệu, tin nhắn, hình ảnh, âm thanh, video, phần mềm và Nội Dung khác, sẽ chịu trách nhiệm về độ chính xác, độ tin cậy, tính nguyên bản, rõ ràng các quyền, tính tuân thủ pháp luật và các giới hạn pháp lý liên quan đến bất kỳ Nội Dung đóng góp nào. Như vậy, Foody không chịu trách nhiệm, và cũng không phải, thường xuyên giám sát hoặc kiểm tra độ chính xác, độ tin cậy, tính nguyên bản, rõ ràng các quyền, tính tuân thủ pháp luật và các giới hạn pháp lý liên quan đến bất kỳ Nội Dung đóng góp nào. Tài xế đồng ý giữ vô hại cho Foody trước tất cả các hành động, khiếu nại của bên thứ ba, tổn thất, thiệt hại, trách nhiệm, bao gồm cả phí luật sư, phát sinh từ bất kỳ các hoạt động hoặc không hoạt động nào của Tài xế, bao gồm nhưng không giới hạn, các vấn đề Người Bán đăng tải hoặc bằng cách khác tạo sẵn qua Dịch Vụ.\n" +
                        "\n" +
                        "13.2. Ngoài ra, ShopeeFood - Đối tác Tài xế có thể chứa các đường dẫn tới sản phẩm, website, dịch vụ và các mời chào của bên thứ ba. Những đường dẫn, sản phẩm, websites, dịch vụ và các mời chào của bên thứ ba này không thuộc sở hữu và quản lý của Foody. Thực tế, các sản phẩm, website, dịch vụ và các mời chào đó được thực hiện bởi, và là tài sản của bên thứ ba tương ứng, và được bảo vệ bởi luật pháp và các điều ước về bản quyền hoặc quyền sở hữu trí tuệ khác. Foody không xem xét, và không có trách nhiệm đối với những nội dung, chức năng, độ an toàn, dịch vụ, các chính sách bảo mật, hoặc các hoạt động khác của những bên thứ ba này. Foody khuyến khích Tài xế tìm hiểu các điều khoản và các chính sách khác được niêm yết bởi bên thứ ba trên các website hoặc phương tiện khác của họ. Bằng việc sử dụng Dịch Vụ, Tài xế thừa nhận rằng Foody không chịu trách nhiệm dưới bất kỳ hình thức nào do việc Tài xế sử dụng, hoặc không thể sử dụng được bất kỳ website hay ứng dụng nào. Ngoài ra Tài xế thừa nhận và đồng ý rằng Foody có thể vô hiệu hóa việc sử dụng của Tài xế, hoặc gỡ bỏ, bất kỳ các đường dẫn của bên thứ ba nào, hoặc các ứng dụng trên Dịch Vụ trong giới hạn bên thứ ba vi phạm các Điều Khoản Dịch Vụ này.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "14. KHẲNG ĐỊNH VÀ ĐẢM BẢO CỦA TÀI XẾ\n" +
                        "\n" +
                        "Tài xế khẳng định và đảm bảo rằng:\n" +
                        "\n" +
                        "- Tài xế sở hữu năng lực, quyền và khả năng hợp pháp để đọc, hiểu, chấp thuận và tuân thủ Điều Khoản Dịch Vụ này; và\n" +
                        "\n" +
                        "- Tài xế chỉ sử dụng Dịch Vụ cho các mục đích hợp pháp và theo quy định của Điều Khoản Dịch Vụ này cũng như tất cả các luật, quy tắc, quy chuẩn, chỉ thị, hướng dẫn, chính sách và quy định áp dụng.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "15. BỒI THƯỜNG\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Tài xế đồng ý bồi thường, bảo vệ và không gây thiệt hại cho Foody, các cổ đông, công ty con, công ty liên kết, giám đốc, viên chức, đại lý, đồng sở hữu thương hiệu hoặc đối tác, và nhân viên của Foody (gọi chung là “Bên Được Bồi Thường”) liên quan đến hoặc đối với bất kỳ và tất cả khiếu nại, hành động, thủ tục tố tụng, và các vụ kiện cũng như tất cả nghĩa vụ, tổn thất, thanh toán, khoản phạt, tiền phạt, chi phí và phí tổn có liên quan (bao gồm, nhưng không giới hạn, bất kỳ chi phí giải quyết tranh chấp nào khác) do bất kỳ Bên Được Bồi Thường nào gánh chịu, phát sinh từ hoặc có liên quan đến (a) bất kỳ hoạt động nào được thực hiện trên ShopeeFood - Đối tác Tài xế, hoặc bất kỳ tranh chấp nào liên quan đến hoạt động đó, (b) vi phạm hoặc không tuân thủ bất kỳ điều khoản nào trong Điều Khoản Dịch Vụ này hoặc bất kỳ chính sách hoặc hướng dẫn nào được tham chiếu theo đây, (c) việc Tài xế sử dụng hoặc sử dụng không đúng Dịch Vụ, hoặc (d) vi phạm của Tài xế đối với bất kỳ luật hoặc bất kỳ các quyền của bên thứ ba nào, hoặc (e) bất kỳ Nội Dung nào được truyền tải, công khai bởi Tài xế.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "16. TÍNH RIÊNG RẼ\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Nếu bất kỳ điều khoản nào trong Điều Khoản Dịch Vụ này không hợp pháp, bị bãi bỏ, hoặc vì bất kỳ lý do nào không thể thực thi theo pháp luật, thì điều khoản đó sẽ được tách ra khỏi các điều khoản và điều kiện này và sẽ không ảnh hưởng tới hiệu lực cũng như tính thi hành của bất kỳ điều khoản còn lại nào cũng như không ảnh hưởng tới hiệu lực cũng như tính thi hành của điều khoản sẽ được xem xét theo luật.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "17. LUẬT ÁP DỤNG\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Điều Khoản Dịch Vụ này sẽ được điều chỉnh bởi và diễn giải theo luật pháp của Việt Nam. Bất kỳ tranh chấp, tranh cãi, khiếu nại hoặc sự bất đồng dưới bất cứ hình thức nào phát sinh từ hoặc liên quan đến Điều Khoản Dịch Vụ này chống lại hoặc liên quan đến Foody hoặc bất kỳ Bên Được Bồi Thường nào thuộc đối tượng của Điều Khoản Dịch Vụ này sẽ được giải quyết bằng Trung tâm Trọng tài Quốc tế Việt Nam (VIAC) theo Quy tắc tố tụng trọng tài của Trung tâm này. Ngôn ngữ phán xử của trọng tài là Tiếng Việt.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "18. QUY ĐỊNH CHUNG\n" +
                        "\n" +
                        "Tài xế đồng ý và Foody bảo lưu tất cả các quyền sau đây:\n" +
                        "\n" +
                        "- Foody có quyền chỉnh sửa Điều Khoản Dịch Vụ này vào bất cứ thời điểm nào thông qua việc đăng tải Điều Khoản Dịch Vụ được chỉnh sửa lên ShopeeFood - Đối tác Tài xế. Việc Tài xế tiếp tục sử dụng ShopeeFood - Đối tác Tài xế sau khi các thay đổi được đăng tải sẽ cấu thành trong và được xem là việc Tài xế đã chấp nhận Điều Khoản Dịch Vụ được chỉnh sửa.\n" +
                        "\n" +
                        "- Tài xế không được phép chuyển giao, cấp lại quyền hoặc chuyển nhượng bất kỳ các quyền hoặc nghĩa vụ được cấp cho Tài xế theo đây.\n" +
                        "\n" +
                        "- Không một quy định nào trong Điều Khoản Dịch Vụ này sẽ tạo thành liên doanh hoặc quan hệ đại lý – chủ sở hữu giữa Tài xế và Foody.\n" +
                        "\n" +
                        "- Tại bất kỳ hoặc các thời điểm nào, nếu Foody không thể thực hiện được bất kỳ điều khoản nào theo đây sẽ không ảnh hưởng, dưới bất kỳ hình thức nào, đến các quyền của Foody vào thời điểm sau đó để thực thi các quyền này trừ khi việc thực thi các quyền này được miễn trừ bằng văn bản.\n" +
                        "\n" +
                        "- Điều Khoản Dịch Vụ này, tất cả các quyền và nghĩa vụ của Foody theo Điều Khoản Dịch Vụ này có thể được chuyển nhượng, chuyển giao, hoán đổi, hoặc thương lượng ngược lại bởi Foody cho bên được chỉ định bởi Foody mà không cần có sự cho phép hoặc chấp thuận của Tài xế. Tài xế cam kết thực hiện tất cả các công việc và ký kết các tài liệu cần thiết để tạo điều kiện cho việc chuyển nhượng, chuyển giao, hoán đổi hoặc thương lượng này.\n" +
                        "\n" +
                        "- Điều Khoản Dịch Vụ này hoàn toàn phục vụ cho lợi ích của Foody và Tài xế mà không vì lợi ích của bất kỳ cá nhân hay tổ chức nào khác, trừ các công ty liên kết và các công ty con của Foody (và cho từng bên kế thừa hay bên nhận chuyển giao của Foody hoặc của các công ty liên kết và công ty con của Foody).\n" +
                        "\n" +
                        "18.7. Các điều khoản được quy định trong Điều Khoản Dịch Vụ này và bất kỳ các thỏa thuận và chính sách được bao gồm hoặc được dẫn chiếu trong các Điều Khoản Dịch Vụ này cấu thành sự thỏa thuận và cách hiểu tổng thể của các bên đối với Dịch Vụ và thay thế bất kỳ thỏa thuận hoặc cách hiểu trước đây giữa các bên liên quan đến vấn đề đó. Với việc các bên giao kết thỏa thuận được tạo thành theo Điều Khoản Dịch Vụ này, các bên không dựa vào bất kỳ tuyên bố, khẳng định, đảm bảo, cách hiểu, cam kết, lời hứa hoặc cam đoan nào của bất kỳ người nào trừ những điều được nêu rõ trong Điều Khoản Dịch Vụ này. Điều Khoản Dịch Vụ này có thể sẽ không mâu thuẫn, giải thích hoặc bổ sung như là bằng chứng của các thỏa thuận trước, bất kỳ thỏa thuận miệng hiện tại nào hoặc bất kỳ các điều khoản bổ sung nhất quán nào.\n" +
                        "\n" +
                        "Tài xế đồng ý tuân thủ mọi quy định pháp luật hiện hành liên quan đến chống tham nhũng và chống hối lộ.\n" +
                        "\n" +
                        "Nếu Tài xế có bất kỳ câu hỏi hay thắc mắc nào liên quan đến Điều Khoản Dịch Vụ này hoặc các vấn đề phát sinh liên quan đến Dịch Vụ trên ShopeeFood - Đối tác Tài xế, vui lòng liên hệ Foody tại đây.\n" +
                        "\n" +
                        "THÔNG BÁO PHÁP LÝ: Xin vui lòng gửi tất cả các thông báo pháp lý đến Foody: tại đây.\n" +
                        "\n" +
                        "TÔI ĐÃ ĐỌC CÁC ĐIỀU KHOẢN DỊCH VỤ NÀY VÀ ĐỒNG Ý VỚI TẤT CẢ CÁC ĐIỀU KHOẢN NHƯ TRÊN CŨNG NHƯ BẤT KỲ ĐIỀU KHOẢN NÀO ĐƯỢC CHỈNH SỬA SAU NÀY.",
                "Nội dung chi tiết của Điều khoản dịch vụ Tài xế khu vực...",
                "Nội dung chi tiết của Chính sách bảo mật..."
        };

        // Duyệt qua các tiêu đề để tạo dữ liệu cho ExpandableListView
        for (int i = 0; i < groupTitles.length; i++) {
            // Nhóm chính
            Map<String, String> group = new HashMap<>();
            group.put("GROUP_KEY", groupTitles[i]);
            groupList.add(group);

            // Nội dung con
            List<Map<String, String>> childItems = new ArrayList<>();
            Map<String, String> child = new HashMap<>();
            child.put("CHILD_KEY", childContents[i]);
            childItems.add(child);
            childList.add(childItems);
        }

        // Sử dụng SimpleExpandableListAdapter để hiển thị dữ liệu
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getContext(),
                groupList,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{"GROUP_KEY"},
                new int[]{android.R.id.text1},
                childList,
                android.R.layout.simple_list_item_1,
                new String[]{"CHILD_KEY"},
                new int[]{android.R.id.text1}
        );

        expandableListView.setAdapter(adapter);
        return view;
    }
}